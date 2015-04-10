#!/usr/bin/gnuplot
reset
set terminal png
set datafile separator ","
set title "Obst optimize"
set xlabel "Aantal elementen n"
set ylabel "Tijd in ms"
set autoscale
set key below
set grid

plot "obst2_optimize.csv" using 1:2 title "Optimize Obst2" w lp, \
     "obst2_optimize.csv" using 1:3 title "n*n/5000" w lp
