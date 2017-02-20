# Copyright (C) 2016 Christophe Priouzeau <christophe.priouzeau@st.com>
# Released under the MIT license (see COPYING.MIT for the terms)

# Tools extracted from 96boards-tools https://github.com/96boards/96boards-tools
DESCRIPTION = "Tools for resizing the file system"
SECTION = "devel"
PR = "r0"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

# e2fsprogs for resize2fs
# gptfdisk for sgdisk
# parted for parted and partprobe (Gplv3)
# util-linux for findmnt
DEPENDS = " e2fsprogs gptfdisk util-linux"

SRC_URI = " file://resize-helper.service file://resize-helper"

S = "${WORKDIR}"

inherit systemd

SYSTEMD_PACKAGES += " resize-helper "
SYSTEMD_SERVICE_${PN} = "resize-helper.service"
SYSTEMD_AUTO_ENABLE_${PN} = "enable"

do_install() {
    install -d ${D}${systemd_unitdir}/system ${D}${base_sbindir}
    install -m 0644 ${WORKDIR}/resize-helper.service ${D}${systemd_unitdir}/system
    install -m 0755 ${WORKDIR}/resize-helper ${D}${base_sbindir}
}

