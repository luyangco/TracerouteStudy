#!/bin/bash


while read p; do
echo $p
ssh -i ~/.ssh/id_rsa wpi_routing_behavior@$p "pkill -f trace.sh" < /dev/null &
ssh -i ~/.ssh/id_rsa wpi_routing_behavior@$p "pkill -f trace_2.sh" < /dev/null &
ssh -i ~/.ssh/id_rsa wpi_routing_behavior@$p "pkill -f sleep" < /dev/null &
done < hosts.txt
wait
echo 'done!'
