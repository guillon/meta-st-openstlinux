DEFAULT_PREFERENCE = "-1"

include gstreamer1.0-plugins-base.inc

# Untill patch task is fixed for devtool usage
include gstreamer1.0-fixdevtool.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=c54ce9345727175ff66d17b67ff51f58 \
                    file://COPYING.LIB;md5=6762ed442b3822387a51c92d928ead0d \
                    file://common/coverage/coverage-report.pl;beginline=2;endline=17;md5=a4e1830fce078028c8f0974161272607"

SRCBRANCH = "lms-1.8.3"
SRC_URI = "${ST_GIT_SERVER_URI}/oeivi/oe/multimedia/gst-plugins-base${ST_GIT_SERVER_PROTOCOL};branch=${SRCBRANCH};name=base"
SRC_URI_append = " git://anongit.freedesktop.org/gstreamer/common;destsuffix=git/common;name=common"

PV = "st-1.8.3"
PR = "git${SRCPV}.r0"

UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>(\d+(\.\d+)+))"

SRCREV_base = "f65b6400cb289567aa91ca7535172eba35e9b826"
SRCREV_common = "f363b3205658a38e84fa77f19dee218cd4445275"
SRCREV_FORMAT = "base"

S = "${WORKDIR}/git"

PACKAGECONFIG ?= " \
    ${GSTREAMER_ORC} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'alsa', 'alsa', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'x11', '', d)} \
    ivorbis ogg pango theora vorbis \
    encoding \
"

PACKAGECONFIG[encoding]    = "--enable-encoding,--disable-encoding,"

EXTRA_OECONF += " \
    --enable-examples \
"

#enable hardware convert/scale in playbin (gstsubtitleoverlay.c, gstplaysinkvideoconvert.c, gstplaysink.c) & gstencodebin (gstencodebin.c)
#disable software convert/scale/rate in gstencodebin (gstencodebin.c)
HW_TRANSFORM_CONFIG = 'CFLAGS="-DCOLORSPACE=\\\\\\"autovideoconvert\\\\\\" \
                               -DCOLORSPACE_SUBT=\\\\\\"videoconvert\\\\\\" \
                               -DGST_PLAYBIN_DEFAULT_FLAGS=0x00000017 \
                               -DCOLORSPACE2=\\\\\\"identity\\\\\\" \
                               -DVIDEOSCALE=\\\\\\"identity\\\\\\" \
                               -DVIDEORATE=\\\\\\"identity\\\\\\" "'

CACHED_CONFIGUREVARS += "${@bb.utils.contains('DISTRO_FEATURES', 'swdecode', '', '${HW_TRANSFORM_CONFIG}', d)}"

do_configure_prepend() {
    ${S}/autogen.sh --noconfigure
}

do_install_append() {
    cp -f ${B}/tests/examples/encoding/.libs/encoding ${D}/${bindir}/
}
