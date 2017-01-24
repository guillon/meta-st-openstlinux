FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}/st-1.8.3:"

SRC_URI_append = " \
    file://0001-STM-videomixer-downstream-pool-support.patch \
    file://0002-v4l2object-force-DMABUF-by-default-for-capture-io-mo.patch \
    file://0003-v4l2object-TMP-workaround-for-unaligned-video.patch \
    file://0004-v4l2object-fix-video-alignment.patch \
    file://0005-v4l2bufferpool-fix-condition-for-copy-threshold.patch \
    file://0006-v4l2videodec-increase-ranking.patch \
    file://0007-fixup-v4l2object-Add-MPEG1-2-support.patch \
    file://0008-v4l2object-Add-VC1-support.patch \
    file://0009-v4l2object-Add-DivX-support.patch \
    file://0010-v4l2object-disable-copy-threshold-if-memory-DMABuf-i.patch \
    file://0011-v4l2object-force-capture-io-mode-if-memory-DMABuf-in.patch \
    file://0012-v4l2object-function-to-add-memory-DMABuf-feature-in-.patch \
    file://0013-v4l2src-enable-memory-DMABuf-feature-in-caps.patch \
    file://0014-v4l2videodec-enable-memory-DMABuf-feature-in-caps.patch \
    file://0015-v4l2object-probe-all-colorspace-supported-by-device.patch \
    file://0016-v4l2object-refactor-gst_v4l2_object_get_colorspace-t.patch \
    file://0017-v4l2object-fill-colorimetry-in-gst_v4l2_object_acqui.patch \
    file://0018-v4l2videodec-unref-frame-before-processing-the-input.patch \
    file://0019-v4l2object-force-DMABUF_IMPORT-for-output-io-mode.patch \
    file://0020-v4l2transform-enable-memory-DMABuf-feature-in-caps.patch \
    file://0021-v4l2transform-keep-colorimetry-and-chroma-site-field.patch \
    file://0022-v4l2transform-set-the-output-format-from-imported-po.patch \
    file://0023-v4l2allocator-unref-memory-before-the-buffer-is-queu.patch \
    file://0024-v4l2object-allows-a-single-OUTPUT-buffer.patch \
    file://0025-v4l2-fix-missing-v4l2_calls-indirection.patch \
    file://0026-v4l2object-use-G_SELECTION-instead-of-G_CROP-in-gst_.patch \
    "

PACKAGECONFIG ?= " \
    ${GSTREAMER_ORC} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'pulseaudio', 'pulseaudio', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'x11', '', d)} \
    cairo flac gdk-pixbuf gudev jpeg libpng soup speex taglib v4l2 \
    libv4l2 \
"

EXTRA_OECONF += " \
    --enable-v4l2-probe \
    "

do_configure_prepend() {
    ${S}/autogen.sh --noconfigure
}
