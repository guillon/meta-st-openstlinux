include recipes-multimedia/gstreamer/gstreamer1.0.inc

LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=6762ed442b3822387a51c92d928ead0d \
                    file://${S}/gst/gst.h;beginline=1;endline=21;md5=e059138481205ee2c6fc1c079c016d0d"

SRCBRANCH = "lms-1.6.0"
SRC_URI = "${ST_GIT_SERVER_URI}/oeivi/oe/multimedia/gstreamer${ST_GIT_SERVER_PROTOCOL};branch=${SRCBRANCH}"
SRCREV = "e1abe5e25e62ba9432de273f24e6d8da782321cb"

S = "${WORKDIR}/git"


do_configure_prepend() {
    srcdir=${S} NOCONFIGURE=1 ${S}/autogen.sh
}

DEPENDS += "libcap"
FILES_${PN}-dev += " ${libdir}/gstreamer-1.0/include "

PACKAGES += "${PN}-bash-completion ${PN}-bash-completion-dbg"
FILES_${PN}-bash-completion = "${datadir}/bash-completion/completions ${datadir}/bash-completion/helpers/gst*"
FILES_${PN}-bash-completion-dbg = "${datadir}/bash-completion/helpers/.debug"

