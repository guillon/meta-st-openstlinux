include gstreamer1.0.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=6762ed442b3822387a51c92d928ead0d \
                    file://gst/gst.h;beginline=1;endline=21;md5=e059138481205ee2c6fc1c079c016d0d"

PV = "st-1.8.0"
PR = "git${SRCPV}.r0"

SRCBRANCH = "lms-1.8.0"
SRC_URI = "${ST_GIT_SERVER_URI}/oeivi/oe/multimedia/gstreamer${ST_GIT_SERVER_PROTOCOL};branch=${SRCBRANCH}"
SRCREV = "1c5c2df33554f866ae5c779676423918ddac7485"

S = "${WORKDIR}/git"


do_configure_prepend() {
    srcdir=${S} NOCONFIGURE=1 ${S}/autogen.sh
}
