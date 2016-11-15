#!/bin/sh

if [ -e /sys/class/remoteproc/remoteproc0/state ]; then
    if [ -z $(grep running /sys/class/remoteproc/remoteproc0/state)]; then
        echo start > /sys/class/remoteproc/remoteproc0/state
    fi
else
    echo start > /sys/class/remoteproc/remoteproc0/state
fi
