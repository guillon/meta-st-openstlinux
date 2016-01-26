# look for files in the layer first
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SYSTEMD_PACKAGES += "dhcp-client"
SYSTEMD_SERVICE_dhcp-client = "dhclient.service"
SYSTEMD_AUTO_ENABLE_dhcp-client = "disable"

FILES_dhcp-client += "${systemd_unitdir}/system/dhclient.service"
RPROVIDES_dhcp-client += "dhcp-client-systemd"
RREPLACES_dhcp-client += "dhcp-client-systemd"
RCONFLICTS_dhcp-client += "dhcp-client-systemd"

SYSTEMD_PACKAGES += "st-dhcp-client"
SYSTEMD_SERVICE_st-dhcp-client = "st-dhclient.service"
SYSTEMD_AUTO_ENABLE_st-dhcp-client = "disable"

FILES_st-dhcp-client += "${systemd_unitdir}/system/st-dhclient.service"

PACKAGES += "st-dhcp-client"

SRC_URI += "file://dhclient.service "
SRC_URI += "file://st-dhclient.service "

# In case meta-openembedded/meta-systemd layer is used, overrides dhclient.service instruction:
do_install_prepend() {
    if [ -f ${WORKDIR}/dhclient.service ]; then
        sed -i 's/\$INTERFACES/eth0/' ${WORKDIR}/dhclient.service
    else
        die "Cannot find 'dhclient.service' file in ${WORKDIR} to apply expected config."
    fi
}

do_install_append() {
    install -m 0644 ${WORKDIR}/dhclient.service ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/st-dhclient.service ${D}${systemd_unitdir}/system
}
