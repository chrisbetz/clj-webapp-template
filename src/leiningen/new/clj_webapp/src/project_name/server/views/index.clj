(ns {{sanitized-ns}}.server.views.index
     (:use [hiccup core page])
     (:require [{{sanitized-ns}}.server.views.common :as common]))

(defn index-page []
 (common/layout "Hello World"
   [:div.row [:div.large-12.columns [:h1 "Hello World"]
              [:p#clickable "Click me"]
              ]]))