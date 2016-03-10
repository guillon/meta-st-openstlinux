DESCRIPTION = "simple zapping tool for the Linux DVB S2 API"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${ST_LOCAL_SRC}szap-s2/README;md5=a3d05bdcbaa2eb6db1715b65b7d2bef2"

PR = "r0"
PV = "1.0"

inherit stm-externalsrc

EXTERNALSRC_pn-szap-s2 ?= "${ST_LOCAL_SRC}szap-s2"


FILES_${PN} = "${bindir}/szap-s2"

do_compile () {
  oe_runmake -C ${S} OUTPUT_DIR=${B}
}


do_install () {
    install --mode=0755 -d ${D}/${bindir}
    install --mode=0755 ${B}/szap-s2 ${D}/${bindir}
}


