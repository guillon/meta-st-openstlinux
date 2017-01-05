DEFAULT_PREFERENCE = "-1"

include gstreamer1.0.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=6762ed442b3822387a51c92d928ead0d \
                    file://gst/gst.h;beginline=1;endline=21;md5=e059138481205ee2c6fc1c079c016d0d"

SRCBRANCH = "lms-1.8.3"
SRC_URI = "${ST_GIT_SERVER_URI}/oeivi/oe/multimedia/gstreamer${ST_GIT_SERVER_PROTOCOL};branch=${SRCBRANCH};name=base"
SRC_URI_append = " git://anongit.freedesktop.org/gstreamer/common;destsuffix=git/common;name=common"

PV = "st-1.8.3"
PR = "git${SRCPV}.r0"

UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>(\d+(\.\d+)+))"

SRCREV_base = "3cba0df969495b37e643e4aa6dd520c0e5cd98b9"
SRCREV_common = "f363b3205658a38e84fa77f19dee218cd4445275"
SRCREV_FORMAT = "base"

S = "${WORKDIR}/git"

do_configure_prepend() {
	${S}/autogen.sh --noconfigure
}
