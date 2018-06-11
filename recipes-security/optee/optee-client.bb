SUMMARY = "OPTEE Client"
DESCRIPTION = "OPTEE Linux Userspace Client component: libteec and tee-supplicant"

LIC_FILES_CHKSUM = "file://LICENSE;md5=69663ab153298557a59c67a60a743e5b"
LICENSE = "BSD"

COMPATIBLE_MACHINE = "(stm32mpcommon)"

PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = "git://github.com/OP-TEE/optee_client.git"
SRCREV = "2d542f2074223fde918e68efa4a9ff37f927e604"

PV = "3.1.0+"
PR = "git${SRCPV}.r0"

S = "${WORKDIR}/git"

EXTRA_OEMAKE = 'CROSS_COMPILE=${TARGET_PREFIX}'
EXTRA_OEMAKE += 'CC="${TARGET_PREFIX}gcc --sysroot=${STAGING_DIR_TARGET}"'
EXTRA_OEMAKE += 'CFG_TEE_CLIENT_LOAD_PATH="${libdir}"'

do_compile() {
    mkdir -p ${D}${exec_prefix}
    oe_runmake -C ${S} EXPORT_DIR=${D}${exec_prefix}
}

FILES_${PN} = "${libdir}/libteec.so*"
FILES_${PN} += "${bindir}/tee-supplicant"

do_install() {
    install -d ${D}${exec_prefix}
    oe_runmake -C ${S} install EXPORT_DIR=${D}${exec_prefix}

    cd ${D}/${libdir}
    rm libteec.so libteec.so.1
    ln -s libteec.so.1.0 libteec.so.1
    ln -s libteec.so.1.0 libteec.so
    cd -
}

do_clean() {
    oe_runmake -C ${S} EXPORT_DIR=${D}${exec_prefix} clean
}
