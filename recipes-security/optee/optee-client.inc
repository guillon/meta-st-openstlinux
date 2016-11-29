SUMMARY = "OPTEE Client"
DESCRIPTION = "OPTEE Client"

# license hash since optee-v2.2.0
LIC_FILES_CHKSUM ?= "file://LICENSE;md5=a6d62e1b5fef18a1854bd538e3160d7c"
LICENSE = "BSD"

SRC_URI = "git://github.com/OP-TEE/optee_client.git"

S = "${WORKDIR}/git"

DEPENDS = "optee-linuxdriver"

PACKAGE_ARCH = "${MACHINE_ARCH}"

do_install() {
    install -d ${D}${exec_prefix}
    oe_runmake install EXPORT_DIR=${D}${exec_prefix}

    cd ${D}${libdir}
    rm libteec.so libteec.so.1
    ln -s libteec.so.1.0 libteec.so.1
    ln -s libteec.so.1.0 libteec.so
    cd -
}

