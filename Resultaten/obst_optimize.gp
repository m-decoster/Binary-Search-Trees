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

plot "obst_optimize.csv" using 1:3 title "Optimize Obst1" w lp, \
     "obst_optimize.csv" using 1:2 title "Optimize Obst2" w lp
