#!/bin/bash


while read p; do
echo $p
ssh -i ~/.ssh/id_rsa wpi_routing_behavior@$p 'rm *.log' < /dev/null &
done < hosts.txt
wait
echo 'Done!'
