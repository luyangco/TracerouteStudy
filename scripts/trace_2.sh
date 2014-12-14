#!/bin/bash

while true; do
while read p; do
today=`date +%Y-%m-%d.%H-%M-%S`
host=`hostname`
traceroute $p &> trace2---$host---$p---$today.log
done < hosts.txt
sleep 66h
done
