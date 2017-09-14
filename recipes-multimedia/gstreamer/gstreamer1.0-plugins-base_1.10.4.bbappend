FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}/:"

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
#HW_TRANSFORM_CONFIG = 'CFLAGS="-DCOLORSPACE=\\\\\\"autovideoconvert\\\\\\" \
#                               -DCOLORSPACE_SUBT=\\\\\\"videoconvert\\\\\\" \
#                               -DGST_PLAYBIN_DEFAULT_FLAGS=0x00000017 \
#                               -DCOLORSPACE2=\\\\\\"identity\\\\\\" \
#                               -DVIDEOSCALE=\\\\\\"identity\\\\\\" \
#                               -DVIDEORATE=\\\\\\"identity\\\\\\" "'

#CACHED_CONFIGUREVARS += "${@bb.utils.contains('DISTRO_FEATURES', 'hwdecode', '${HW_TRANSFORM_CONFIG}', '', d)}"

do_install_append() {
    cp -f ${B}/tests/examples/encoding/.libs/encoding ${D}/${bindir}/
}
