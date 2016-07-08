include optee-os.inc

SRCREV = "c5bbfb4de5de0f0e82939e7c51b1f99586a9f212"

PV = "1.1.0"
PR = "git${SRCPV}.r0"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}/${PV}:"

SRC_URI += "file://optee-os-1.1.0-fix-mmu_v7-mapping.patch"
