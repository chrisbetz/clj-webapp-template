(ns {{sanitized-ns}}.server
  (:require [ring.adapter.jetty :as jetty]
            [{{sanitized-ns}}.routes :as routes]
            [clojure.tools.logging :as log]
            [clojure.java.io :as io]
            [{{sanitized-ns}}.config :as config]
              )
  (:gen-class))

(defn init []
      (log/info "{{name}} is starting with config" (config/read-config (io/resource "config.edn"))))

(defn destroy []
      (log/info "{{name}} is shutting down"))
