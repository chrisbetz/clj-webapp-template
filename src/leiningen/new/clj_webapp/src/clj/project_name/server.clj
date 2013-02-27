(ns {{sanitized-ns}}.server
	(:require 
		[ring.adapter.jetty :as jetty]
		[{{sanitized-ns}}.routes :as routes]
		)
	(:gen-class))


(defn -main [& args]
	(jetty/run-jetty (routes/app) {:port 3000}))

