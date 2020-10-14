SUMMARY= "GTK player with touch screen management"
LICENSE = "GPL-2.0+"
LIC_FILES_CHKSUM = "file://COPYING;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "git://gerrit.st.com:29418/oeivi/oe/st/st-openstlinux-application;protocol=ssh;branch=master"

# Modify these as desired
PV = "2.1.0+git${SRCPV}"
SRCREV = "8917d65d9d57143d0a86bde679d30a0af20b6006"

DEPENDS += "gstreamer1.0 gstreamer1.0-plugins-base gstreamer1.0-plugins-bad gtk+3"

inherit meson pkgconfig

S = "${WORKDIR}/git/touch-event-gtk-player"

do_install () {
	install -d ${D}${prefix}/local/demo/bin
	install -m 0755 ${B}/touch-event-gtk-player ${D}${prefix}/local/demo/bin/
}
FILES_${PN} += "${prefix}/local/demo/bin"

