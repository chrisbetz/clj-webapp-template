(ns {{sanitized-ns}}.server
	(:require 
		[ring.adapter.jetty :as jetty]
		[{{sanitized-ns}}.routes :as routes]
		)
	(:gen-class))

(defn init []
  (println "myapp is starting"))

(defn destroy []
  (println "myapp is shutting down"))
