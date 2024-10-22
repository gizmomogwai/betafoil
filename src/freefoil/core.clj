(ns freefoil.core
  (:gen-class)
  (:use [scad-clj.model])
  (:use [scad-clj.scad]))

(defn m5
  "m5 screw with conic head and LENGTH"
  [length]
  (union
   (translate [0 0 (- length 12)]
              (cylinder [0 6] 12 :center false))
   (cylinder 2.5 length :center false)))

(def m5-spacing 25)
(def m5-long-length 50)
(def m5-short-length 20)

(defn tool-bit []
  (rotate [(* Math/PI 1.5) 0 0]
          (cylinder 7 89 :center false)))

(defn tool-handle []
  (rotate [(/ Math/PI 2) 0 (/ Math/PI 2)]
          (union
           (cylinder 6 33 :center false)
           (translate [-41 -16 -30] (cube 82 32 30 :center false)))))

(defn screws []
  (union
   (for [x (range 2)]
     (for [y (range 4)]
       (union
        (->> (m5 m5-long-length)
             (translate [(* x m5-spacing) (* y  m5-spacing) 0]))
        (->> (m5 m5-short-length)
             (translate [(* m5-spacing 0) (* 4 m5-spacing) 30]))
        (->> (m5 m5-short-length)
             (translate [(* m5-spacing 0) (* 5 m5-spacing) 30]))
        (->> (m5 m5-short-length)
             (translate [(* m5-spacing 1) (* 4 m5-spacing) 30])))))))

(def primitives
  (with-fn 64
     (difference
      (union
       (intersection
        (->> (cube 200 200 60 :center false)
             (translate [0 0 -12]))
        (cylinder 170 170)))
    
      (union 
       (->> (screws)
            (translate [20 20 0]))
       (->> (tool-bit)
            (translate [70 15 50]))
       (->> (tool-handle)
            (translate [120 55 50]))
       ))))

(defn -main
  "I don't do a whole lot ... yet."
  [& _args]
  (println "Hello, World!")

  (spit "out/freefoil.scad"
        (write-scad primitives)))
