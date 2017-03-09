FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}/st-1.12:"

SRC_URI_append = " \
    file://0001-ST-compositor-adaptation.patch \
    file://0002-ST-libweston-adaptation.patch \
    file://0003-ST-others.patch \
    file://0004-ST-add-simple-egl-cube-and-slideshow-applications.patch \
    file://0005-Revert-compositor-force-repaint-immediately-on-recei.patch \
    "

# Weston on KMS
PACKAGECONFIG[stkms] = "--enable-st-compositor,--disable-st-compositor,drm udev virtual/mesa mtdev"
# Composition capture
PACKAGECONFIG[capture] = "--enable-display-capture,--disable-display-capture"
