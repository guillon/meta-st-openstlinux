SUMMARY = "OPTEE Client"
DESCRIPTION = "OPTEE Client"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=f0fb2f357d31d6a98213b19f57abf927"
PR="r0"
PV="1.0+git"

DEPENDS = "optee-linuxdriver"

PACKAGE_ARCH = "${MACHINE_ARCH}"

#inherit autotools pkgconfig

SRC_URI = "git://github.com/OP-TEE/optee_client.git"
SRC_URI += " file://0001-OE-adapt-build-process.patch "

S = "${WORKDIR}/git"

SRCREV = "1ebcb5af14f809bf6f8e68fcca27352071ea8e2d"

do_compile() {
    mkdir -p ${D}/usr
    oe_runmake EXPORT_DIR=${D}/usr/
}
do_install() {
    mkdir -p ${D}/usr
    oe_runmake install EXPORT_DIR=${D}/usr

    cd ${D}/usr/lib
    rm libteec.so libteec.so.1
    ln -s libteec.so.1.0 libteec.so.1
    ln -s libteec.so.1.0 libteec.so
    cd -
}

