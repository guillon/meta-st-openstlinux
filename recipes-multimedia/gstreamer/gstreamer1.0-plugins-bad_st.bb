DEFAULT_PREFERENCE = "-1"

include recipes-multimedia/gstreamer/gstreamer1.0-plugins-bad.inc

SRCBRANCH = "lms-1.6.0"
SRC_URI = "${ST_GIT_SERVER_URI}/oeivi/oe/multimedia/gst-plugins-bad${ST_GIT_SERVER_PROTOCOL};branch=${SRCBRANCH}"
SRCREV = "41c9a461c6e787db9f4e7b3f612c6d52a4b1937c"

S = "${WORKDIR}/git"


EXTRA_OECONF_remove = "--disable-osx_video --disable-quicktime --disable-directdraw --disable-mythtv --disable-libssh2"

#LICENSE = "GPLv2+ & LGPLv2+ & LGPLv2.1+ "
LIC_FILES_CHKSUM = "file://COPYING;md5=73a5855a8119deb017f5f13cf327095d \
                    file://gst/tta/filters.h;beginline=12;endline=29;md5=8a08270656f2f8ad7bb3655b83138e5a \
                    file://COPYING.LIB;md5=21682e4e8fea52413fd26c60acb907e5 \
                    file://gst/tta/crc32.h;beginline=12;endline=29;md5=27db269c575d1e5317fffca2d33b3b50"

PACKAGECONFIG ??= " \
   ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'wayland', '', d)} \
   ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'gles2', '', d)} \
   ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'bluez', '', d)} \
   ${@bb.utils.contains('DISTRO_FEATURES', 'directfb', 'directfb', '', d)} \
   orc curl uvch264 neon \
   hls sbc dash bz2 smoothstreaming \
   faac \
   libsmaf \
   "

PACKAGECONFIG[libsmaf] = ",,libsmaf,libsmaf"

ARM_INSTRUCTION_SET = "arm"

do_configure_prepend() {
    srcdir=${S} NOCONFIGURE=1 ${S}/autogen.sh
}

DEPENDS += "openssl"
RDEPENDS_${PN}-dtls += "openssl"

FILES_${PN}-dev += "${libdir}/gstreamer-1.0/include/gst/gl/gstglconfig.h"
FILES_${PN}-freeverb += "${datadir}/gstreamer-1.0/presets/GstFreeverb.prs"
