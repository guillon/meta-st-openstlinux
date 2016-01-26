DEFAULT_PREFERENCE = "-1"

include recipes-multimedia/gstreamer/gstreamer1.0-plugins-base.inc

inherit externalsrc

EXTERNALSRC_pn-gstreamer1.0-plugins-base ?= "${ST_LOCAL_SRC}gst-plugins-base"

LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=c54ce9345727175ff66d17b67ff51f58 \
                    file://${S}/common/coverage/coverage-report.pl;beginline=2;endline=17;md5=a4e1830fce078028c8f0974161272607 \
                    file://${S}/COPYING.LIB;md5=6762ed442b3822387a51c92d928ead0d"

PACKAGECONFIG ??= " \
    encoding \
    ${@base_contains('DISTRO_FEATURES', 'x11', 'x11', '', d)} \
    ${@base_contains('DISTRO_FEATURES', 'alsa', 'alsa', '', d)} \
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
