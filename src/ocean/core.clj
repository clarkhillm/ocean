(ns ocean.core
  (:require [org.httpkit.client :as http])
  (:require [clojure.string :as str])
  (:gen-class))

(def options {:timeout 200})
(def ip_f_s (str/trim (slurp "/Users/gavin/goagent/local/ip_list.txt")))
(def ip_s_s (str/split ip_f_s #"\|"))
;(println @(http/get "http://www.baidu.com"))
(def p_ips (partition 50 ip_s_s))
(prn "========" (count p_ips) "========")

(defn test_ip [ip] (let [{:keys [opts]} @(http/get (str "http://" ip) options)] (println opts)))
(defn test_main [] (loop [a ip_s_s b 0] (if (= b 10) nil (recur (do (test_ip (first a)) (rest a)) (inc b)))))

(defn generate_promise [ips] (map (fn [x] {:p (http/get (str "http://" x) options),:ip x}) ips))
(defn get_result [pms] (map (fn [x] (let [{:keys [opts error]} @(:p x)] (if error "-" (:ip x)))) pms))
(defn test_main3 [ips] (map (fn [x] (get_result (generate_promise x))) ips))
(def result (loop [x (test_main3 p_ips) b []] (if (nil? (first x)) b (recur (rest x) (conj b (future (first x)))))))
(loop [x result] (if (nil? (first x)) nil (recur (do (prn @(first x)) (rest x)))))
(defn -main [& args])
