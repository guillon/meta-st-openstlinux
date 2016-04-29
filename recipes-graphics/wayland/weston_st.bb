DEFAULT_PREFERENCE = "-1"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=d79ee9e66bb0f95d3386a7acae780b70 "

inherit autotools pkgconfig useradd

SRCBRANCH = "oe-weston-1_9"
SRC_URI = "${ST_GIT_SERVER_URI}/oeivi/oe/st/weston${ST_GIT_SERVER_PROTOCOL};branch=${SRCBRANCH}"
SRCREV = "e8867a677bf254ce63ef81611f4319a64efeb92c"

S = "${WORKDIR}/git"


DEPENDS = "libxkbcommon gdk-pixbuf pixman libdrm cairo glib-2.0 jpeg"
DEPENDS += "wayland libinput pango"

EXTRA_OECONF = "--enable-setuid-install \
                --disable-xwayland \
                --enable-simple-clients \
                --enable-clients \
                --enable-demo-clients-install \
                --disable-rpi-compositor \
                --disable-rdp-compositor \
                "

#EXTRA_OECONF += "--enable-vaapi-recorder "
#EXTRA_OECONF += "--enable-screen-sharing "

WAYLAND_EXTRA_OECONF ="WAYLAND_SCANNER_CFLAGS=' ' WAYLAND_SCANNER_LIBS=' ' "
EXTRA_OECONF += " ${@base_contains("DISTRO_FEATURES", "wayland", "${WAYLAND_EXTRA_OECONF}", "", d)} "

PACKAGECONFIG ??= "${@base_contains('DISTRO_FEATURES', 'wayland', 'kms wayland', '', d)} \
                   ${@base_contains('DISTRO_FEATURES', 'x11', 'x11', '', d)} \
                   ${@base_contains('DISTRO_FEATURES', 'opengl', 'egl', '', d)} \
                   ${@base_contains('DISTRO_FEATURES', 'pam', 'launch', '', d)} \
                   ${@base_contains('DISTRO_FEATURES', 'systemd', 'systemd', '', d)} \
                  "
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
# Weston with unwinding support
PACKAGECONFIG[libunwind] = "--enable-libunwind,--disable-libunwind,libunwind"

PACKAGECONFIG[systemd] = ",,systemd"

PACKAGES += "${PN}-examples ${PN}-protocol"

FILES_${PN} = "${bindir}/weston ${bindir}/weston-terminal ${bindir}/weston-info ${bindir}/weston-launch ${bindir}/wcap-decode ${libexecdir} ${libdir}/${BPN}/*.so ${datadir}"
FILES_${PN}-examples = "${bindir}/*"
FILES_${PN}-protocol = "/usr/include/weston/protocol/*"


RDEPENDS_${PN} += "xkeyboard-config"
RRECOMMENDS_${PN} = "liberation-fonts"

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM_${PN} = "--system weston-launch"

WESTON_PROTOCOL_TO_EXPORT = " \
    desktop-shell.xml           \
    fullscreen-shell.xml        \
    input-method.xml            \
    ivi-application.xml         \
    ivi-hmi-controller.xml      \
    linux-dmabuf.xml            \
    presentation_timing.xml     \
    scaler.xml                  \
    screenshooter.xml           \
    text-cursor-position.xml    \
    text.xml                    \
    weston-test.xml             \
    workspaces.xml              \
    xdg-shell.xml               \
    "

WAYLAND_SCANNER = "${STAGING_DIR_NATIVE}/usr/bin/wayland-scanner"

do_install_append() {
    # Weston doesn't need the .la files to load modules, so wipe them
    rm -f ${D}/${libdir}/weston/*.la

    install -d ${D}/usr/include/weston/protocol/
    for PROTO in ${WESTON_PROTOCOL_TO_EXPORT};
    do
        install -m 0666 ${S}/protocol/$PROTO ${D}/usr/include/weston/protocol/
        PROTO_NAME=`basename $PROTO .xml`
        ${WAYLAND_SCANNER} client-header < ${S}/protocol/${PROTO} > ${D}/usr/include/weston/protocol/${PROTO_NAME}-client-protocol.h
        ${WAYLAND_SCANNER} code < ${S}/protocol/${PROTO} > ${D}/usr/include/weston/protocol/${PROTO_NAME}-protocol.c
    done
}


