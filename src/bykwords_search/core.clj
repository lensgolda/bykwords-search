(ns bykwords-search.core
  (:gen-class)
  (:require [org.httpkit.server :refer [run-server]]
            [ring.middleware.params :refer :all]
            [ring.middleware.reload :refer [wrap-reload]]
            [bykwords-search.handler :refer [app-routes]]
            [ring.middleware.json :refer [wrap-json-response]]))

(defn -main
  "Entry point"
  [& args]
  (-> app-routes
      (wrap-params {:encoding "UTF-8"})
      (wrap-json-response)
      (wrap-reload)
      (run-server {:port 5000}))                                        ;; default port starts at 5000
  (println (str "Server started...Started at http://localhost:5000")))