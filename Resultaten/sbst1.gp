#!/usr/bin/gnuplot
reset
set terminal png
set datafile separator ","
set title "Sbst1"
set xlabel "Aantal elementen n"
set ylabel "Tijd in ms"
set autoscale
set key below
set grid

plot "sbst1.csv" using 1:2 title "Add" w lp, \
     "sbst1.csv" using 1:3 title "Contains" w lp, \
     "sbst1.csv" using 1:4 title "n*log(n)/10000" w lp
