DESCRIPTION = "SMAF library"

LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=d749e86a105281d7a44c2328acebc4b0"
DEPENDS = "virtual/kernel"

PV="1.0+git"

inherit autotools gettext pkgconfig

SRC_URI = "git://git.linaro.org/people/benjamin.gaignard/libsmaf.git;protocol=http"
S = "${WORKDIR}/git"

SRCREV = "809068982a3d3ee9b601bf4798db16d12590a27b"
