DESCRIPTION = "Wscan is a dvb channel scanner that doesn't require an initial frequency table"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=892f569a555ba9c07a568a7c0c4fa63a"

PV = "20140118"
PR = "r1"
#http://wirbel.htpc-forum.de/w_scan/w_scan-20140118.tar.bz2
SRC_URI = "http://wirbel.htpc-forum.de/w_scan/w_scan-${PV}.tar.bz2"
S = "${WORKDIR}/w_scan-${PV}"

#TARGET_CC_ARCH += "${LDFLAGS}"

inherit autotools

do_configure() {
    oe_runconf
}

do_install() {
    install -d ${D}/${bindir}
    install -m 0755 w_scan ${D}/${bindir}/

#    install -d ${D}/${datadir}/w_scan
#    install -m 0644 *.ids *.classes  ${D}/${datadir}/w_scan/
}

FILES_${PN} += "${datadir}"


SRC_URI[md5sum] = "81f20a802c2e9b397addbd232ca0c352"
SRC_URI[sha256sum] = "dda9d3999eabef99561b9d8ef25af8ec912c30cfff9ba177a1ca7e70e0c28cd7"
