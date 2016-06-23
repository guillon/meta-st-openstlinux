DEFAULT_PREFERENCE = "-1"

include gstreamer1.0-plugins-good.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343 \
                    file://gst/replaygain/rganalysis.c;beginline=1;endline=23;md5=b60ebefd5b2f5a8e0cab6bfee391a5fe"

PV = "st-1.6.0"
PR = "git${SRCPV}.r0"

SRCBRANCH = "lms-1.6.0"
SRC_URI = "${ST_GIT_SERVER_URI}/oeivi/oe/multimedia/gst-plugins-good${ST_GIT_SERVER_PROTOCOL};branch=${SRCBRANCH}"
SRCREV = "ae129003c6e1fffbcb52ed514f1dfb492b48a019"

S = "${WORKDIR}/git"

do_configure_prepend() {
    srcdir=${S} NOCONFIGURE=1 ${S}/autogen.sh
}

