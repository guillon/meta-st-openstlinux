FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append = " file://0010-waylandsink-Uprank-to-secondary.patch "

PACKAGECONFIG_GL ?= "${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'gles2 egl', '', d)}"

PACKAGECONFIG ?= " \
    ${GSTREAMER_ORC} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'bluez', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'wayland', '', d)} \
    bz2 curl dash dtls hls rsvg sbc smoothstreaming sndfile ttml uvch264 webp \
    faac kms \
"

ARM_INSTRUCTION_SET = "arm"

#do_configure_prepend() {
#    ${S}/autogen.sh --noconfigure
#}

do_install_append() {
    install -d ${D}${libdir}/pkgconfig ${D}${includedir}/gstreamer-1.0/wayland
    install -m 644 ${B}/pkgconfig/gstreamer-wayland.pc ${D}${libdir}/pkgconfig/gstreamer-wayland-1.0.pc
    install -m 644 ${S}/gst-libs/gst/wayland/wayland.h ${D}${includedir}/gstreamer-1.0/wayland
}
