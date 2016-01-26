SUMMARY = "OPTEE Client"
DESCRIPTION = "OPTEE Client"

LICENSE = "CLOSED"
#LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=f0fb2f357d31d6a98213b19f57abf927"
PR="r0"
PV="1.0+git"

DEPENDS = "optee-os optee-client"

PACKAGE_ARCH = "${MACHINE_ARCH}"


SRC_URI = "git://github.com/jenswi-linaro/lcu14_optee_hello_world.git"
SRCREV = "8e95dfe8c3f77cbdbd19c5d06d5898c9f11ec34d"

#patch
SRC_URI += "file://0001-OE-remove-the-compile-choice.patch"

S = "${WORKDIR}/git"
B = "${S}"

#stub
do_configure[noexec] = "1"

EXTRA_OEMAKE = ""
do_compile() {
    export TA_DEV_KIT_DIR=${STAGING_INCDIR}/optee/export-user_ta
    export TEEC_EXPORT=${STAGING_DIR_HOST}/usr

    # compile linux part, here helloword
    oe_runmake -C ${S}/host LDFLAGS=""

    #compile trusted part, here TA
    oe_runmake -C ${S}/ta LDFLAGS=""
}
do_install() {
    mkdir -p ${D}/usr/bin
    mkdir -p ${D}/lib/teetz

    #linux binary
    install -m 544 ${B}/host/hello_world ${D}/usr/bin/optee_hello_work

    #ta part
    install -m 444 ${B}/ta/8aaaf200-2450-11e4-abe20002a5d5c51b.ta ${D}/lib/teetz/
}

#extended helloword
# SRC_URI += " file://0001-Add-support-of-extended-hello-word.patch "
# do_compile_append() {
#     export TA_DEV_KIT_DIR=${STAGING_INCDIR}/optee/export-user_ta
#     export TEEC_EXPORT=${STAGING_DIR_HOST}/usr

#     # compile linux part, here helloword extended
#     oe_runmake -C ${S}/host_ext LDFLAGS=""

#     #compile trusted part, here TA
#     oe_runmake -C ${S}/ta/hellota_ext_ta LDFLAGS="" 
#     oe_runmake -C ${S}/ta/helloworld_ext_ta LDFLAGS=""
# }

# do_install_append() {
#     mkdir -p ${D}/usr/bin
#     mkdir -p ${D}/lib/teetz

#     #linux binary
#     install -m 544 ${B}/host_ext/hello_world_ext ${D}/usr/bin/optee_hello_work_ext

#     #ta part
#     install -m 444 ${B}/ta/hellota_ext_ta/1b990126-6c04-11e4-8ad5101f74f39d1d.ta ${D}/lib/teetz/
#     install -m 444 ${B}/ta/helloworld_ext_ta/1ca38dfc-6c04-11e4-9dbb101f74f39d1d.ta ${D}/lib/teetz/
# }



FILES_${PN} = "/usr/bin/ /lib/teetz/"
INSANE_SKIP_${PN}-dev = "staticdev"

INHIBIT_PACKAGE_STRIP = "1"
