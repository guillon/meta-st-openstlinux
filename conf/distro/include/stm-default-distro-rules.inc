#@DESCRIPTION: STM default distro rules

#--------------------------
# Mirror settings
#
YOCTOPROJECT_MIRROR_URL = "http://downloads.yoctoproject.org/mirror/sources/"

MIRRORS =+ "\
ftp://.*/.*    ${YOCTOPROJECT_MIRROR_URL} \n \
http://.*/.*   ${YOCTOPROJECT_MIRROR_URL} \n \
https://.*/.*  ${YOCTOPROJECT_MIRROR_URL} \n"

INHERIT += "own-mirrors"
SOURCE_MIRROR_URL ??= "${YOCTOPROJECT_MIRROR_URL}"

#--------------------------
# The CONNECTIVITY_CHECK_URI's are used to test whether we can succesfully
# fetch from the network (and warn you if not).
# URI's to check can be set in the CONNECTIVITY_CHECK_URIS variable
# using the same syntax as for SRC_URI. If the variable is not set
# the check is skipped
#
CONNECTIVITY_CHECK_URIS ?= " \
    https://eula-downloads.yoctoproject.org/index.php \
    http://bugzilla.yoctoproject.org/report.cgi"

#--------------------------
# OELAYOUT_ABI allows us to notify users when the format of TMPDIR changes in
# an incompatible way. Such changes should usually be detailed in the commit
# that breaks the format and have been previously discussed on the mailing list
# with general agreement from the core team.
#
OELAYOUT_ABI = "11"

#--------------------------
# License settings
#
# We dont want (L)GPL-3.0+
INCOMPATIBLE_LICENSE = "GPLv3.0 GPLv3 GPL-3.0 GPLv3+ LGPLv3.0 LGPLv3 LGPL-3.0 LGPLv3+"
#binutils: used at compilation time
#ccache: used at compilation time
#autoconf: used at compilation time
#gcc: used at compilation time
#mpfr: used at compilation time
#libmpc used at compilation time
OPENSDK_WHITELIST_GPLv3 = "binutils ccache less autoconf gcc mpfr libmpc"
WHITELIST_GPLv3.0+ = "${OPENSDK_WHITELIST_GPLv3}"
WHITELIST_LGPLv3.0 = "${OPENSDK_WHITELIST_GPLv3}"
WHITELIST_GPL-3.0 = "${OPENSDK_WHITELIST_GPLv3}"
WHITELIST_GPL-3.0+ = "${OPENSDK_WHITELIST_GPLv3}"

#--------------------------
# Tune build config display
#
BUILDCFG_VARS += "GCCVERSION"
BUILDCFG_VARS += "PREFERRED_PROVIDER_virtual/kernel"
BUILDCFG_VARS += "PREFERRED_VERSION_${PREFERRED_PROVIDER_virtual/kernel}"

#--------------------------
# Writes build information to target filesystem on /etc/build
#
IMAGE_BUILDINFO_VARS = "${BUILDCFG_VARS}"
INHERIT += "image-buildinfo"
