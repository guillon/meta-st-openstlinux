SUMMARY = "OPTEE SDP trusted application"
DESCRIPTION = "OPTEE SDP trusted application"

LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://host/smaf-optee.c;beginline=1;endline=7;md5=f065d06d933e001ca619e02b182e6d9d "

inherit module

PR="r0"
PV="1.1.0"

SRC_URI = "git://git.linaro.org/people/benjamin.gaignard/optee-sdp.git"
SRCREV = "a12758b7bda2dd28860c6029c0ffa58fcca85975"

S = "${WORKDIR}/git"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}/${PV}:"

SRC_URI += "file://sync-ta-makefile-with-optee-1.1.0.patch"

DEPENDS = "optee-client optee-os"

do_configure[noexec] = "1"
do_make_scripts[noexec] = "1"

# refer to optee-os for TA devkit installation path
EXTRA_OEMAKE = "TA_DEV_KIT_DIR=${STAGING_INCDIR}/optee/export-ta_arm32"

# TA devkit requires a CROSS_COMPILE
EXTRA_OEMAKE_TA = "CROSS_COMPILE=${TARGET_PREFIX}"

EXTRA_SMAF_OPTEE_KCMDLINE = "O=${STAGING_KERNEL_BUILDDIR} M=${S}/host"

do_compile() {
	#trusted application
	unset LDFLAGS
	oe_runmake -C ${S}/ta ${EXTRA_OEMAKE_TA}

	#linux driver part
	oe_runmake -C ${STAGING_KERNEL_DIR} modules ${EXTRA_SMAF_OPTEE_KCMDLINE}
}

do_install() {
	#trusted application
	install -d ${D}${base_libdir}/optee_armtz
	install -m 444 ${B}/ta/b9aa5f00-d229-11e4-925c0002a5d5c51b.ta ${D}${base_libdir}/optee_armtz

	#linux driver driver
	install -d ${D}${base_libdir}/modules/${KERNEL_VERSION}
	install -m 0755 ${S}/host/smaf-optee.ko ${D}${base_libdir}/modules/${KERNEL_VERSION}/smaf-optee.ko
}

FILES_${PN} = "${base_libdir}/optee_armtz/"
INSANE_SKIP_${PN}-dev = "staticdev"

INHIBIT_PACKAGE_STRIP = "1"
