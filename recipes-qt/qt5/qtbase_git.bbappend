FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

#DEPENDS += " libpng"
#PACKAGECONFIG = " \
#    release \
#    dbus udev evdev widgets tools libs \
#    ${@base_contains('DISTRO_FEATURES', 'opengl', 'gles2', '', d)} \
#    jpeg libpng zlib eglfs\
#    ${@base_contains('DISTRO_FEATURES', 'pulseaudio', 'pulseaudio', '', d)} \
#    accessibility \
#    examples \
#"
PACKAGECONFIG_GL = " ${@base_contains('DISTRO_FEATURES', 'opengl', 'gles2', '', d)} "
PACKAGECONFIG_append = " eglfs kms gbm examples "
QT_CONFIG_FLAGS += " -no-sse2 -no-opengles3 -no-mirclient "

SRC_URI += "file://0001-qtbase-Cast-EGL-type-for-build-issue.patch"
