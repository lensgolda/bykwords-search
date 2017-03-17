(ns bykwords-search.yapi
    (:require [org.httpkit.client :as http]
              [clojure.string :as str]
              [clojure.pprint :as pp]
              [clojure.data.xml :as xml]
              [clojure.data.zip.xml :as zipxml]
              [clojure.zip :as zip]))

(def yapi-params {  :base-url "http://blogs.yandex.ru/"     ;; Base Yandex blogs API URL
                    :method "search"                        ;; method
                    :doclimit 10                            ;; Limit 10 docs to return
                    :rss "rss"})                            ;; type of response RSS

(defn split-and-sanitize
    " Sanitize input "
    [words]
    (->> (str/split words #" ")
         (map #(str/trim %))
         (into #{})))

(defn get-base-url
    " Return base URL for API request "
    [text]
    (let [{:keys [base-url method rss doclimit]} yapi-params]
        (str base-url method "." rss "?numdoc=" doclimit "&text=" text)))

(defn get-links
    " Get/Parse link from XML "
    [response]
    (-> response
        :body
        (xml/parse-str)
        (zip/xml-zip)
        (zipxml/xml-> 
            :channel
            :item 
            :link
            zipxml/text)))

(defn get-domains
    " Get domains "
    [response]
    (->> response
         (get-links)
         (into [])))

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
                (pp/pprint (get-domains response)))))))

