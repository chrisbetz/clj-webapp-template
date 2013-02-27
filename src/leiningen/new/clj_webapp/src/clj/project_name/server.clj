(ns {{sanitized-ns}}.server
  (:require [ring.adapter.jetty :as jetty]
            [{{sanitized-ns}}.routes :as routes]
            [clojure.tools.logging :as log]
            )
  (:gen-class))

(defn init []
      (log/info "{{name}} is starting"))

(defn destroy []
      (log/info "{{name}} is shutting down"))
