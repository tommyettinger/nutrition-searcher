(ns nutrition-searcher.customizer
	(:require [clojure.string :as st :only [join split upper-case replace]])
	(:use seesaw.core
	      seesaw.font)
	(:import (java.io File FileWriter))
  (:gen-class))

(native!)

(def f (frame :title "Nutrition!" :on-close :exit :size [800 :by 600]))

(defn display [content]
  (config! f :content content)
  content)
(defn acquire [kw] (select (to-root f) kw))

(def columns-measures
[
[:NDB_No "5-digit Nutrient Databank number."]
[:Shrt_Desc "60-character abbreviated description of food item."]
[:Water "Water (g/100 g)"]
[:Energ_Kcal "Food energy (kcal/100 g)"]
[:Protein "Protein (g/100 g)"]
[:Lipid_Tot "Total lipid (fat) (g/100 g)"]
[:Ash "Ash (g/100 g)"]
[:Carbohydrt "Carbohydrate, by difference (g/100 g)"]
[:Fiber_TD "Total dietary fiber (g/100 g)"]
[:Sugar_Tot "Total sugars (g/100 g)"]
[:Calcium "Calcium (mg/100 g)"]
[:Iron "Iron (mg/100 g)"]
[:Magnesium "Magnesium (mg/100 g)"]
[:Phosphorus "Phosphorus (mg/100 g)"]
[:Potassium "Potassium (mg/100 g)"]
[:Sodium "Sodium (mg/100 g)"]
[:Zinc "Zinc (mg/100 g)"]
[:Copper "Copper (mg/100 g)"]
[:Manganese "Manganese (mg/100 g)"]
[:Selenium "Selenium (μg/100 g)"]
[:Vit_C "Vitamin C (mg/100 g)"]
[:Thiamin "Thiamin (mg/100 g)"]
[:Riboflavin "Riboflavin (mg/100 g)"]
[:Niacin "Niacin (mg/100 g)"]
[:Panto_acid "Pantothenic acid (mg/100 g)"]
[:Vit_B6 "Vitamin B6 (mg/100 g)"]
[:Folate_Tot "Folate, total (μg/100 g)"]
[:Folic_acid "Folic acid (μg/100 g)"]
[:Food_Folate "Food folate (μg/100 g)"]
[:Folate_DFE "Folate (μg dietary folate equivalents/100 g)"]
[:Choline_Tot "Choline, total (mg/100 g)"]
[:Vit_B12 "Vitamin B12 (μg/100 g)"]
[:Vit_A_IU "Vitamin A (IU/100 g)"]
[:Vit_A_RAE "Vitamin A (μg retinol activity equivalents/100g)"]
[:Retinol "Retinol (μg/100 g)"]
[:Alpha_Carot "Alpha-carotene (μg/100 g)"]
[:Beta_Carot "Beta-carotene (μg/100 g)"]
[:Beta_Crypt "Beta-cryptoxanthin (μg/100 g)"]
[:Lycopene "Lycopene (μg/100 g)"]
[:Lut+Zea "Lutein+zeazanthin (μg/100 g)"]
[:Vit_E "Vitamin E (alpha-tocopherol) (mg/100 g)"]
[:Vit_D_mcg "Vitamin D (μg/100 g)"]
[:Vit_D_IU "Vitamin D (IU/100 g)"]
[:Vit_K "Vitamin K (phylloquinone) (μg/100 g)"]
[:FA_Sat "Saturated fatty acid (g/100 g)"]
[:FA_Mono "Monounsaturated fatty acids (g/100 g)"]
[:FA_Poly "Polyunsaturated fatty acids (g/100 g)"]
[:Cholestrl "Cholesterol (mg/100 g)"]
[:GmWt_1 "First household weight for this item from the Weight file."]
[:GmWt_Desc1 "Description of household weight number 1."]
[:GmWt_2 "Second household weight for this item from the Weight file."]
[:GmWt_Desc2 "Description of household weight number 2."]
[:Refuse_Pct "Percent refuse"]
])
(def columns
[
[:NDB_No "5-digit Nutrient Databank number."]
[:Shrt_Desc "60-character abbreviated description of food item."]
[:Water "Water"]
[:Energ_Kcal "Food energy"]
[:Protein "Protein"]
[:Lipid_Tot "Total lipid (fat)"]
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


(defn write-obj [file-name obj]
  (with-open [w (FileWriter. (File. file-name))]
    (binding [*out* w *print-dup* true] (prn obj))))


(defn prime
  []
    (let [
                mdl (cons "Select a nutrient to consider restricted" (map #(nth % 1) (subvec columns-measures 2 48)))
  	  	corefont (font "VERDANA-PLAIN-20")]
  	  	(display (vertical-panel :items [
  	  			(horizontal-panel :items [(combobox :id :nutri-1 :model mdl) (label "Maximum allowed amount: ") (spinner :id :amt-1 :model (spinner-model 1 :from 1 :to 50000 :by 1))])
  	  			(horizontal-panel :items [(combobox :id :nutri-2 :model mdl) (label "Maximum allowed amount: ") (spinner :id :amt-2 :model (spinner-model 1 :from 1 :to 50000 :by 1))])
  	  			(horizontal-panel :items [(combobox :id :nutri-3 :model mdl) (label "Maximum allowed amount: ") (spinner :id :amt-3 :model (spinner-model 1 :from 1 :to 50000 :by 1))])
  	  			(horizontal-panel :items [(combobox :id :nutri-4 :model mdl) (label "Maximum allowed amount: ") (spinner :id :amt-4 :model (spinner-model 1 :from 1 :to 50000 :by 1))])
  	  			(horizontal-panel :items [(combobox :id :nutri-5 :model mdl) (label "Maximum allowed amount: ") (spinner :id :amt-5 :model (spinner-model 1 :from 1 :to 50000 :by 1))])
  	  			(button :id :search-button :text "Store Custom Nutrition Info" :font corefont
  	  			        :listen [:action (fn [e] (write-obj "MyNutrition.dat" (filter #(not= % nil) [
  	  			        		(if (not= 0 (.getSelectedIndex (acquire [:#nutri-1]))) [ (nth columns (+ 1 (.getSelectedIndex (acquire [:#nutri-1])))) (selection (acquire [:#amt-1]))])
  	  			        		(if (not= 0 (.getSelectedIndex (acquire [:#nutri-2]))) [ (nth columns (+ 1 (.getSelectedIndex (acquire [:#nutri-2])))) (selection (acquire [:#amt-2]))])
  	  			        		(if (not= 0 (.getSelectedIndex (acquire [:#nutri-3]))) [ (nth columns (+ 1 (.getSelectedIndex (acquire [:#nutri-3])))) (selection (acquire [:#amt-3]))])
  	  			        		(if (not= 0 (.getSelectedIndex (acquire [:#nutri-4]))) [ (nth columns (+ 1 (.getSelectedIndex (acquire [:#nutri-4])))) (selection (acquire [:#amt-4]))])
  	  			        		(if (not= 0 (.getSelectedIndex (acquire [:#nutri-5]))) [ (nth columns (+ 1 (.getSelectedIndex (acquire [:#nutri-5])))) (selection (acquire [:#amt-5]))])
  	  			        		])))])
  	  			]))
  	  				                 
  	  				
  	  	(.setDefaultButton (.getRootPane f) (acquire [:#search-button])) 
  (-> f pack! show!)))

(defn -main [& args]
  (invoke-later    
    (prime)
))
