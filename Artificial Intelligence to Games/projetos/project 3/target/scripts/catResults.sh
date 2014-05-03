#!/bin/sh

I=1
LIM=6
OUT=MarioAIResult.m
while [ $I -le $LIM ]
do
    echo processing iMario${I}.m ...
    cat MarioAI${I}.m >> ${OUT}
    I=$(( $I + 1 ))
done
echo "results saved to ${OUT}" 