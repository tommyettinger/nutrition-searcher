# nutrition-searcher

Which foods are suitable for a restricted diet? This application could help.

## Installation

Download from Releases tab.

## Usage

The .jar files are runnable and will open graphical windows. Normally you run
`nutrition-searcher.jar` when you want to look up whether a food is a reasonable
option for a specific restricted diet, or you want to find the best or worst options
for a specific restriction. Some effort has been made to remove entries that are
unreasonable for an adult to eat in any sizable amount, like cooking oil or baby
formula. The portion size is not based on serving, but instead on 100g portions.
There's an option to search by healthiest first, which is default, or alphabetically,
which may be useful if you want to find out what a food's nutrient levels are.

You only need to run `nutrition-customizer.jar` when you want to choose what nutritional
components should be considered when sorting the list of foods; effectively, it only
needs to change if the dietary restrictions change. This is typically necessary before
the first run of `nutrition-searcher.jar`. In this customizer, you can set the maximum
amount of a restricted ingredient to allow in searches; restricted ingredients will also
be used to sort by "healthiest first," where lower amounts are healthier.

## License

Copyright © 2018 Tommy Ettinger

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
