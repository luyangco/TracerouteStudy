#!/bin/bash


while read p; do
echo $p
ssh -i ~/.ssh/id_rsa wpi_routing_behavior@$p './trace.sh' < /dev/null &
ssh -i ~/.ssh/id_rsa wpi_routing_behavior@$p './trace_2.sh' < /dev/null &
done < $1
wait
echo 'done!'
