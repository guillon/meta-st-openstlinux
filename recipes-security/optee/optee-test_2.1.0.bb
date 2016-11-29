include optee-test.inc

SRCREV = "26b8aa8821e22df5606f62a85c20fc177b505a60"

PV = "2.1.0"
PR = "git${SRCPV}.r0"

SRC_URI += "file://workaround-minor-gcc-warning.patch"

