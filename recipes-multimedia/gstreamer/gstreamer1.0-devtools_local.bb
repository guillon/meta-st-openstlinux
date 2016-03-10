SUMMARY = "Development and debugging tools for the GStreamer multimedia framework 1.x"

DEPENDS = "gstreamer1.0"
DEPENDS += "gstreamer1.0-plugins-base"

LICENSE = "LGPLv2.1"

LIC_FILES_CHKSUM = "file://COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343"

inherit autotools gettext stm-externalsrc

#for local source
EXTERNALSRC_pn-gstreamer1.0-devtools ?= "${ST_LOCAL_SRC}gst-devtools/validate"

do_configure_prepend() {
    srcdir=${S} NOCONFIGURE=1 ${S}/autogen.sh
}

do_install_append () {
    install -d ${D}/usr/share/gstreamer-1.0/validate/scenarios
    install -m 0555 -C ${S}/data/scenarios/*.scenario ${D}/usr/share/gstreamer-1.0/validate/scenarios
}

FILES_${PN} += "/usr/share/gstreamer-1.0/* /usr/lib/gstreamer-1.0/validate/* /usr/lib/gst-validate-launcher"
FILES_${PN}-dbg += "${libdir}/gstreamer-1.0/validate/.debug"

RDEPENDS_${PN} += "python python-netclient python-io python-stringold python-shell python-subprocess python-fcntl python-misc python-xml"
