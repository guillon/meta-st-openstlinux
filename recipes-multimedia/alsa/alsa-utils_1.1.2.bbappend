PACKAGECONFIG = "udev bat"
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://0001-speaker-test-Fix-chmap-wav-file-selection.patch"

