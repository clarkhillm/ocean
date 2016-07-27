(ns ocean.core
  (:require [org.httpkit.client :as http])
  (:require [clojure.string :as str])
  (:gen-class))

(def options {:timeout 500})
(def ip_f_s (slurp "/Users/gavin/goagent/local/ip_list.txt"))
(def ip_s_s (str/split ip_f_s #"\|"))
(println @(http/get "http://www.baidu.com"))
(def p_ips (partition 10 ip_s_s))
(println "========" (count p_ips) "========")
(def agents (map (fn [x] (agent "+")) p_ips))
(println (count agents))

(defn test_ip [ip]
  (let [{:keys [opts]}
        @(http/get (str "http://" ip) options)]
    (println opts)))

(defn test_main []
  (loop [a ip_s_s b 0]
    (if (= b 10) nil
      (recur (do (test_ip (first a)) (rest a)) (inc b)))))

(defn test_main1 [ips]
  (loop [rs [] a ips]
    (if (nil? (first a)) rs
      (recur
        (conj rs {:p (http/get (str "http://" (first a)) options),:ip (first a)})
        (rest a)))))

(defn test_main2 [pms]
  (map (fn [x] (let [{:keys [opts error]} @(:p x)] (if error "-" (:ip x)))) pms))
;(test_main)
;(println (test_main1))
;(loop [a (test_main1) ]
;  (if (nil? (first a))
;   (println "finished ..") 
;    (recur (do (println (:opts @(:p (first a)))) (rest a)))))
(defn test_main3 [ips]
  (loop [x ips fs agents b 1]
    (if (nil? (first x))
      fs
      (recur
        (do
          ;(prn "-------------------" b "--------------------")
          ;(prn (first x))
          ;(prn "--------------------------------------------")
          ;(prn (test_main2 (test_main1 (first x))))
          (rest x))
        (do (prn (first x)) (send (first fs) (test_main2 (test_main1 (first x)))) (rest fs))
        (inc b)))))
;(test_main3 p_ips)
;(loop [a agents] (if (nil? (first a)) nil (recur (do (println @(first a)) (rest a)))))

(defn -main [& args])
