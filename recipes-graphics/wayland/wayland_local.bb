LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=b31d8f53b6aaf2b4985d7dd7810a70d1"

# 1.9
#SRC_URI = "http://wayland.freedesktop.org/releases/${BPN}-${PV}.tar.xz "
#SRC_URI[md5sum] = "5e141b3f2a7005d6c89d6f233c87c317"
#SRC_URI[sha256sum] = "9c8770720aa0034479735f58a4dc4ca9b172ecfede28f5134312e135b7301efa"


EXTERNALSRC_pn-wayland ?= "${ST_LOCAL_SRC}wayland"

inherit autotools pkgconfig stm-externalsrc

# We need wayland-native for the wayland-scanner utility
BBCLASSEXTEND = "native"

DEPENDS_virtclass-native = "expat-native libffi-native"
DEPENDS = "expat libffi wayland-native"

EXTRA_OECONF_virtclass-native = "--disable-documentation --enable-scanner"
EXTRA_OECONF = "--disable-documentation --with-host-scanner"


# Wayland installs a M4 macro for other projects to use, which uses the target
# pkg-config to find files.  Replace pkg-config with pkg-config-native.
do_install_append_class-native() {
    sed -e 's,PKG_CHECK_MODULES(.*),,g' \
      -e 's,$PKG_CONFIG,pkg-config-native,g' \
      -i ${D}/${datadir}/aclocal/wayland-scanner.m4
    install -m 0444 ${S}/src/wayland-scanner.pc ${D}/${prefix}/lib/pkgconfig/
}

FILES_${PN} = "${libdir}/*${SOLIBS}"
FILES_${PN}-dev += "${bindir} ${datadir}/wayland"