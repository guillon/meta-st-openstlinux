SUMMARY = "OPTEE core packagegroup"
DESCRIPTION = "Provide optee-os optee-client and optee-linuxdriver packages"
LICENSE = "LGPLv2+"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/LGPL-2.0;md5=9427b8ccf5cf3df47c29110424c9641a"

PR = "r0"

PACKAGE_ARCH = "${TUNE_PKGARCH}"

inherit packagegroup

PACKAGES = "packagegroup-optee-core"

PROVIDES = "${PACKAGES}"

RDEPENDS_packagegroup-optee-core = "\
    optee-linuxdriver \
    optee-client \
"
