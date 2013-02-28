(ns {{sanitized-ns}}.server.server
  (:require [ring.adapter.jetty :as jetty]
            [{{sanitized-ns}}.server.routes :as routes]
            [clojure.tools.logging :as log]
            [clojure.java.io :as io]
            [{{sanitized-ns}}.server.config :as config]
              )
  (:gen-class))

(defn init []
  (defonce config (ref (config/read-config (io/resource "config.edn"))))
  (log/info "{{name}} is starting with config" @config))

(defn destroy []
  (log/info "{{name}} is shutting down"))
