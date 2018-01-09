# Copyright (C) 2018, STMicroelectronics - All Rights Reserved
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Mount Bootfs and userfs"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

RDEPENDS_${PN} += " util-linux "

PR = "r0"

SRC_URI = "file://mount-bootfs-userfs.service file://mount-bootfs-userfs.sh"

inherit systemd update-rc.d

SYSTEMD_PACKAGES = "${@bb.utils.contains('DISTRO_FEATURES','systemd','${PN}','',d)}"
SYSTEMD_SERVICE_${PN} = "mount-bootfs-userfs.service"
SYSTEMD_AUTO_ENABLE_${PN} = "enable"

do_install() {
    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -d ${D}${systemd_unitdir}/system ${D}/${base_sbindir}
        install -m 644 ${WORKDIR}/*.service ${D}/${systemd_unitdir}/system

        install -m 755 ${WORKDIR}/*.sh ${D}/${base_sbindir}/
    fi
    install -d ${D}/${INIT_D_DIR}
    install -m 755 ${WORKDIR}/*.sh ${D}/${INIT_D_DIR}/
}

INITSCRIPT_NAME = "mount-bootfs-userfs.sh"
INITSCRIPT_PARAMS = "start 22 5 3 ."

FILES_${PN} += " ${systemd_unitdir} ${base_sbindir} ${INIT_D_DIR}"

