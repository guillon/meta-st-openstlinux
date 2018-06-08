FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI_append = "file://0001-Enable-hardware-watchdog-inside-systemd.patch \
        file://0002-logind.conf-ignore-the-poweroff-key.patch \
    "

do_install_append() {
    #Remove this service useless for our needs
    rm -f ${D}/${rootlibexecdir}/systemd/system-generators/systemd-gpt-auto-generator
}
