(defproject bykwords-search "0.1.0-SNAPSHOT"
  :description "Yandex blog API search by keywords"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ring/ring-defaults "0.2.3"]
                 [compojure "1.5.2"]
                 [ring/ring-json "0.4.0"]
                 [http-kit "2.2.0"]
                 [hiccup "1.0.5"]
                 [org.clojure/data.xml "0.0.8"]
                 [org.clojure/data.zip "0.1.2"]]
  :profiles {:dev {:dependencies [[ring/ring-devel "1.5.1"]]}}
  :main bykwords-search.core/-main
  :aot [bykwords-search.core])
