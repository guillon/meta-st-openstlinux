include optee-os.inc

SRCREV = "a0cd5d60beafc282c9016a095b930dc4750f0fb0"

PV = "2.0.0"
PR = "git${SRCPV}.r0"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}/${PV}:"

SRC_URI += "file://optee-os-2.0.0-fix-mmu_v7-mapping.patch"
