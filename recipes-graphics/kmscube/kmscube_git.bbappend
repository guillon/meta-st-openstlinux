FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append += " file://0001-fix-gbm_bo_map-detection.patch "
SRC_URI_append += " file://0001-Disable-Texturator.patch "
