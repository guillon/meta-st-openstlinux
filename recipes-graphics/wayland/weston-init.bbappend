FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
            file://weston.ini \
            file://utilities-terminal.png \
            file://ST_1366x768.png \
            file://weston.sh \
            file://weston_profile.sh \
            "

FILES_${PN} += " ${datadir}/weston \
         ${systemd_system_unitdir}/weston.service \
         ${sbindir}/weston.sh \
         ${sysconfdir}/etc/profile.d \
         ${sysconfdir}/xdg/weston/weston.ini \
         "
CONFFILES_${PN} += "${sysconfdir}/xdg/weston/weston.ini"

do_install_append() {
    install -d ${D}${sysconfdir}/xdg/weston/
    install -d ${D}${datadir}/weston/backgrounds
    install -d ${D}${datadir}/weston/icon

    install -m 0644 ${WORKDIR}/weston.ini ${D}${sysconfdir}/xdg/weston
    install -m 0644 ${WORKDIR}/utilities-terminal.png ${D}${datadir}/weston/icon/utilities-terminal.png
    install -m 0644 ${WORKDIR}/ST_1366x768.png ${D}${datadir}/weston/backgrounds/ST_1366x768.png

    install -d ${D}${systemd_system_unitdir} ${D}${sbindir}
    install -m 0755  ${WORKDIR}/weston.sh ${D}${sbindir}/

    #  install -d ${D}/etc/systemd/system/ ${D}/etc/systemd/system/multi-user.target.wants/
    #  ln -s /lib/systemd/system/weston.service ${D}/etc/systemd/system/multi-user.target.wants/display-manager.service

    install -d ${D}${sysconfdir}/profile.d
    install -m 0755 ${WORKDIR}/weston_profile.sh ${D}${sysconfdir}/profile.d/

    if ${@bb.utils.contains('DISTRO_FEATURES','xwayland','true','false',d)}; then
        # uncomment modules line for support of xwayland
        sed -i -e 's,#modules=xwayland.so,modules=xwayland.so,g' ${D}${sysconfdir}/xdg/weston/weston.ini
    fi
}
