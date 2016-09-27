DEFAULT_PREFERENCE = "-1"

include gstreamer1.0-plugins-good.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343 \
                    file://gst/replaygain/rganalysis.c;beginline=1;endline=23;md5=b60ebefd5b2f5a8e0cab6bfee391a5fe"

PV = "st-1.8.0"
PR = "git${SRCPV}.r0"

SRCBRANCH = "lms-1.8.0"
SRC_URI = "${ST_GIT_SERVER_URI}/oeivi/oe/multimedia/gst-plugins-good${ST_GIT_SERVER_PROTOCOL};branch=${SRCBRANCH}"
SRCREV = "b0b9779875ec5cec2482d2b936eb4b8b1161a692"

S = "${WORKDIR}/git"


PACKAGECONFIG ??= " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'pulseaudio', 'pulseaudio', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'x11', '', d)} \
    cairo flac gdk-pixbuf gudev jpeg libpng orc soup speex taglib v4l2 \
"

EXTRA_OECONF += " \
    --enable-v4l2-probe \
"



do_configure_prepend() {
    srcdir=${S} NOCONFIGURE=1 ${S}/autogen.sh
}

