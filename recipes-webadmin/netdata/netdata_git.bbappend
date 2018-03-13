FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}/:"

SRC_URI_append = " file://stm32.html "
SRC_URI_append = " file://python.d.conf "
SRC_URI_append = " file://kill_netdata "


do_install_append() {
    install -d ${D}${sysconfdir}/netdata
    install -d ${D}${datadir}/netdata/web
    install -d ${D}${bindir}/netdata/web

    install -m 0644 ${WORKDIR}/stm32.html ${D}${datadir}/netdata/web/
    install -m 0644 ${WORKDIR}/python.d.conf ${D}${sysconfdir}/netdata/
    install -m 0755 ${WORKDIR}/kill_netdata ${D}${bindir}/netdata/

}
