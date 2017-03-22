(ns bykwords-search.yapi
    (:require [org.httpkit.client :as http]
              [clojure.string :as str]
              [clojure.pprint :refer [pprint]]
              [clojure.data.xml :refer :all]
              [clojure.data.zip.xml :refer :all]
              [clojure.zip :refer [xml-zip]]))

;; Request params for Yandex blog
;; :doclimit keyword as additional parameter for Request
;; to limit number of documents to return from API call
(def yapi-params {  :base-url "http://blogs.yandex.ru/"     ;; Base Yandex blogs API URL
                    :method "search"                        ;; method
                    :doclimit 10                            ;; Limit 10 docs to return
                    :rss "rss"})                            ;; type of response RSS

;; atom for unique link urls
;; to exclude repeated urls from statistics
(def unique-urls (atom #{}))

;; Trim and sanitize input data
(defn split-and-sanitize
    " Trim and sanitize input data "
    [words]
    (->> (str/split words #" ")
         (map #(str/trim %))
         (into #{})))

;; Return base URL for API Request
(defn get-base-url
    " Return base URL for API request "
    [text]
    (let [{:keys [base-url method rss doclimit]} yapi-params]
        (str base-url method "." rss "?numdoc=" doclimit "&text=" text)))

;; Get(parse/extract) links from response of XML type
;; using clojure xml, data, zip libraries
(defn xml-extract-links
    " Get/Parse link from XML "
    [response]
    (-> response
        :body
        (clojure.data.xml/parse-str)
        (clojure.zip/xml-zip)
        (clojure.data.zip.xml/xml->
            :channel
            :item 
            :link
            clojure.data.zip.xml/text)))

;; Get/Extracts full domain from URL
(defn get-domain-from-url
  " Extracts domain from URL "
  [url]
   (-> url
       (str/split #"//")
       (second)
       (str/split #"/")
       (first)))

;; Checks link to be unique in response data
;; using atom unique-urls as set #{}
(defn get-unique
   " Check is link unique "
   [url]
   (when-not (contains? (deref unique-urls) url)
     (swap! unique-urls conj url)
     (get-domain-from-url url)))

;; Global, extract links, check for unique, filter if nil? values
;; and get 2-nd level domain name via regular expression
(defn get-domains
    " Get domains "
    [response]
    (->> response
         (xml-extract-links)
         (map #(get-unique %))
         (into [])
         (remove nil?)
         (map #(first (re-find #"(\w+\.\w+)$" %)))))

;; Sends Requests and collect responses
;; Set requests to Queue by 3 request per attempt
;; using http-kit client which returns responses as promises
(defn get-all
    " Send requests and collect responses "
    [keywords]
    (let [kwords (split-and-sanitize keywords)
          queue (partition-all 3 kwords)]
      (for [part queue]
        (let [urls (map #(get-base-url %) part)
              promises (doall (map http/get urls))
              results (doall (map deref promises))]
            (for [response results]
                (get-domains response))))))

;; Global, get statistics
(defn get-statistics
  " Get domains statistics "
  [kwords]
  (reset! unique-urls #{})
  (-> kwords
      get-all
      flatten
      frequencies))