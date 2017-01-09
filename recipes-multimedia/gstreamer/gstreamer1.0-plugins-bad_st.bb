DEFAULT_PREFERENCE = "-1"

include gstreamer1.0-plugins-bad.inc

# Untill patch task is fixed for devtool usage
include gstreamer1.0-fixdevtool.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=73a5855a8119deb017f5f13cf327095d \
                    file://COPYING.LIB;md5=21682e4e8fea52413fd26c60acb907e5 \
                    file://gst/tta/crc32.h;beginline=12;endline=29;md5=27db269c575d1e5317fffca2d33b3b50 \
                    file://gst/tta/filters.h;beginline=12;endline=29;md5=8a08270656f2f8ad7bb3655b83138e5a"

SRCBRANCH = "lms-1.8.3"
SRC_URI = "${ST_GIT_SERVER_URI}/oeivi/oe/multimedia/gst-plugins-bad${ST_GIT_SERVER_PROTOCOL};branch=${SRCBRANCH};name=base"
SRC_URI_append = " git://anongit.freedesktop.org/gstreamer/common;destsuffix=git/common;name=common"

PV = "st-1.8.3"
PR = "git${SRCPV}.r0"

UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>(\d+(\.\d+)+))"

SRCREV_base = "a25389aee1b9c32a7c7969d3c2d2325d4d55108b"
SRCREV_common = "f363b3205658a38e84fa77f19dee218cd4445275"
SRCREV_FORMAT = "base"

S = "${WORKDIR}/git"

PACKAGECONFIG ?= " \
    ${GSTREAMER_ORC} \
    ${PACKAGECONFIG_GL} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'bluez', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'directfb', 'directfb', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'wayland egl', '', d)} \
    bz2 curl dash hls neon sbc smoothstreaming sndfile uvch264  \
    faac \
    libsmaf \
"

PACKAGECONFIG[libsmaf] = ",,libsmaf,libsmaf"

ARM_INSTRUCTION_SET = "arm"

do_configure_prepend() {
	${S}/autogen.sh --noconfigure
}

# In 1.6.2, the "--enable-hls" configure option generated an installable package
# called "gstreamer1.0-plugins-bad-fragmented". In 1.7.1 that HLS plugin package
# has become "gstreamer1.0-plugins-bad-hls". See:
# http://cgit.freedesktop.org/gstreamer/gst-plugins-bad/commit/?id=efe62292a3d045126654d93239fdf4cc8e48ae08

PACKAGESPLITFUNCS_append = " handle_hls_rename "

python handle_hls_rename () {
    d.setVar('RPROVIDES_gstreamer1.0-plugins-bad-hls', 'gstreamer1.0-plugins-bad-fragmented')
    d.setVar('RREPLACES_gstreamer1.0-plugins-bad-hls', 'gstreamer1.0-plugins-bad-fragmented')
    d.setVar('RCONFLICTS_gstreamer1.0-plugins-bad-hls', 'gstreamer1.0-plugins-bad-fragmented')
}
