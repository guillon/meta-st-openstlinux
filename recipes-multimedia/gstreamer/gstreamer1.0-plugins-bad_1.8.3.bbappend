FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}/st-1.8.3:"

SRC_URI_append = " \
    file://0001-STM-h264parse-update-src-caps-if-resolution-change.patch \
    file://0002-STM-dvbbasebin-optional-support-of-target-device-in-.patch \
    file://0003-STM-h265parse-ignore-and-continue-decode-if-out-of-r.patch \
    file://0004-STM-codecparser-h265-correct-frame-height-for-interl.patch \
    file://0005-STM-videoparser-h265-fix-all-au-boundary-issues.patch \
    file://0006-wayland-Update-from-scaler-to-viewporter-protocol.patch \
    file://0007-wayland-fix-wayland-protocols-path.patch \
    file://0008-STM-waylandsink-disable-last-sample-sink-feature.patch \
    file://0009-STM-waylandsink-increase-bufferpool-size.patch \
    file://0010-STM-waylandsink-set-video-alignment.patch \
    file://0011-STM-waylandsink-manage-server-window-resizing.patch \
    file://0012-STM-waylandsink-support-fullscreen.patch \
    file://0013-STM-waylandsink-rendering-window-size-setting.patch \
    file://0014-STM-waylandsink-fix-RGB888-SHM-format-conversion.patch \
    file://0015-STM-waylandsink-ranked-as-primary.patch \
    file://0016-STM-waylandsink-support-linux-dmabuf-protocol.patch \
    file://0017-STM-waylandsink-fix-build-warnings.patch \
    file://0018-STM-waylandsink-support-dmabuf-YUY2-and-I420-pixel-f.patch \
    file://0019-STM-waylandsink-fix-bad-no_wl_buffer-statement.patch \
    file://0020-STM-waylandsink-memory-DMABuf-preferred-while-negoti.patch \
    file://0021-glupload-enable-memory-DMABuf-feature-in-caps.patch \
    file://0022-STM-waylandsink-specify-the-minimum-number-of-buffer.patch \
    file://0023-STM-waylandsink-use-buffer-video-meta.patch \
    file://0024-STM-waylandsink-fix-display-reconnection-redraw-pend.patch \
    "

PACKAGECONFIG ?= " \
    ${GSTREAMER_ORC} \
    ${PACKAGECONFIG_GL} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'bluez', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'directfb', 'directfb', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'wayland egl', '', d)} \
    bz2 curl dash hls neon sbc smoothstreaming sndfile uvch264  \
    faac \
    libsmaf \
"

PACKAGECONFIG[libsmaf] = ",,libsmaf,libsmaf"

ARM_INSTRUCTION_SET = "arm"

do_configure_prepend() {
    ${S}/autogen.sh --noconfigure
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
