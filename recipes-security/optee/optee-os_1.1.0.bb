SUMMARY = "OPTEE OS"
DESCRIPTION = "OPTEE OS"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=69663ab153298557a59c67a60a743e5b"

inherit deploy

PR="r0"
PV="1.1.0"

SRC_URI = "git://github.com/OP-TEE/optee_os.git"
SRCREV = "c5bbfb4de5de0f0e82939e7c51b1f99586a9f212"

S = "${WORKDIR}/git"

DEPENDS = "virtual/kernel"

PACKAGE_ARCH = "${MACHINE_ARCH}"

OPTEE_PLATFORM ?= "vexpress"

EXTRA_OEMAKE = "CROSS_COMPILE=${TARGET_PREFIX}"
EXTRA_OEMAKE += "ARCH=${TARGET_ARCH}"
EXTRA_OEMAKE += "comp-cflagscore='--sysroot=${STAGING_DIR_TARGET}'"
EXTRA_OEMAKE += "CFLAGS='-std=gnu99'"


is_armv7 = "${@bb.utils.contains("TUNE_FEATURES", "armv7a", "1", "0", d)}"
is_armv8 = "${@bb.utils.contains("TUNE_FEATURES", "aarch64", "1", "0", d)}"

do_compile() {
    unset LDFLAGS
    oe_runmake PREFIX=${STAGING_DIR_HOST}
}

do_install() {
    #install core on boot directory
    install -d ${D}${base_libdir}/firmware/
    if [ "${is_armv7}" = "1" ]; then
        install -m 644 ${B}/out/${TARGET_ARCH}-plat-${OPTEE_PLATFORM}/core/tee.bin ${D}${base_libdir}/firmware/tee-armv7.bin
        install -m 644 ${B}/out/${TARGET_ARCH}-plat-${OPTEE_PLATFORM}/core/tee-loader.bin ${D}${base_libdir}/firmware/tee-loader-armv7.bin
    fi

    #install TA devkit
    install -d ${D}${includedir}/optee
    if [ "${is_armv7}" = "1" ]; then
	    cp -aR ${B}/out/${TARGET_ARCH}-plat-${OPTEE_PLATFORM}/export-ta_arm* ${D}${includedir}/optee
    fi
}

do_deploy() {
    long_srvrev=${SRCREV}
    short_srvrev=$(echo $long_srvrev | awk  '{ string=substr($0, 1, 8); print string; }')
    OPTEE_CORE_SUFFIX="${MACHINE}-$short_srvrev-${DATETIME}"
    install -d ${DEPLOYDIR}/optee
    if [ "${is_armv7}" = "1" ]; then
        for f in ${D}${base_libdir}/firmware/*; do
            filename=$(basename "$f")
            extension="${filename##*.}"
            sfilename="${filename%.*}"
            bbnote "Deploy $sfilename-${OPTEE_CORE_SUFFIX}.$extension"
            install -m 644 $f ${DEPLOYDIR}/optee/$sfilename-${OPTEE_CORE_SUFFIX}.$extension
        done
    fi
}
addtask deploy before do_build after do_install

FILES_${PN} += "${base_libdir}/firmware/"

INSANE_SKIP_${PN}-dev = "staticdev"
INHIBIT_PACKAGE_STRIP = "1"
