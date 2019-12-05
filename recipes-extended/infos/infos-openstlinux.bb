SUMMARY = "Information for support"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

PV="1.1"

do_install() {
	if [ "${TARGET_ARCH}" = "arm" ]; then
		mkdir -p ${D}${sysconfdir}/st-info.d
		touch ${D}${sysconfdir}/st-info.d/graphics-${PV}
		printf "LIBGLES1=${PREFERRED_PROVIDER_virtual/libgles1}\n" > ${D}${sysconfdir}/st-info.d/graphics-${PV}
	fi
}
FILES_${PN} = "${sysconfdir}/st-info.d"
ALLOW_EMPTY_${PN} = "1"
