FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

QT_WAYLAND_CONFIG = ""

SRC_URI += "file://0001-qtwayland-Add-cast-to-avoid-build-issue.patch"
