FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}/st-1.8.3:"

SRC_URI_append = " \
    file://0001-STM-playbin-force-the-default_flag-value.patch \
    file://0002-STM-encodebin-enables-hardware-scaling.patch \
    file://0003-STM-subtitleoverlay-enable-specific-colorspace-for-s.patch \
    file://0004-STM-tools-gst-play-latency-option.patch \
    file://0005-STM-streamsplitter-push-pending-events-before-serial.patch \
    file://0006-STM-encoding-add-dot-file-dumping-for-pipeline-graph.patch \
    file://0007-STM-alsasink-endianess-fix-for-iec61937.patch \
    file://0008-STM-iec61937-incoherent-endianness-in-payload.patch \
    file://0009-STM-alsasink-fix-iec958-format-detection.patch \
    file://0010-STM-iec61937-force-hw_param-channels-to-stereo.patch \
    file://0011-STM-iec61937-set-iec958-boolean-before-use-it.patch \
    file://0012-PATCH-WIP-allocators-define-GST_CAPS_FEATURE_MEMORY_.patch \
    file://0013-STM-encoding-fix-merge-problem-enables-hardware-scal.patch \
    "

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

CACHED_CONFIGUREVARS += "${@bb.utils.contains('DISTRO_FEATURES', 'hwdecode', '${HW_TRANSFORM_CONFIG}', '', d)}"

do_configure_prepend() {
    ${S}/autogen.sh --noconfigure
}

do_install_append() {
    cp -f ${B}/tests/examples/encoding/.libs/encoding ${D}/${bindir}/
}
