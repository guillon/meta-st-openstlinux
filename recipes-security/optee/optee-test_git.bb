SUMMARY = "OPTEE test"
DESCRIPTION = "OPTEE test (Client and Trusted Applications)"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://Notice.md;md5=445126761b17c6ff8957f3a3dbf7fa54"

SRC_URI = "git://github.com/OP-TEE/optee_test.git"
SRCREV = "a4653559d989d5006f67f4498be4cb090be12f79"

PV = "2.3.0"
PR = "git${SRCPV}.r0"

S = "${WORKDIR}/git"
B = "${S}"

DEPENDS = "optee-os optee-client"

PACKAGE_ARCH = "${MACHINE_ARCH}"

# optee-test expects specific toolchains for client Appli and Trusted Appli.
EXTRA_OEMAKE = "CROSS_COMPILE_HOST=${TARGET_PREFIX}"
EXTRA_OEMAKE += "CROSS_COMPILE_TA=${TARGET_PREFIX}"
# refer to optee-os for TA devkit installation path
EXTRA_OEMAKE += "TA_DEV_KIT_DIR=${STAGING_INCDIR}/optee/export-ta_arm32"
# refer to optee-client for TA devkit installation path
EXTRA_OEMAKE += "OPTEE_CLIENT_EXPORT=${STAGING_EXECPREFIXDIR}"

do_compile() {
    unset LDFLAGS
    oe_runmake
}

do_install() {
    install -d ${D}/${bindir}
    install -m 544 ${B}/out/xtest/xtest ${D}/${bindir}

    install -d ${D}${base_libdir}/optee_armtz
    for f in `find ${B}/out/ta -name \*.ta`; do
        install -m 444 "$f" ${D}${base_libdir}/optee_armtz
    done
}

FILES_${PN} += "${bindir}"
FILES_${PN} += "${base_libdir}/optee_armtz"

INSANE_SKIP_${PN}-dev = "staticdev"

INHIBIT_PACKAGE_STRIP = "1"
