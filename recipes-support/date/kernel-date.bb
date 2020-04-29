# Copyright (C) 2019, STMicroelectronics - All Rights Reserved
DESCRIPTION = "Set date of kernel at first boot"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI = " \
    file://set_kernel_date.service \
    file://set_kernel_date.sh \
"

S = "${WORKDIR}"

inherit systemd

SYSTEMD_PACKAGES += " ${PN} "
SYSTEMD_SERVICE_${PN} = "set_kernel_date.service"
SYSTEMD_AUTO_ENABLE_${PN} = "enable"

do_install() {
    install -d ${D}${systemd_unitdir}/system ${D}${base_sbindir}
    install -m 0644 ${WORKDIR}/set_kernel_date.service ${D}${systemd_unitdir}/system
    install -m 0755 ${WORKDIR}/set_kernel_date.sh ${D}${base_sbindir}

    sed -i -e "s:@sbindir@:${base_sbindir}:; s:@sysconfdir@:${sysconfdir}:" ${D}${systemd_unitdir}/system/set_kernel_date.service
}
