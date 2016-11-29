SUMMARY = "OPTEE OS"
DESCRIPTION = "OPTEE OS"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=69663ab153298557a59c67a60a743e5b"

inherit deploy

SRC_URI = "git://github.com/OP-TEE/optee_os.git"

S = "${WORKDIR}/git"

DEPENDS = "virtual/kernel"

PACKAGE_ARCH = "${MACHINE_ARCH}"

EXTRA_OEMAKE = "CROSS_COMPILE=${TARGET_PREFIX}"
EXTRA_OEMAKE += "ARCH=${TARGET_ARCH}"
EXTRA_OEMAKE += "comp-cflagscore='--sysroot=${STAGING_DIR_TARGET}'"
EXTRA_OEMAKE += "CFLAGS='-std=gnu99'"

do_compile() {
    unset LDFLAGS
    oe_runmake PREFIX=${STAGING_DIR_HOST}
}

do_install() {
	# intallation deferred to board specific
}

do_deploy() {
	# deployment deferred to board specific
}
addtask deploy before do_build after do_install

INSANE_SKIP_${PN}-dev = "staticdev"
INHIBIT_PACKAGE_STRIP = "1"
