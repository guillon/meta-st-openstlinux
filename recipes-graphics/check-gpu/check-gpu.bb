# Copyright (C) 2018, STMicroelectronics - All Rights Reserved
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "The goal is to detect if the gpu is present/loaded or not and decide if we must configure weston on pix-man or gpu composition"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

PV = "1.0"
PR = "r0"

SRC_URI = " \
        file://README-CHECK-GPU \
    "

SRC_URI_append_stm32mp1 = " \
        file://system-generator-check-gpu \
    "

inherit systemd

do_install() {
    install -d ${D}/home/root/
    install -m 644 ${WORKDIR}/README-CHECK-GPU ${D}/home/root/
}

do_install_append_stm32mp1() {
    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -d ${D}${systemd_unitdir}/system-generators/

        install -m 755 ${WORKDIR}/system-generator-check-gpu ${D}${systemd_unitdir}/system-generators/
    fi
}

FILES_${PN} += " /home/root ${systemd_unitdir}/system-generators"
