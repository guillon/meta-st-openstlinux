FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append = " file://0001-Description-Fix-SIGBUS-on-armhf-when-compiled-with-b.patch "
