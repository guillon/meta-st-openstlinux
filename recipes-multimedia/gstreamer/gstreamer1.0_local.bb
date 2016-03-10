include recipes-multimedia/gstreamer/gstreamer1.0.inc
inherit stm-externalsrc

EXTERNALSRC_pn-gstreamer1.0 ?= "${ST_LOCAL_SRC}gstreamer"

LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=6762ed442b3822387a51c92d928ead0d \
                    file://${S}/gst/gst.h;beginline=1;endline=21;md5=e059138481205ee2c6fc1c079c016d0d"

do_configure_prepend() {
    srcdir=${S} NOCONFIGURE=1 ${S}/autogen.sh
}

DEPENDS += "libcap"
FILES_${PN}-dev += " ${libdir}/gstreamer-1.0/include "

PACKAGES += "${PN}-bash-completion ${PN}-bash-completion-dbg"
FILES_${PN}-bash-completion = "${datadir}/bash-completion/completions ${datadir}/bash-completion/helpers/gst*"
FILES_${PN}-bash-completion-dbg = "${datadir}/bash-completion/helpers/.debug"