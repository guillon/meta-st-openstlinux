DEFAULT_PREFERENCE = "-1"

include recipes-multimedia/gstreamer/gstreamer1.0-plugins-base.inc

LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=c54ce9345727175ff66d17b67ff51f58 \
                    file://${S}/COPYING.LIB;md5=6762ed442b3822387a51c92d928ead0d"


SRCBRANCH = "lms-1.6.0"
SRC_URI = "${ST_GIT_SERVER_URI}/oeivi/oe/multimedia/gst-plugins-base${ST_GIT_SERVER_PROTOCOL};branch=${SRCBRANCH}"
SRCREV = "ce02e461d7f2b3f3dbe1e86cbeec56e2811c2920"

S = "${WORKDIR}/git"


PACKAGECONFIG ??= " \
    encoding \
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'x11', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'alsa', 'alsa', '', d)} \
    orc ivorbis ogg theora vorbis \
    pango \
     "
PACKAGECONFIG[encoding]    = "--enable-encoding,--disable-encoding,"
EXTRA_OECONF += " \
    --enable-examples \
"

#enable hardware convert/scale in playbin (gstsubtitleoverlay.c, gstplaysinkvideoconvert.c, gstplaysink.c) & gstencodebin (gstencodebin.c)
#disable software convert/scale/rate in gstencodebin (gstencodebin.c), hardware convert/scale enabled through COLORSPACE define (gstencodebin.c)
CACHED_CONFIGUREVARS += '   CFLAGS="-DCOLORSPACE=\\\"v4l2trans\\\" \
                                    -DCOLORSPACE_SUBT=\\\"videoconvert\\\" \
                                    -DGST_PLAYBIN_DEFAULT_FLAGS=0x00000017 \
                                    -DCOLORSPACE2=\\\"identity\\\" \
                                    -DVIDEOSCALE=\\\"identity\\\" \
                                    -DVIDEORATE=\\\"identity\\\" "'

do_configure_prepend() {
    srcdir=${S} NOCONFIGURE=1 ${S}/autogen.sh
}

do_install_append() {
    cp -f ${B}/tests/examples/encoding/.libs/encoding ${D}/${bindir}/
}
