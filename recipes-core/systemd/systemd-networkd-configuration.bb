# Copyright (C) 2017 Christophe Priouzeau <christophe.priouzeau@st.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Basic networkd configuration"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
DEPENDS = "systemd"

PR = "r0"

SRC_URI = " \
    file://50-wired.network \
    file://51-wireless.network.sample \
    "

do_install() {
    install -d ${D}${systemd_unitdir}/network
    install -m 644 ${WORKDIR}/50-wired.network ${D}${systemd_unitdir}/network
    install -m 644 ${WORKDIR}/51-wireless.network.sample ${D}${systemd_unitdir}/network
}

FILES_${PN} = "${systemd_unitdir}/network"
