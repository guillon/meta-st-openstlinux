include optee-test.inc

SRCREV = "b28d0dadd467b7bcf13cb89deaec0d7ea122480b"

PV = "1.1.0"
PR = "git${SRCPV}.r0"

SRC_URI += "file://workaround-minor-gcc-warning.patch"
