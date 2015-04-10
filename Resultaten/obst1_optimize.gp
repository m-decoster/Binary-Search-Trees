#!/usr/bin/gnuplot
reset
set terminal png
set datafile separator ","
set title "Obst1 optimize"
set xlabel "Aantal elementen n"
set ylabel "Tijd in ms"
set autoscale
set key below
set grid

plot "obst1_optimize.csv" using 1:2 title "Optimize" w lp, \
     "obst1_optimize.csv" using 1:3 title "n*n*n/100.000" w lp
