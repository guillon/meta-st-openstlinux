include optee-os.inc

SRCREV = "c0c5d399d81a0669f5c8e3bcb20039d65649a78d"

PV = "2.2.0"
PR = "git${SRCPV}.r0"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}/${PV}:"

SRC_URI += "file://optee-os-2.2.0-fix-armv7-scr-init.patch"
SRC_URI += "file://optee-os-2.2.0-fix-armv7-spinlock.patch"
