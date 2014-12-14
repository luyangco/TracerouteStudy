#!/bin/bash

while true; do
while read p; do
today=`date +%Y-%m-%d.%H-%M-%S`
host=`hostname`
traceroute $p &> trace1---$host---$p---$today.log
done < hosts.txt
sleep 2h
done

