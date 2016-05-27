SUMMARY = "Weston configuration file"
DESCRIPTION = "Weston configuration file installation"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420 "

SRC_URI = " file://weston.ini \
            file://utilities-terminal.png \
            file://wallpaper_1920x1080_white.png "

SRC_URI += " file://weston.service \
             file://weston.sh \
             file://weston_profile.sh "

FILES_${PN} = "\
    /tmp/xdg_runtime_dir \
    /home/root/.config/weston.ini \
    /usr/share/weston/icon/utilities-terminal.png \
    /usr/share/weston/backgrounds/wallpaper_1920x1080_white.png "

FILES_${PN} += "/lib/systemd/system/weston.service \
            /usr/sbin/weston.sh \
            /etc/systemd/system/multi-user.target.wants/display-manager.service \
            /etc/profile.d/"

do_install() {
  install -d 0700 ${D}/tmp/xdg_runtime_dir
  install -d ${D}/home/root/.config
  install -d ${D}/usr/share/weston/backgrounds
  install -d ${D}/usr/share/weston/icon
  install -m 0644 ${WORKDIR}/weston.ini ${D}/home/root/.config/weston.ini
  install -m 0644 ${WORKDIR}/utilities-terminal.png ${D}/usr/share/weston/icon/utilities-terminal.png
  install -m 0644 ${WORKDIR}/wallpaper_1920x1080_white.png ${D}/usr/share/weston/backgrounds/wallpaper_1920x1080_white.png

  install -d ${D}/lib/systemd/system/ ${D}/usr/sbin
  install -m 0644 ${WORKDIR}/weston.service ${D}/lib/systemd/system/
  install -m 0755  ${WORKDIR}/weston.sh ${D}/usr/sbin

  install -d ${D}/etc/systemd/system/ ${D}/etc/systemd/system/multi-user.target.wants/
  ln -s /lib/systemd/system/weston.service ${D}/etc/systemd/system/multi-user.target.wants/display-manager.service

  install -d ${D}/etc/profile.d
  install -m 0755 ${WORKDIR}/weston_profile.sh ${D}/etc/profile.d/

  if ${@bb.utils.contains('DISTRO_FEATURES','xwayland','true','false',d)}; then
    # uncomment modules line for support of xwayland
    sed -i -e 's,#modules=xwayland.so,modules=xwayland.so,g' ${D}/home/root/.config/weston.ini
  fi
}
