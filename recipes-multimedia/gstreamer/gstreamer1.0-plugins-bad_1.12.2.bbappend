FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append = " file://0002-waylandsink-Disable-video-alignment-from-the-pool.patch "
SRC_URI_append = " file://0003-waylandsink-Only-try-SHM-for-single-memory-buffer.patch "
SRC_URI_append = " file://0004-waylandsink-Allocate-only-what-s-needed-when-copying.patch "
SRC_URI_append = " file://0005-waylandsink-Update-video-info-size-to-buffer-size.patch "
SRC_URI_append = " file://0006-waylandsink-Validate-strides-and-offset-when-using-F.patch "
SRC_URI_append = " file://0007-waylandsink-Rollback-video-info-changes-when-copying.patch "
SRC_URI_append = " file://0008-waylandsink-Add-prototype-for-gst_wayand_pool_get_ty.patch "
SRC_URI_append = " file://0009-waylandsink-Fix-memory-leak-of-shm-allocator.patch "
SRC_URI_append = " file://0010-waylandsink-Fix-memory-leak-of-shm-allocator.patch "

# remove egl (glimagesink)
# PACKAGECONFIG_GL ?= "${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'gles2 egl', '', d)}"
PACKAGECONFIG_GL = ""

PACKAGECONFIG ?= " \
    ${GSTREAMER_ORC} \
    ${PACKAGECONFIG_GL} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'bluez', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'directfb', 'directfb', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'wayland', '', d)} \
    bz2 curl dash hls neon sbc smoothstreaming sndfile uvch264  \
    faac kms\
"

ARM_INSTRUCTION_SET = "arm"

do_configure_prepend() {
    ${S}/autogen.sh --noconfigure
}

do_install_append() {
    install -d ${D}${libdir}/pkgconfig ${D}${includedir}/gstreamer-1.0/wayland
    install -m 644 ${B}/pkgconfig/gstreamer-wayland.pc ${D}${libdir}/pkgconfig/gstreamer-wayland-1.0.pc
    install -m 644 ${S}/gst-libs/gst/wayland/wayland.h ${D}${includedir}/gstreamer-1.0/wayland
}

# In 1.6.2, the "--enable-hls" configure option generated an installable package
# called "gstreamer1.0-plugins-bad-fragmented". In 1.7.1 that HLS plugin package
# has become "gstreamer1.0-plugins-bad-hls". See:
# http://cgit.freedesktop.org/gstreamer/gst-plugins-bad/commit/?id=efe62292a3d045126654d93239fdf4cc8e48ae08

PACKAGESPLITFUNCS_append = " handle_hls_rename "

python handle_hls_rename () {
    d.setVar('RPROVIDES_gstreamer1.0-plugins-bad-hls', 'gstreamer1.0-plugins-bad-fragmented')
    d.setVar('RREPLACES_gstreamer1.0-plugins-bad-hls', 'gstreamer1.0-plugins-bad-fragmented')
    d.setVar('RCONFLICTS_gstreamer1.0-plugins-bad-hls', 'gstreamer1.0-plugins-bad-fragmented')
}
