include optee-linuxdriver.inc

SRCREV = "5fcce5d5800a60957141f1d963edfd199480bfcb"

PV = "2.0.0"
PR = "git${SRCPV}.r0"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}/${PV}:"

SRC_URI += "file://0001-remove-driver-1.1.0-source-tree.patch"
SRC_URI += "file://0002-jens-PATCH-v9-1.patch"
SRC_URI += "file://0003-jens-PATCH-v9-2.patch"
SRC_URI += "file://0004-jens-PATCH-v9-3.patch"
SRC_URI += "file://0005-jens-PATCH-v9-4.patch"
SRC_URI += "file://0006-build-jens-PATCH-v9-on_STI-4.1.patch"

EXTRA_OEMAKE += "CONFIG_TEE=m CONFIG_OPTEE=m"
