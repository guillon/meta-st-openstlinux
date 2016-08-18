# Copyright (C) 2014 Christophe Priouzeau <christophe.priouzeau@st.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Basic splash screen which display a picture on DRM/KMS"
LICENSE = "MIT"
DEPENDS = "libdrm"
PR = "r0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
        file://image_header.h \
        file://basic_splash_drm.c \
        file://Makefile \
    "

SRC_URI += " file://psplash-drm-start.service "

inherit systemd

SYSTEMD_PACKAGES = "${@base_contains('DISTRO_FEATURES','systemd','${PN}','',d)}"
SYSTEMD_SERVICE_${PN} = "${@base_contains('DISTRO_FEATURES','systemd','psplash-drm-start.service','',d)}"

S = "${WORKDIR}"

do_configure[noexec] = "1"

do_compile() {
    oe_runmake clean
    oe_runmake psplash
}
do_install() {
    install -d ${D}/usr/bin/
    install -m 755 ${WORKDIR}/psplash-drm ${D}/usr/bin

    if ${@base_contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -d ${D}${systemd_unitdir}/system
        install -m 644 ${WORKDIR}/*.service ${D}/${systemd_unitdir}/system
    fi
}
#plymouth: scritp which send to fofo command like QUIT

