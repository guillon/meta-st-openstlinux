include optee-test.inc

SRCREV = "2190cdcc7c4d1f477ef136690f820edf8c19f418"

PV = "2.0.0"
PR = "git${SRCPV}.r0"

SRC_URI += "file://workaround-minor-gcc-warning.patch"

