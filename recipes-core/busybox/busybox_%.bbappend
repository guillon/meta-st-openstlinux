FILESEXTRAPATHS_append := ":${THISDIR}/${PN}"

SRC_URI += "file://${BUSYBOX_CONFIG_FRAGMENT}"
SRC_URI += "file://busybox-syslog.default"

BUSYBOX_CONFIG_FRAGMENT = "busybox-opensdk.cfg"

do_configure_append () {
    # merge specific configuration to newly generated .config
	merge_config.sh -m -r -O ${B} ${B}/.config ${BUSYBOX_CONFIG_FRAGMENT} 1>&2
    # make sure to generate proper config file for busybox
    cml1_do_configure
}
