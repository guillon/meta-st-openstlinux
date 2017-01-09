DEFAULT_PREFERENCE = "-1"

include gstreamer1.0-plugins-good.inc

# Untill patch task is fixed for devtool usage
include gstreamer1.0-fixdevtool.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343 \
                    file://common/coverage/coverage-report.pl;beginline=2;endline=17;md5=a4e1830fce078028c8f0974161272607 \
                    file://gst/replaygain/rganalysis.c;beginline=1;endline=23;md5=b60ebefd5b2f5a8e0cab6bfee391a5fe"

SRCBRANCH = "lms-1.8.3"
SRC_URI = "${ST_GIT_SERVER_URI}/oeivi/oe/multimedia/gst-plugins-good${ST_GIT_SERVER_PROTOCOL};branch=${SRCBRANCH};name=base"
SRC_URI_append = " git://anongit.freedesktop.org/gstreamer/common;destsuffix=git/common;name=common"

PV = "st-1.8.3"
PR = "git${SRCPV}.r0"

UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>(\d+(\.\d+)+))"

SRCREV_base = "6607c6203f9221da3aaf2aef4ddd7e0b249a47be"
SRCREV_common = "f363b3205658a38e84fa77f19dee218cd4445275"
SRCREV_FORMAT = "base"

S = "${WORKDIR}/git"

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
