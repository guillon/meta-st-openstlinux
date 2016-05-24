# look for files in the layer first
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://dhcp-client"
SRC_URI += "file://dhclient.service "
SRC_URI += "file://st-dhclient.service "

SYSTEMD_PACKAGES += "dhcp-client"
SYSTEMD_SERVICE_dhcp-client = "dhclient.service"
SYSTEMD_AUTO_ENABLE_dhcp-client = "disable"

SYSTEMD_PACKAGES += "st-dhcp-client"
SYSTEMD_SERVICE_st-dhcp-client = "st-dhclient.service"
SYSTEMD_AUTO_ENABLE_st-dhcp-client = "disable"

PACKAGES += "st-dhcp-client"

FILES_${PN}-client += "/etc/default/dhcp-client"

FILES_st-dhcp-client += "${systemd_unitdir}/system/st-dhclient.service"

do_install_append() {
    install -d ${D}/etc/default/
    install -m 0644 ${WORKDIR}/dhcp-client ${D}/etc/default/
    install -m 0644 ${WORKDIR}/dhclient.service ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/st-dhclient.service ${D}${systemd_unitdir}/system
}
