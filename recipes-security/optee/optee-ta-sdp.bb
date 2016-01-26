SUMMARY = "OPTEE SDP trusted application"
DESCRIPTION = "OPTEE SDP trusted application"

LICENSE = "CLOSED"
PR="r0"
PV="1.0+git"

inherit module

DEPENDS = "optee-os optee-client virtual/kernel"

PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = "git://git.linaro.org/people/benjamin.gaignard/optee-sdp.git"
S = "${WORKDIR}/git"

SRCREV = "2a981c229030a9cc46487ba2c75aa48a5d6413de"

#stub
do_configure[noexec] = "1"
do_make_scripts[noexec] = "1"

EXTRA_OEMAKE = ""
do_compile() {
    export TA_DEV_KIT_DIR=${STAGING_INCDIR}/optee/export-user_ta
    export TEEC_EXPORT=${STAGING_DIR_HOST}/usr

    #compile trusted part
    oe_runmake -C ${S}/ta LDFLAGS=""

	#compile host driver part
	KDIR=${STAGING_KERNEL_DIR} make -C ${S}/host
}

do_install() {
	mkdir -p ${D}/lib/teetz

    #ta part
    install -m 444 ${B}/ta/b9aa5f00-d229-11e4-925c0002a5d5c51b.ta ${D}/lib/teetz/

	install -d ${D}/lib/modules/${KERNEL_VERSION}
	install -m 0755 ${S}/host/smaf-optee.ko ${D}/lib/modules/${KERNEL_VERSION}/smaf-optee.ko
}

FILES_${PN} = "/lib/teetz/"
INSANE_SKIP_${PN}-dev = "staticdev"

INHIBIT_PACKAGE_STRIP = "1"
