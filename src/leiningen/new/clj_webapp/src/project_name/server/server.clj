(ns {{sanitized-ns}}.server.server
  (:require [ring.adapter.jetty :as jetty]
            [{{sanitized-ns}}.server.routes :as routes]
            [clojure.tools.logging :as log]
            [{{sanitized-ns}}.server.config :as config]
              )
  (:gen-class))

(defn init []
  (config/init)
  (log/info "{{name}} is starting with config" @config/config))

(defn destroy []
  (log/info "{{name}} is shutting down"))
