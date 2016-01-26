DESCRIPTION = "v4l2 and IR applications"

LICENSE = "GPLv2 & LGPLv2.1"
LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=48da9957849056017dc568bbc43d8975 \
                    file://${S}/COPYING.libv4l;md5=d749e86a105281d7a44c2328acebc4b0"

PV="1.0+local"

DEPENDS = "jpeg gstreamer1.0-plugins-base"
DEPENDS += " ${@base_contains('DISTRO_FEATURES', 'x11', 'virtual/libx11', '', d)} "

inherit autotools gettext externalsrc pkgconfig

EXTERNALSRC_pn-v4l-utils ?= "${ST_LOCAL_SRC}v4l-utils"

# libv4l was absorbed into this, let OE know that
PROVIDES = "libv4l"

EXTRA_OECONF = "--disable-qv4l2 --enable-shared"

PACKAGES =+ "rc-keymaps libv4l libv4l-dbg libv4l-dev"

FILES_rc-keymaps = "${sysconfdir}/rc* ${base_libdir}/udev/rc*"
FILES_${PN} = "${bindir} ${sbindir} ${base_libdir}/udev/rules.d/70-infrared.rules"
FILES_libv4l += "${libdir}/libv4l/*.so.* ${libdir}/libv4l/*.so ${libdir}/libv4l/plugins/*.so ${libdir}/*.so.*"
FILES_libv4l-dbg += "${libdir}/libv4l/.debug/ ${libdir}/libv4l/plugins/.debug "
FILES_libv4l-dev += "${libdir}/*.so ${includedir}/lib* ${libdir}/pkgconfig/lib* ${libdir}/libv4l/ov* ${libdir}/libv4l/*.la ${libdir}/libv4l/plugins/*.la "
