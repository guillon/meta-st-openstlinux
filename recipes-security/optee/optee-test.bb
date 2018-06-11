SUMMARY = "OPTEE test"
DESCRIPTION = "OPTEE test (Client and Trusted Applications)"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://Notice.md;md5=445126761b17c6ff8957f3a3dbf7fa54"

SRC_URI = "git://github.com/OP-TEE/optee_test.git"
SRCREV = "3ff36f5d06984b121d9857a552993864ea56ad0c"

PV = "3.1.0+"
PR = "git${SRCPV}.r0"

S = "${WORKDIR}/git"

COMPATIBLE_MACHINE = "(stm32mpcommon)"

DEPENDS = "optee-os"
DEPENDS += "optee-client"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit deploy

# optee-test expects specific toolchains for client Appli and Trusted Appli.
EXTRA_OEMAKE = 'CROSS_COMPILE_HOST=${TARGET_PREFIX}'
EXTRA_OEMAKE += 'CROSS_COMPILE_TA=${TARGET_PREFIX}'
# refer to optee-os for TA devkit installation path
EXTRA_OEMAKE += 'TA_DEV_KIT_DIR=${STAGING_INCDIR}/optee/export-ta_arm32'
EXTRA_OEMAKE += 'COMPILE_NS_USER=32'
EXTRA_OEMAKE += 'OPTEE_CLIENT_EXPORT=${STAGING_EXECPREFIXDIR}'
EXTRA_OEMAKE += 'TA_DIR=${libdir}/optee_armtz'

CFLAGS_append = " --sysroot=${STAGING_DIR_TARGET} "

OUT_DIR = "${B}/out"

do_compile() {
    #unset LDFLAGS
    oe_runmake -C ${S} O=${OUT_DIR}
}

do_install() {
    install -d ${D}/${bindir}
    install -m 544 ${OUT_DIR}/xtest/xtest ${D}/${bindir}

    install -d ${D}/${libdir}/optee_armtz
    for f in `find ${OUT_DIR}/ta -name \*.ta`; do
        install -m 444 "$f" ${D}/${libdir}/optee_armtz
    done
}

do_clean() {
    oe_runmake -C ${S} O=${OUT_DIR} clean
}

FILES_${PN} += "${bindir}"
FILES_${PN} += "${libdir}/optee_armtz"

INSANE_SKIP_${PN}-dev = "staticdev"

INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"
