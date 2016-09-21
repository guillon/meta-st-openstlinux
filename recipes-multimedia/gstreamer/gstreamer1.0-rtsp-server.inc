SUMMARY = "A library on top of GStreamer for building an RTSP server"
HOMEPAGE = "http://cgit.freedesktop.org/gstreamer/gst-rtsp-server/"
SECTION = "multimedia"
LICENSE = "LGPLv2"

DEPENDS = "gstreamer1.0 libcgroup gstreamer1.0-plugins-base"

PNREAL = "gst-rtsp-server"

SRC_URI = "http://gstreamer.freedesktop.org/src/${PNREAL}/${PNREAL}-${PV}.tar.xz"

S = "${WORKDIR}/${PNREAL}-${PV}"

inherit autotools pkgconfig

FILES_${PN}     += "${libdir}/gstreamer-1.0"
FILES_${PN}-dev += "${libdir}/gstreamer-1.0/*.la"
FILES_${PN}-dbg += "${libdir}/gstreamer-1.0/.debug"
