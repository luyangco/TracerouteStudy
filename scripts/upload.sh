#!/bin/bash


while read p; do
echo $p
scp hosts.txt wpi_routing_behavior@$p:~
done < $1
