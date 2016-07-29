(ns ocean.core
  (:require [clojure.string :as str])
  (:gen-class))

(def options {:timeout 200})
(def ip_f_s (str/trim (slurp "/Users/gavin/goagent/local/ip_list.txt")))
(def ip_s_s (str/split ip_f_s #"\|"))
(def p_ips (first (split-at 20 (partition 50 ip_s_s))))
(prn "========" (count p_ips) "========")


(defn -main [& args])
