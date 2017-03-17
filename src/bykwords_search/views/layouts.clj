(ns bykwords-search.views.layouts
  (:use [hiccup.page :only (html5 include-css include-js)]))

(defn app
  " Main app layout "
  [title & content]
  (html5 [:head
          [:title title]
          (include-css "https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.7/css/bootstrap.min.css")
          (include-css "/css/main.css")
          (include-js "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.1.1/jquery.min.js")]
          [:body
           [:div { :id "content" } content ]]))