#!/bin/sh -

KERNEL_VERSION=$(cat /proc/version)

MONTH=$(echo $KERNEL_VERSION | awk '{print $13; }')
DAY=$(echo $KERNEL_VERSION | awk '{print $14; }')
HOUR=$(echo $KERNEL_VERSION | awk '{print $15; }')
YEAR=$(echo $KERNEL_VERSION | awk '{print $17; }')

date -s "$DAY $MONTH $YEAR $HOUR"

