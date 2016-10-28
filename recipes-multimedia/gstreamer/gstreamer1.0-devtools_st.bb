SUMMARY = "Development and debugging tools for the GStreamer multimedia framework 1.x"

DEPENDS = "gstreamer1.0"
DEPENDS += "gstreamer1.0-plugins-base gtk+3 gdk-pixbuf atk"

LICENSE = "LGPLv2.1"

LIC_FILES_CHKSUM = "file://COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343"

inherit autotools pkgconfig upstream-version-is-even gobject-introspection gtk-doc gettext

PV = "st-1.8.0"
PR = "git${SRCPV}.r0"

SRCBRANCH = "lms-1.8.0"
SRC_URI = "${ST_GIT_SERVER_URI}oeivi/oe/multimedia/gst-devtools${ST_GIT_SERVER_PROTOCOL};branch=${SRCBRANCH}"
SRCREV = "a08806a6ae8f29e3952182840b3f48597a87e483"

#for krogoth
SRC_URI_append = " file://0001-GSTVALIDATE-introspection.m4-prefix-pkgconfig-paths.patch "

S = "${WORKDIR}/git/validate"
B = "${S}"

do_configure_prepend() {
    srcdir=${S} NOCONFIGURE=1 ${S}/autogen.sh
}

do_install_append () {
    install -d ${D}/usr/share/gstreamer-1.0/validate/scenarios
    install -m 0555 -C ${S}/data/scenarios/*.scenario ${D}/usr/share/gstreamer-1.0/validate/scenarios
}

delete_pkg_m4_file() {
	# This m4 file is out of date and is missing PKG_CONFIG_SYSROOT_PATH tweaks which we need for introspection
	rm "${S}/common/m4/pkg.m4" || true
	rm -f "${S}/common/m4/gtk-doc.m4"
}

# gstreamer is not using system-wide makefiles (which we patch in gtkdoc recipe,
# but its own custom ones, which we have to patch here
patch_gtk_doc_makefiles() {
        # Patch the gtk-doc makefiles so that the qemu wrapper is used to run transient binaries
        # instead of libtool wrapper or running them directly
        # Also substitute a bogus plugin scanner, as trying to run the real one is causing issues during build on x86_64.
        sed -i \
           -e "s|GTKDOC_RUN =.*|GTKDOC_RUN = \$(top_builddir)/gtkdoc-qemuwrapper|" \
           -e "s|\$(GTKDOC_EXTRA_ENVIRONMENT)|\$(GTKDOC_EXTRA_ENVIRONMENT) GST_PLUGIN_SCANNER_1_0=\$(top_builddir)/libs/gst/helpers/gst-plugin-scanner-dummy|" \
           ${S}/common/gtk-doc*mak
}

do_configure[prefuncs] += " delete_pkg_m4_file patch_gtk_doc_makefiles"


FILES_${PN} += "${datadir}/gstreamer-1.0/*"
FILES_${PN} += "${libdir}/gstreamer-1.0/*"
FILES_${PN} += "${libdir}/gst-validate-launcher"

FILES_${PN}-dbg += "${libdir}/gstreamer-1.0/validate/.debug"
FILES_${PN}-dbg += "${libdir}/gstreamer-1.0/.debug"

FILES_${PN}-dev += "${libdir}/gstreamer-1.0/*.so"

RDEPENDS_${PN} += "python python-netclient python-io python-stringold python-shell python-subprocess python-fcntl python-misc python-xml"
