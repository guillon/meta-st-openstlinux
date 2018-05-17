FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

#DEPENDS += " libpng"
#PACKAGECONFIG = " \
#    release \
#    dbus udev evdev widgets tools libs \
#    ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'gles2', '', d)} \
#    jpeg libpng zlib eglfs\
#    ${@bb.utils.contains('DISTRO_FEATURES', 'pulseaudio', 'pulseaudio', '', d)} \
#    accessibility \
#    examples \
#"
PACKAGECONFIG_GL = " ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'gles2', '', d)} "
PACKAGECONFIG_append = " eglfs kms gbm examples fontconfig accessibility "
QT_CONFIG_FLAGS += " -no-sse2 -no-opengles3 -no-mirclient "

