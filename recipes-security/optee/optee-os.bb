SUMMARY = "OPTEE OS"
DESCRIPTION = "OPTEE OS"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=f0fb2f357d31d6a98213b19f57abf927"
PR="r0"
PV="1.0+git"

DEPENDS = "optee-linuxdriver"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit deploy

SRC_URI = "git://github.com/OP-TEE/optee_os.git"
SRC_URI += "file://0001-Support-TEE-fmw-being-load-init-after-a-tz_loader-bo.patch"
SRC_URI += "file://0002-OE-adapt-build-process-for-armv7-with-hardfloat.patch"

S = "${WORKDIR}/git"

SRCREV = "0f2293b796b141d15c18db856d63f200e4f02981"

EXTRA_OEMAKE = "CROSS_COMPILE=${CCACHE}${HOST_PREFIX} PLATFORM_FLAVOR=cannes NOWERROR=1"

is_armv7 = "${@bb.utils.contains("TUNE_FEATURES", "armv7a", "1", "0", d)}"
is_armv8 = "${@bb.utils.contains("TUNE_FEATURES", "aarch64", "1", "0", d)}"

do_compile() {
    if [ "${is_armv7}" = "1" ];
    then
        bbwarn "Optee is compiled for ARMV7a"
        cp ${STAGING_KERNEL_DIR}/System.map* ${S}/core/arch/arm32/plat-stm/
    fi
    unset LDFLAGS

    oe_runmake PREFIX=${STAGING_DIR_HOST} CFG_DDR_TEETZ_RESERVED_START=${SECURITY_MEMORY_TEEZ_START} CFG_DDR_TEETZ_RESERVED_SIZE=${SECURITY_MEMORY_TEEZ_SIZE}
}

do_install() {
    bbwarn "Optee installation"
    #install core on boot directory
    install -d ${D}/lib/firmware/

    if [ "${is_armv7}" = "1" ];
    then
        install -m 644 ${B}/out/arm32-plat-stm/core/tee.bin ${D}/lib/firmware/tee-armv7.bin
        install -m 644 ${B}/out/arm32-plat-stm/core/tee-init.bin ${D}/lib/firmware/tee-init-armv7.bin
    fi
    #install TA devkit
    install -d ${D}/usr/include/optee/export-user_ta/
    if [ "${is_armv7}" = "1" ];
    then
        for f in  ${B}/out/arm32-plat-stm/export-user_ta/* ; do
            cp -aR  $f  ${D}/usr/include/optee/export-user_ta/
        done
    fi
}


do_deploy() {
    long_srvrev=${SRCREV}
    short_srvrev=$(echo $long_srvrev | awk  '{ string=substr($0, 1, 8); print string; }')
    OPTEE_CORE_SUFFIX="${MACHINE}-$short_srvrev-${DATETIME}"
    install -d ${DEPLOYDIR}/optee
    if [ "${is_armv7}" = "1" ];
    then
        for f in ${D}/lib/firmware/*; do
            filename=$(basename "$f")
            extension="${filename##*.}"
            sfilename="${filename%.*}"
            bbnote "Deploy $sfilename-${OPTEE_CORE_SUFFIX}.$extension"
            install -m 644 $f ${DEPLOYDIR}/optee/$sfilename-${OPTEE_CORE_SUFFIX}.$extension
        done
    fi
}
addtask deploy before do_build after do_install
FILES_${PN} = "/lib/firmware/"
FILES_${PN}-dev = "/usr/include/optee"
INSANE_SKIP_${PN}-dev = "staticdev"

INHIBIT_PACKAGE_STRIP = "1"
