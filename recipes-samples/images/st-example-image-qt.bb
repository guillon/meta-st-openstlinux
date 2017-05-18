SUMMARY = "ST example of image based on QT framework."
LICENSE = "MIT"

inherit core-image distro_features_check

# let's make sure we have a good image..
CONFLICT_DISTRO_FEATURES = "x11 wayland"

IMAGE_LINGUAS = "en-gb"

IMAGE_FEATURES += "splash package-management ssh-server-dropbear hwcodecs"

#
# Multimedia part addons
#
IMAGE_MM_PART = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'gstreamer', 'packagegroup-gstreamer1-0', '', d)} \
    tiff \
    libv4l \
    rc-keymaps \
    "

#
# Display part addons
#
IMAGE_DISPLAY_PART = " \
    fb-test         \
    libdrm          \
    libdrm-tests    \
    "

#
# QT part addons
#
IMAGE_QT_PART = " \
    qtbase                  \
    qtbase-plugins          \
    \
    qtdeclarative           \
    qtdeclarative-plugins   \
    qtdeclarative-qmlplugins\
    \
    qtgraphicaleffects-qmlplugins \
    \
    qt3d                    \
    qt3d-qmlplugins         \
    \
    qtscript                \
    \
    qtmultimedia            \
    qtmultimedia-plugins    \
    qtmultimedia-qmlplugins \
    \
    qtsvg                   \
    qtsvg-plugins           \
    \
    qtlocation              \
    qtlocation-qmlplugins   \
    qtlocation-plugins      \
    \
    qt3d-examples           \
    qtbase-examples         \
    qtlocation-examples     \
    \
    qt5nmapcarousedemo      \
    cinematicexperience     \
    qt5everywheredemo       \
    \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'qtwayland', '', d)} \
    "

#
# INSTALL addons
#
CORE_IMAGE_EXTRA_INSTALL += " \
    packagegroup-framework-tools-core	    \
    packagegroup-framework-tools-kernel	    \
    packagegroup-framework-tools-network    \
    packagegroup-framework-tools-audio	    \
    packagegroup-framework-tools-ui	    \
    \
    packagegroup-core-eclipse-debug	    \
    \
    \
    ${IMAGE_DISPLAY_PART}		    \
    ${IMAGE_MM_PART}			    \
    \
    ${IMAGE_QT_PART}			    \
    "
