FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

DEPENDS += " udev "
SRC_URI_append = " file://0003-drm-automatic-driver-name-detection.patch \
		   file://0001-NativeStateDRM-add-stm-driver-in-module-list.patch \
		   file://0001-NativeStateDRM-Set-a-default-encoder-for-not-connect.patch "

PACKAGECONFIG = "${@bb.utils.contains('DISTRO_FEATURES', 'x11 opengl', 'x11-gl x11-gles2', '', d)} \
                 ${@bb.utils.contains('DISTRO_FEATURES', 'wayland opengl', 'wayland-gles2', '', d)} \
                 drm-gles2"
