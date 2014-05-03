#!/bin/sh

rm *.m
# ./runServer.sh
ATTEMPTS=5

echo "MarioAI Benchmark: Starting Native Java Agents..."
java -jar iMario.jar -m iMario1 -ag ForwardAgent -vlx 0 -vly 0 -an ${ATTEMPTS} -ld 5 -pw on -echo on -gv off -tc off -vis on -maxFPS on -ewf on -vaot on &
java -jar iMario.jar -m iMario2 -ag ForwardJumpingAgent -vlx 330 -vly 0 -an ${ATTEMPTS} -ld 9 -pw on -echo on -gv off -tc off -vis on -maxFPS on -ewf on -vaot on &
java -jar iMario.jar -m iMario3 -ag RandomAgent -vlx 660 -vly 0 -an ${ATTEMPTS} -ld 5 -pw on -echo on -gv off -tc off -vis on -maxFPS on -ewf on -vaot on &


I=1
LIM=3
PORT=4242
VLX=0
VLY=320

echo "iMarioFramework: Starting ServerAgents..."
# java -cp .:../../../trunk/lib/jdom.jar ch.idsia.scenarios.oldscenarios.MainRun -ag ServerAgent -vis on -ld 1 -pw on -maxFPS off -an 1 -vlx 400  -vaot on -ewf off
while [ $I -le $LIM ]
do
    # echo $I $PORT $VLX $VLY	$(($I+3))
    java -jar iMario.jar -m iMario$(( $I + 3 )) -ag ServerAgent:${PORT} -vlx ${VLX} -vly ${VLY} -an ${ATTEMPTS} -ld 5 -pw on -echo on -gv off -tc off -vis on -maxFPS on -ewf on -vaot on &
    VLX=$(( $VLX + 330 ))
    PORT=$(( $PORT + 1 ))
    I=$(( $I + 1 ))
done

echo "iMarioFramework: Wait until all servers are ready..."
sleep 5 #Wait until all servers are ready. You may adjust this paramer.

echo "iMarioFramework: Starting Clients..."
./runClient.sh

echo "iMarioFramework: Wait until all evaluations complete..."

jobs -lp
wait

echo "iMarioFramework: All evaluations have been completed. Gather data..."
sleep 1
./catResults.sh

echo "iMarioFramework: All data have been gathered..."
sleep 1

echo "iMarioFramework: producing LaTeX output..."
matlab -r "func_opts.format = 'latex'; func_opts.imageFormat = 'png'; func_opts.outputDir = 'latex'; publish ('iMarioResult.m', func_opts); exit" -nodesktop -nosplash > /dev/null
echo "iMarioFramework: LaTeX output saved to latex/iMarioResult.tex"
cd latex
echo "iMarioFramework: producing pdf..."
pdflatex iMarioResult.tex
echo "iMarioFramework: pdf successfully created!"
echo "iMarioFramework: opening iMarioResult.pdf"
open iMarioResult.pdf &
echo "iMarioFramework: Experiments has been successfully evaluated"
