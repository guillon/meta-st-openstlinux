FILESEXTRAPATHS_prepend_stm32mpcommon := "${THISDIR}/${PN}:"

SRC_URI_append_stm32mpcommon = " file://0001-simple-dmabuf-egl-Allow-QueryDmaBufModifiers-to-repo.patch "
SRC_URI_append_stm32mpcommon = " file://0002-clients-drop-simple-dmabuf-drm.patch "
SRC_URI_append_stm32mpcommon = " file://0002-Allow-to-get-hdmi-output-with-several-outputs.patch "
SRC_URI_append_stm32mpcommon = " file://0003-Force-to-close-all-output.patch "

