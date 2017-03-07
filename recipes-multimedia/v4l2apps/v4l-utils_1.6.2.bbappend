FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}/st-1.6.2:"

SRC_URI_append = " \
    file://0002-LIBv4l-codecparsers.patch \
    file://0003-add-libv4l-hva.patch \
    file://0004-Adapt-build-to-add-codecparser-and-libv4l-hva.patch \
    "

DEPENDS += "gstreamer1.0-plugins-base"

