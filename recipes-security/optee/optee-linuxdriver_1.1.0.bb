SUMMARY = "OPTEE linux driver"
DESCRIPTION = "OPTEE linux driver"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263"

inherit module

PR="r0"
PV="1.1.0"

SRC_URI = "git://github.com/OP-TEE/optee_linuxdriver.git"
SRCREV = "5fcce5d5800a60957141f1d963edfd199480bfcb"

S = "${WORKDIR}/git"

SRC_URI += "file://tee-fmw-runtime-load.patch"

EXTRA_OEMAKE = "-C ${STAGING_KERNEL_BUILDDIR} M=${S}"

do_install_append () {
    # Installs kernel module header files in the 'OP-TEE Trusted Application
    # development kit', installed by "optee-os" component.
    install -d ${D}${includedir}/optee/export-ta_arm32/linux/
    install ${S}/include/linux/*.h ${D}${includedir}/optee/export-ta_arm32/linux/
}
