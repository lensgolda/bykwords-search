(defproject bykwords-search "0.1.0-SNAPSHOT"
  :description "Yandex blog API search by keywords"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ring/ring-defaults "0.3.1"]
                 [compojure "1.6.0"]
                 [ring/ring-json "0.4.0"]
                 [http-kit "2.2.0"]
                 [clj-http "3.7.0"]
                 [hiccup "1.0.5"]
                 [org.clojure/data.xml "0.0.8"]
                 [org.clojure/data.zip "0.1.2"]]
                 [cheshire "5.8.0"]]
  :profiles {:dev {:dependencies [[ring/ring-devel "1.6.2"]]}}
  :main bykwords-search.core/-main
  :aot [bykwords-search.core])
