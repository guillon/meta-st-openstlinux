FILESEXTRAPATHS_append := ":${THISDIR}/files"

SRC_URI_append = " file://generic.recept.json "

do_install_append() {
    install -d ${D}${sysconfdir}/minicoredumper

    install -m 0644 ${WORKDIR}/generic.recept.json ${D}${sysconfdir}/minicoredumper/
}
