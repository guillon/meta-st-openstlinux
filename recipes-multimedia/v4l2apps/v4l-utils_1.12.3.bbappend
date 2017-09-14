FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}/st-1.12.3:"

SRC_URI_append = " \
    file://0001-Add-autogen.sh.patch \
    file://0002-Correct-X11-dependency.patch \
    file://0003-Align-include-with-kernel.patch \
    file://0004-add-libhva-and-codecparser.patch \
"

DEPENDS += "gstreamer1.0-plugins-base"

