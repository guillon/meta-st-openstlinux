SUMMARY = "configuration files"
DESCRIPTION = "configuration files installation"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = " file://dvb-channels.conf "

FILES_${PN} += "/home/root/.config/gstreamer-1.0/dvb-channels.conf"

do_install() {
  install -d ${D}/home/root/.config/gstreamer-1.0
  install -m 0755 ${WORKDIR}/dvb-channels.conf ${D}/home/root/.config/gstreamer-1.0/dvb-channels.conf
}
