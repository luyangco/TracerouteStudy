#!/bin/bash


while read p; do
echo $p
ssh -i ~/.ssh/id_rsa wpi_routing_behavior@$p ps -aew < /dev/null
done < $1
