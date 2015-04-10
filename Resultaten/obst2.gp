#!/usr/bin/gnuplot
reset
set terminal png
set datafile separator ","
set title "Obst"
set xlabel "Aantal elementen n"
set ylabel "Tijd in ms"
set autoscale
set key below
set grid

plot "obst2.csv" using 1:2 title "Add" w lp, \
     "obst2.csv" using 1:3 title "Contains" w lp, \
     "obst2.csv" using 1:4 title "n*log(n)/10000" w lp
