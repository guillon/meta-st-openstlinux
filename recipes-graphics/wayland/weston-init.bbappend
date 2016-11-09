FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
            file://weston.ini \
            file://utilities-terminal.png \
            file://wallpaper_1920x1080_white.png \
            file://weston.sh \
            file://weston_profile.sh \
            file://start_delta.sh \
            "

FILES_${PN} += " ${datadir}/weston \
         ${systemd_system_unitdir}/weston.service \
         ${sbindir}/weston.sh \
         ${sbindir}/start_delta.sh \
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
  install -m 0644 ${WORKDIR}/wallpaper_1920x1080_white.png ${D}${datadir}/weston/backgrounds/wallpaper_1920x1080_white.png

  install -d ${D}${systemd_system_unitdir} ${D}${sbindir}
#  install -m 0644 ${WORKDIR}/weston.service ${D}/lib/systemd/system/
  install -m 0755  ${WORKDIR}/weston.sh ${D}${sbindir}/
  install -m 0755  ${WORKDIR}/start_delta.sh ${D}${sbindir}/

#  install -d ${D}/etc/systemd/system/ ${D}/etc/systemd/system/multi-user.target.wants/
#  ln -s /lib/systemd/system/weston.service ${D}/etc/systemd/system/multi-user.target.wants/display-manager.service

  install -d ${D}${sysconfdir}/profile.d
  install -m 0755 ${WORKDIR}/weston_profile.sh ${D}${sysconfdir}/profile.d/

  if ${@bb.utils.contains('DISTRO_FEATURES','xwayland','true','false',d)}; then
    # uncomment modules line for support of xwayland
    sed -i -e 's,#modules=xwayland.so,modules=xwayland.so,g' ${D}${sysconfdir}/xdg/weston/weston.ini
  fi
}
