#!/bin/bash

java -jar -Xmx2048m "Project/dist/Project.jar" > "Resultaten/$1.csv"
exitval=$?
cd "Resultaten"
echo "$1 is ready! Exited with code $exitval" | mail -s "DA2" mth.decoster@gmail.com
