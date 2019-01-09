# Copyright (C) 2018, STMicroelectronics - All Rights Reserved

DESCRIPTION = "Python script which launch several use-cases"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${OPENSTLINUX_BASE}/files/licenses/ST-Proprietary;md5=7cb1e55a9556c7dd1a3cae09db9cc85f"

DEPENDS += "demo-hotspot-wifi"
DEPENDS += "ai-hand-char-reco-launcher"


SRC_URI = " \
    file://demo_launcher.py \
    file://bin \
    file://pictures \
    file://media \
    "

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${prefix}/local/demo/
    install -d ${D}${prefix}/local/demo/bin
    install -d ${D}${prefix}/local/demo/pictures
    install -d ${D}${prefix}/local/demo/media
    install -m 0755 ${WORKDIR}/demo_launcher.py ${D}${prefix}/local/demo/
    install -m 0755 ${WORKDIR}/bin/* ${D}${prefix}/local/demo/bin
    install -m 0644 ${WORKDIR}/pictures/* ${D}${prefix}/local/demo/pictures/
    install -m 0644 ${WORKDIR}/media/* ${D}${prefix}/local/demo/media/
}

FILES_${PN} += "${prefix}/local/demo/"

RDEPENDS_${PN} += "python3 python3-pygobject gtk+3 gstreamer1.0-plugins-base python3-ptyprocess python3-pexpect python3-terminal python3-resource"
