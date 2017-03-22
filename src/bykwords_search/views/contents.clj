(ns bykwords-search.views.contents
  (:require [hiccup.core :as hiccup]
    [hiccup.form :as form]))

;; Views helpers
(defn wrap-container-row
  " Wraps inner into div.container > div.row "
  [inner]
  [:div {:class "container"}
   [:div {:class "row"} [:div {:class "col-md-12"} inner]]])

(defn wrap-panel
  "Wraps with .panel.panel-default"
  [phead pbody]
  [:div {:class "panel panel-default"}
   [:div {:class "panel-heading"} phead]
   [:div {:class "panel-body"} pbody]])

;; Search form
(def searchform
  [:form {:action "/search" :method "GET"}
    [:div {:class "col-md-4"} [:label "Введите ключевое слово для поиска"
      [:input {:class "form-control" :name "keywords"}]]]
    [:div {:class "col-md-2"} 
      [:label "&nbsp;" 
        [:input {:type "submit" :value "Поиск" :class "form-control btn btn-primary"}]]]])

;; Views
(defn index
  " Route / "
  [head & content]
  (wrap-container-row (wrap-panel head searchform)))

(defn results
  "Results page"
  [content & params]
  (wrap-container-row
    (wrap-panel "Results"
      (for [[k v] content]
        [:p k ": " v]))))