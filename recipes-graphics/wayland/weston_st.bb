DEFAULT_PREFERENCE = "-1"

SUMMARY = "Weston, a Wayland compositor"
DESCRIPTION = "Weston is the reference implementation of a Wayland compositor"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=d79ee9e66bb0f95d3386a7acae780b70 \
                    file://libweston/compositor.c;endline=23;md5=1d535fed266cf39f6d8c0647f52ac331"

SRCBRANCH = "oe-weston-1_12"
SRC_URI = "${ST_GIT_SERVER_URI}/oeivi/oe/st/weston${ST_GIT_SERVER_PROTOCOL};branch=${SRCBRANCH}"
SRCREV = "4177630a2bc90aa4f0c1cfb073d8143d7d9cf31d"

SRC_URI += "file://weston.desktop"
SRC_URI += "file://weston.png"
SRC_URI += "file://xwayland.weston-start"

PV = "st-1.12.0"
PR = "git${SRCPV}.r0"

S = "${WORKDIR}/git"

inherit autotools pkgconfig useradd distro_features_check
# depends on virtual/egl
REQUIRED_DISTRO_FEATURES = "opengl"

DEPENDS = "libxkbcommon gdk-pixbuf pixman cairo glib-2.0 jpeg libdrm"
DEPENDS += "wayland wayland-protocols libinput virtual/egl pango wayland-native"

EXTRA_OECONF = "--enable-setuid-install \
                --disable-rdp-compositor \
                WAYLAND_PROTOCOLS_SYSROOT_DIR=${STAGING_DIR}/${MACHINE} \
                "
EXTRA_OECONF[vardepsexclude] = "MACHINE"

EXTRA_OECONF_append_qemux86 = "\
		WESTON_NATIVE_BACKEND=fbdev-backend.so \
		"
EXTRA_OECONF_append_qemux86-64 = "\
		WESTON_NATIVE_BACKEND=fbdev-backend.so \
		"
PACKAGECONFIG ??= "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'kms fbdev wayland egl', '', d)} \
                   ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'x11', '', d)} \
                   ${@bb.utils.contains('DISTRO_FEATURES', 'x11 wayland', 'xwayland', '', d)} \
                   ${@bb.utils.contains('DISTRO_FEATURES', 'pam', 'launch', '', d)} \
                   ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd', '', d)} \
                   ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'egl', '', d)} \
                   clients"
#
# Compositor choices
#
# Weston on KMS
PACKAGECONFIG[kms] = "--enable-drm-compositor,--disable-drm-compositor,drm udev virtual/mesa mtdev"
# Weston on KMS
PACKAGECONFIG[stkms] = "--enable-st-compositor,--disable-st-compositor,drm udev virtual/mesa mtdev"
# Composition capture
PACKAGECONFIG[capture] = "--enable-display-capture,--disable-display-capture"
# Weston on Wayland (nested Weston)
PACKAGECONFIG[wayland] = "--enable-wayland-compositor,--disable-wayland-compositor,virtual/mesa"
# Weston on X11
PACKAGECONFIG[x11] = "--enable-x11-compositor,--disable-x11-compositor,virtual/libx11 libxcb libxcb libxcursor cairo"
# Headless Weston
PACKAGECONFIG[headless] = "--enable-headless-compositor,--disable-headless-compositor"
# Weston on framebuffer
PACKAGECONFIG[fbdev] = "--enable-fbdev-compositor,--disable-fbdev-compositor,udev mtdev"
# weston-launch
PACKAGECONFIG[launch] = "--enable-weston-launch,--disable-weston-launch,libpam drm"
# VA-API desktop recorder
PACKAGECONFIG[vaapi] = "--enable-vaapi-recorder,--disable-vaapi-recorder,libva"
# Weston with EGL support
PACKAGECONFIG[egl] = "--enable-egl --enable-simple-egl-clients,--disable-egl --disable-simple-egl-clients,virtual/egl"
# Weston with cairo glesv2 support
PACKAGECONFIG[cairo-glesv2] = "--with-cairo-glesv2,--with-cairo=image,cairo"
# Weston with lcms support
PACKAGECONFIG[lcms] = "--enable-lcms,--disable-lcms,lcms"
# Weston with webp support
PACKAGECONFIG[webp] = "--with-webp,--without-webp,libwebp"
# Weston with unwinding support
PACKAGECONFIG[libunwind] = "--enable-libunwind,--disable-libunwind,libunwind"
# Weston with systemd-login support
PACKAGECONFIG[systemd] = "--enable-systemd-login,--disable-systemd-login,systemd dbus"
# Weston with Xwayland support (requires X11 and Wayland)
PACKAGECONFIG[xwayland] = "--enable-xwayland,--disable-xwayland"
# colord CMS support
PACKAGECONFIG[colord] = "--enable-colord,--disable-colord,colord"
# Clients support
PACKAGECONFIG[clients] = "--enable-clients --enable-simple-clients --enable-demo-clients-install,--disable-clients --disable-simple-clients"

do_install_append() {
	# Weston doesn't need the .la files to load modules, so wipe them
	rm -f ${D}/${libdir}/libweston-*/*.la
	rm -f ${D}/${libdir}/weston/*.la
	rm -f ${D}/${libdir}/*.la

	# If X11, ship a desktop file to launch it
	if [ "${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'x11', '', d)}" = "x11" ]; then
		install -d ${D}${datadir}/applications
		install ${WORKDIR}/weston.desktop ${D}${datadir}/applications

		install -d ${D}${datadir}/icons/hicolor/48x48/apps
		install ${WORKDIR}/weston.png ${D}${datadir}/icons/hicolor/48x48/apps
	fi

	if [ "${@bb.utils.contains('PACKAGECONFIG', 'xwayland', 'yes', 'no', d)}" = "yes" ]; then
		install -Dm 644 ${WORKDIR}/xwayland.weston-start ${D}${datadir}/weston-start/xwayland
	fi
}

PACKAGE_BEFORE_PN += "${@bb.utils.contains('PACKAGECONFIG', 'xwayland', '${PN}-xwayland', '', d)}"
PACKAGES += "${PN}-examples"

FILES_${PN} = "${bindir}/weston ${bindir}/weston-terminal ${bindir}/weston-info ${bindir}/weston-launch ${bindir}/wcap-decode ${libexecdir} ${libdir}/${BPN}/*.so ${libdir}/lib${BPN}-*/*.so ${libdir}/lib${BPN}-*.so* ${datadir}"
FILES_${PN}-dbg += "${libdir}/lib${BPN}-*/.debug/"
FILES_${PN}-examples = "${bindir}/*"

FILES_${PN}-xwayland = "${libdir}/${BPN}/xwayland.so"
RDEPENDS_${PN}-xwayland += "xserver-xorg-xwayland"

RDEPENDS_${PN} += "xkeyboard-config"
RRECOMMENDS_${PN} = "liberation-fonts"
RRECOMMENDS_${PN}-dev += "wayland-protocols"

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM_${PN} = "--system weston-launch"
