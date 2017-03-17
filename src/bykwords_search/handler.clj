(ns bykwords-search.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [bykwords-search.views.layouts :as layouts]
            [bykwords-search.views.contents :as contents]
            [bykwords-search.yapi :as yapi]))

(defroutes app-routes
   (GET "/"
        {params :params}
        (layouts/app "Solar test" (contents/index "Поиск по записям и комментариям")))
   (GET "/search"
        {{:strs [keywords]} :params}
        (yapi/get-all keywords))
            ;;(layouts/app "Results" (contents/results keywords))))
   (route/not-found (layouts/app "Not Found" "Not Found")))