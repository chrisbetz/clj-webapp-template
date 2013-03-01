(ns {{sanitized-ns}}.server.views.common
     (:use [hiccup core page]))

(defn layout [title & content]
   (html {:mode :html}
     (doctype :html5 )
     "<!--[if IE 8]> 				 <html class='no-js lt-ie9' lang='en'> <![endif]-->
     <!--[if gt IE 8]><!--> <html class='no-js' lang='en'> <!--<![endif]-->"

     [:head [:meta {:charset "utf-8"}]

      "<!-- Set the viewport width to device width for mobile -->"
      [:meta {:name "viewport" :content "width=device-width"}]

      [:title title]
      (include-css "/css/normalize.css")
      (include-css "/css/foundation.min.css")
      (include-css "/css/app.css")
      (include-js "/js/vendor/custom.modernizr.js")]
     [:body content
      (include-js "//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js")
      (include-js "/js/foundation.min.js")
      (include-js "/js/app.js")
      [:script "$(document).foundation();"]] "</html>"
     ))