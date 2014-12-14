#!/bin/bash


while read p; do
echo $p
mkdir -p output/trace_1/$p/
mkdir -p output/trace_2/$p/
scp wpi_routing_behavior@$p:~/trace1---*.log output/trace_1/$p/
scp wpi_routing_behavior@$p:~/trace2---*.log output/trace_2/$p/
ssh -i ~/.ssh/id_rsa wpi_routing_behavior@$p 'rm *.log' < /dev/null &
done < hosts.txt
