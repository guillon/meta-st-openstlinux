SUMMARY = "OPTEE linux driver"
DESCRIPTION = "OPTEE driver"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263"

PR="r0"
PV="1.0+git"

inherit module

DEPENDS = "virtual/kernel"

SRC_URI = "git://github.com/OP-TEE/optee_linuxdriver.git"
SRC_URI += "file://0001-add-rule-to-compile-the-driver-out-of-kernel-tree.patch"
SRC_URI += "file://0002-Runtime-init-and-load-fmw.patch"

S = "${WORKDIR}/git"

SRCREV = "6c1b321c2eecf324d514a8d51b4a22e3ef5e284d"

do_make_scripts[noexec] = "1"

do_compile() {
	KDIR=${STAGING_KERNEL_DIR} make -C ${S} CFG_TEE_CORE_ADDR=${SECURITY_MEMORY_TEEZ_START}
}

do_install () {
	install -d ${D}/lib/modules/${KERNEL_VERSION}
	install -m 0755 ${S}/optee.ko ${D}/lib/modules/${KERNEL_VERSION}/optee.ko

	install -d ${STAGING_INCDIR}/optee/export-user_ta/linux/
	cp  ${S}/include/linux/*.h ${STAGING_INCDIR}/optee/export-user_ta/linux/
}
