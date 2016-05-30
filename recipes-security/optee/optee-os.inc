SUMMARY = "OPTEE OS"
DESCRIPTION = "OPTEE OS"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=69663ab153298557a59c67a60a743e5b"

inherit deploy

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

do_compile() {
    unset LDFLAGS
    oe_runmake PREFIX=${STAGING_DIR_HOST}
}

do_install() {
    install -d ${D}${includedir}/optee
    install -m 644 ${B}/out/${TARGET_ARCH}-plat-${OPTEE_PLATFORM}/core/tee.bin ${D}/${includedir}/optee
    cp -aR ${B}/out/${TARGET_ARCH}-plat-${OPTEE_PLATFORM}/export-ta_arm* ${D}/${includedir}/optee
}

do_deploy() {
    install -d ${DEPLOYDIR}/boot
    install -m 644 ${B}/out/${TARGET_ARCH}-plat-${OPTEE_PLATFORM}/core/tee.bin ${DEPLOYDIR}/boot/tee-${MACHINE}-${DATETIME}.bin
}
addtask deploy before do_build after do_install

FILES_${PN} += "${includedir}/optee"

INSANE_SKIP_${PN}-dev = "staticdev"
INHIBIT_PACKAGE_STRIP = "1"
