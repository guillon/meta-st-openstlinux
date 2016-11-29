include optee-linuxdriver.inc

SRCREV = "5fcce5d5800a60957141f1d963edfd199480bfcb"

PV = "2.2.0"
PR = "git${SRCPV}.r0"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}/${PV}:"

SRC_URI += "file://0001-remove-driver-1.1.0-source-tree.patch"
SRC_URI += "file://0002-PATCH-v13-1.patch"
SRC_URI += "file://0003-PATCH-v13-2.patch"
SRC_URI += "file://0004-PATCH-v13-3.patch"
SRC_URI += "file://0005-PATCH-v13-4.patch"
SRC_URI += "file://0006-PATCH-v13-5.patch"
SRC_URI += "file://0007-out-of-tree-build.patch"
SRC_URI += "file://0008-tee-kernel-api.patch"
SRC_URI += "file://0009-fix-smcc.patch"
SRC_URI += "file://0010-register-shm-fd.patch"

EXTRA_OEMAKE += "CONFIG_TEE=m CONFIG_OPTEE=m"
