FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}/st-1.6.2:"

SRC_URI_append = " \
    file://0001-Addition-of-the-autogen.sh-file-to-ensure-that-our-p.patch \
    file://0002-libv4l2-remove-actions-if-conversion-disabled.patch \
    file://0003-libv4l2-set-log-file-before-plugin-initialization.patch \
    file://0004-remove-X11-dependency-for-test-program.patch \
    file://0005-lib4l-hva-add-new-plugin-for-hva.patch \
    file://0006-libv4l-hva-H264-vaapi-encoder-added.patch \
    file://0007-libv4l-hva-H264-vaapi-encoder-supported.patch \
    file://0008-lib4l-hva-Implementation-of-h264-header-encoder.patch \
    file://0009-libv4l-hva-missing-h264-parser.patch \
    file://0010-libv4l-hva-manage-frame-packing-arrangement.patch \
    file://0011-libv4l-hva-add-second-chroma-qp-index-offset-in-pps.patch \
    file://0012-libv4l-hva-fix-missing-unmap-on-close.patch \
    file://0013-libv4l-hva-fix-sps-pps-memory-leak.patch \
    file://0014-v4l-utils-libv4l-hva-fix-for-T8x8-for-stereo-profile.patch \
    file://0015-v4l-utils-libv4l-hva-new-ops-set_ctrl-added.patch \
    "


