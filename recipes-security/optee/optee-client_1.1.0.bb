SUMMARY = "OPTEE Client"
DESCRIPTION = "OPTEE Client"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=69663ab153298557a59c67a60a743e5b"

PR="r0"
PV="1.1.0"

SRC_URI = "git://github.com/OP-TEE/optee_client.git"
SRCREV = "0b0d237779de6982451f6f5971b7106e459a2958"

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

