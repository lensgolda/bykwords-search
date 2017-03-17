(ns bykwords-search.yapi
    (:require [org.httpkit.client :as http]
              [clojure.string :as str]
              [clojure.pprint :refer [pprint]]
              [clojure.data.xml :refer :all]
              [clojure.data.zip.xml :refer :all]
              [clojure.zip :refer [xml-zip]]))

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
        (clojure.data.xml/parse-str)
        (clojure.zip/xml-zip)
        (clojure.data.zip.xml/xml->
            :channel
            :item 
            :link
            clojure.data.zip.xml/text)))

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
                (pprint (get-domains response)))))))

