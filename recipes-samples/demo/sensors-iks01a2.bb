# Copyright (C) 2018, STMicroelectronics - All Rights Reserved
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Python script which monitor temperature from sensor on Nucleo extension board iks01a2a"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

PR = "r0"

SRC_URI = " \
    file://sensors_temperature.py \
    file://stlogo.png \
    "

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${bindir}
    install -d ${D}${datadir}/sensors_temperature/

    install -m 0755 ${WORKDIR}/sensors_temperature.py ${D}${bindir}
    install -m 0644 ${WORKDIR}/stlogo.png ${D}${datadir}/sensors_temperature/
}

FILES_${PN} += "${datadir}/sensors_temperature/"
RDEPENDS_${PN} += "python3 python3-pygobject python3-pycairo gtk+3 "
