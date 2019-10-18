do_install_append() {
    sed -i -e 's#/usr/lib/#/usr/libexec/#g' ${D}${systemd_unitdir}/system/sysstat.service
}
