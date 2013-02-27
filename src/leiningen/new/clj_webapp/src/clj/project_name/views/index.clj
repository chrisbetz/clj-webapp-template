(ns {{sanitized-ns}}.views.index
	(:use 
		[hiccup core page])
	(:require 
		[{{sanitized-ns}}.views.common :as common]))

(defn index-page []
	(common/layout "Hello World"
		[:div.row [:div.twelve.columns
		[:h1 "Hello World"]
		[:p#clickable "Click me"]
		]]))