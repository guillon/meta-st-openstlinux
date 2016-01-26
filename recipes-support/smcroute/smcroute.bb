SUMMARY = "A static multicast route tool"
DESCRIPTION = "SMCRoute is a command line tool to manipulate the multicast routes"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=4325afd396febcb659c36b49533135d4"

SRCREV = "d6280e64b27d5a4bd7f37dac36b455f4ae5f9ab3"

PV = "1.0+git${SRCPV}"

SRC_URI = "git://github.com/troglobit/smcroute.git"

S = "${WORKDIR}/git"
inherit autotools



FILES_${PN} = "${bindir}/smcroute"

do_install () {
    install --mode=0755 -d ${D}/${bindir}
    install --mode=0755 ${B}/smcroute ${D}/${bindir}
}


