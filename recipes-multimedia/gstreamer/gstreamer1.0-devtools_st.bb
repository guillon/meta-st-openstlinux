SUMMARY = "Development and debugging tools for the GStreamer multimedia framework 1.x"

DEPENDS = "gstreamer1.0"
DEPENDS += "gstreamer1.0-plugins-base"

LICENSE = "LGPLv2.1"

LIC_FILES_CHKSUM = "file://COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343"

inherit autotools gettext

PV = "st-1.8.0"
PR = "git${SRCPV}.r0"

SRCBRANCH = "lms-1.8.0"
SRC_URI = "${ST_GIT_SERVER_URI}/oeivi/oe/multimedia/gst-devtools${ST_GIT_SERVER_PROTOCOL};branch=${SRCBRANCH}"
SRCREV = "1f1cf2f1139c175945a33e34ed1b051b21a594ff"

S = "${WORKDIR}/git/validate"
B = "${S}"

do_configure_prepend() {
    srcdir=${S} NOCONFIGURE=1 ${S}/autogen.sh
}

do_install_append () {
    install -d ${D}/usr/share/gstreamer-1.0/validate/scenarios
    install -m 0555 -C ${S}/data/scenarios/*.scenario ${D}/usr/share/gstreamer-1.0/validate/scenarios
}

FILES_${PN} += "${datadir}/gstreamer-1.0/*"
FILES_${PN} += "${libdir}/gstreamer-1.0/*"
FILES_${PN} += "${libdir}/gst-validate-launcher"

FILES_${PN}-dbg += "${libdir}/gstreamer-1.0/validate/.debug"
FILES_${PN}-dbg += "${libdir}/gstreamer-1.0/.debug"

FILES_${PN}-dev += "${libdir}/gstreamer-1.0/*.so"

RDEPENDS_${PN} += "python python-netclient python-io python-stringold python-shell python-subprocess python-fcntl python-misc python-xml"
