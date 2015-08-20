(ns nutrition-searcher.core
	(:require [clojure.string :as st :only [join split upper-case replace]]
	           [clojure.java.io :as io])
	(:use seesaw.core
	      seesaw.font)
	(:import (java.io File FileReader PushbackReader))
  (:gen-class))

(defn rec-load
	"Load a clojure form from file"
	[#^File file]
	(with-open [r (PushbackReader. (FileReader. file))]
		(let [rec (read r)]
		rec)))

(native!)

(def f (frame :title "Nutrition!" :on-close :exit :size [800 :by 600]))

(defn display [content]
  (config! f :content content)
  content)
(defn acquire [kw] (select (to-root f) kw))

(def columns
[
[:NDB_No "5-digit Nutrient Databank number."]
[:Shrt_Desc "60-character abbreviated description of food item."]
[:Water "Water"]
[:Energ_Kcal "Food energy"]
[:Protein "Protein"]
[:Lipid_Tot "Total lipid"]
[:Ash "Ash"]
[:Carbohydrt "Carbohydrate, by difference"]
[:Fiber_TD "Total dietary fiber"]
[:Sugar_Tot "Total sugars"]
[:Calcium "Calcium"]
[:Iron "Iron"]
[:Magnesium "Magnesium"]
[:Phosphorus "Phosphorus"]
[:Potassium "Potassium"]
[:Sodium "Sodium"]
[:Zinc "Zinc"]
[:Copper "Copper"]
[:Manganese "Manganese"]
[:Selenium "Selenium"]
[:Vit_C "Vitamin C"]
[:Thiamin "Thiamin"]
[:Riboflavin "Riboflavin"]
[:Niacin "Niacin"]
[:Panto_acid "Pantothenic acid"]
[:Vit_B6 "Vitamin B6"]
[:Folate_Tot "Folate, total"]
[:Folic_acid "Folic acid"]
[:Food_Folate "Food folate"]
[:Folate_DFE "Folate"]
[:Choline_Tot "Choline, total"]
[:Vit_B12 "Vitamin B12"]
[:Vit_A_IU "Vitamin A"]
[:Vit_A_RAE "Vitamin A"]
[:Retinol "Retinol"]
[:Alpha_Carot "Alpha-carotene"]
[:Beta_Carot "Beta-carotene"]
[:Beta_Crypt "Beta-cryptoxanthin"]
[:Lycopene "Lycopene"]
[:Lut+Zea "Lutein+zeazanthin"]
[:Vit_E "Vitamin E"]
[:Vit_D_mcg "Vitamin D"]
[:Vit_D_IU "Vitamin D"]
[:Vit_K "Vitamin K"]
[:FA_Sat "Saturated fatty acid"]
[:FA_Mono "Monounsaturated fatty acids"]
[:FA_Poly "Polyunsaturated fatty acids"]
[:Cholestrl "Cholesterol"]
[:GmWt_1 "First household weight for this item from the Weight file."]
[:GmWt_Desc1 "Description of household weight number 1."]
[:GmWt_2 "Second household weight for this item from the Weight file."]
[:GmWt_Desc2 "Description of household weight number 2."]
[:Refuse_Pct "Percent refuse"]
])
(def column-indexes (into {} (for [c (range (count columns))]
			[(first (nth columns c)) c])))
(defn prime
  []
    (let [collected (map #(st/split % #"\^" ) (st/split (slurp (io/file (io/resource "RAW.txt"))) #"\n" ))
    	    loaded (rec-load (File. "MyNutrition.dat"))
  	  	interested (sort-by first (into [] (map #(concat (vector 
  	  	           (apply max (map (fn [ld] (/ (try (read-string (nth % ((nth (nth ld 0) 0) column-indexes))) (catch Exception e (nth ld 1))) (nth ld 1))) loaded))
  	  	  	   
                           (nth % (:Shrt_Desc column-indexes)))
                           (doall (map (fn [ld] (try (read-string (nth % ((nth (nth ld 0) 0) column-indexes))) (catch Exception e "NO DATA"))) loaded)))
                           		collected)))
                           		;(map #(vector (map (fn [ld] (try (read-string (nth % ((nth (nth ld 0) 0) column-indexes))) (catch Exception e (nth ld "NO DATA")))) loaded)) collected)
  	  	  	   
  	  	printable (for [intr interested
  	  		        :when ;(and (> 750 (nth intr 0))
  	  		        	   ;(> 20 (nth intr 5))
  	  		                   (not (re-find #"(DISTILLED)|(INF\w* FOR)|(CH\w*D\w* FOR)|(~FAT)|(~OIL)|(~\w+ OIL)|(~SHORTENING)|(USDA CMDTY)|(BABYFOOD)" (st/upper-case (nth intr 1))))
  	  		                   ]
  	  	              [(str
  	  			      	(nth intr 1)
  	  			                 ":    ")
  	  		      (st/join ", " (map-indexed (fn [idx ld] (str (nth (nth ld 0) 1) " " (nth intr (+ 2 idx)))) loaded))
  	  	              intr])
  	  	corefont (font "VERDANA-PLAIN-20")]
  	  	(display (vertical-panel :items [
  	  			(horizontal-panel :items [(text :editable? false :text "Type the name of a food in\nthe green box to the right." :multi-line? true :font corefont)
  	  				                  (text :id :input-search :size [200 :by 50] :font corefont :background "#CFC")
  	  				                  (button :id :search-button :text "Search, sorted healthiest first" :font corefont
  	  				                  	  :listen [:action (fn [e] (let [surch (st/split (st/upper-case (text (acquire [:#input-search]))) #"\s+|,")]
  	  				                  	  		  (config! (acquire [:#lb]) :model
  	  				                  		              (flatten (for [toshow printable
  	  				                  	        	  	  :when (>= (count (filter true?
  	  				                  		  	  			      (map #(if (not (empty?
  	  				                  		  	  			           (re-find (re-pattern %)
  	  				                  		  	  			           	    (nth (nth toshow 2) 1)))) true false) surch))) (count surch))]        		  	  
                                                                            ;(text :multi-line? true :editable? false :rows 2 :text
  	  				                  		  	[(st/replace
                                                                                (st/replace
                                                                                (st/replace
                                                                                    (first toshow)	                  		  	  
                                                                                #",(\w)" ", $1")
                                                                                #"\&(\w)" " and $1")
                                                                                #"~" "")
                                                                                (nth toshow 1)])))
  	  				                  		  (-> f pack! show!)))])
  	  				                  (button :id :search-button :text "Search, sorted alphabetically" :font corefont
  	  				                  	  :listen [:action (fn [e] (let [surch (st/split (st/upper-case (text (acquire [:#input-search]))) #"\s+|,")]
  	  				                  	  		  (config! (acquire [:#lb]) :model
  	  				                  		              (flatten (for [toshow (sort-by first printable)
  	  				                  	        	  	  :when (>= (count (filter true?
  	  				                  		  	  			      (map #(if (not (empty?
  	  				                  		  	  			           (re-find (re-pattern %)
  	  				                  		  	  			           	    (nth (nth toshow 2) 1)))) true false) surch))) (count surch))]        		  	  
                                                                            ;(text :multi-line? true :editable? false :rows 2 :text
  	  				                  		  	[(st/replace
                                                                                (st/replace
                                                                                (st/replace
                                                                                    (first toshow)
                                                                                #",(\w)" ", $1")
                                                                                #"\&(\w)" " and $1")
                                                                                #"~" "")
                                                                                (nth toshow 1)])))
  	  				                  		  (-> f pack! show!)))])])
  	  		        (scrollable (listbox :id :lb :model [] :font corefont) :size [800 :by 500])
  	  		        (text :multi-line? true :editable? false :text "The numbers for each food are based on a 100 gram edible portion of that food." :font corefont)]))
  	  	(.setDefaultButton (.getRootPane f) (acquire [:#search-button])) 
  (-> f pack! show!)))

(defn -main [& args]
  (invoke-later    
    (prime)
))
