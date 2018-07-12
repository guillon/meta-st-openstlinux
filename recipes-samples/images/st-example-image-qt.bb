SUMMARY = "ST example of image based on QT framework."
LICENSE = "Proprietary"

include recipes-st/images/st-image.inc

inherit core-image distro_features_check

# let's make sure we have a good image..
CONFLICT_DISTRO_FEATURES = "x11 wayland"

IMAGE_LINGUAS = "en-gb"

IMAGE_FEATURES += "splash package-management ssh-server-dropbear hwcodecs"

# Define to null ROOTFS_MAXSIZE to avoid partition size restriction
IMAGE_ROOTFS_MAXSIZE = ""

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
# QT part Essentials
#
IMAGE_QT_MANDATORY_PART = " \
   qtbase                  \
   liberation-fonts        \
   qtbase-plugins          \
   qtbase-tools            \
   \
   qtdeclarative           \
   qtdeclarative-qmlplugins\
   qtdeclarative-tools     \
   \
   qtgraphicaleffects-qmlplugins \
   \
   qtmultimedia            \
   qtmultimedia-plugins    \
   qtmultimedia-qmlplugins \
   \
   qtscript                \
   \
   ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'qtwayland', '', d)} \
   ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'qtwayland-plugins', '', d)} \
   \
   openstlinux-qt-eglfs \
   "

#
# QT part add-ons
#
IMAGE_QT_OPTIONAL_PART = " \
   qtcanvas3d \
   \
   qt3d                    \
   qt3d-qmlplugins         \
   \
   qtsvg                   \
   qtsvg-plugins           \
   \
   qtlocation              \
   qtlocation-qmlplugins   \
   qtlocation-plugins      \
   \
   qtwebkit                \
   \
   qtquick1                \
   \
   qtquickcontrols         \
   qtquickcontrols-qmlplugins \
   qtquickcontrols2         \
   qtquickcontrols2-qmlplugins\
   qtscript                \
   \
   qtsensors               \
   qtserialport            \
   \
   qtcharts                \
   qtcharts-qmlplugins     \
   \
   qtlocation              \
   qtlocation-plugins      \
   qtlocation-qmlplugins   \
   "

#
# QT part examples
#
IMAGE_QT_EXAMPLES_PART = " \
   qtcanvas3d-examples \
   qtbase-examples         \
   qtdeclarative-examples  \
   \
   qtmultimedia-examples   \
   \
   ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'qtwayland-examples', '', d)} \
   \
   qtwebkit-examples       \
   \
   qtquickcontrols-examples \
   qtscript-examples       \
   \
   qtsensors-examples      \
   qtserialport-examples   \
   \
   qtcharts-examples       \
   \
   qt3d-examples           \
   \
   qtlocation-examples     \
   \
   qt5nmapcarousedemo      \
   cinematicexperience     \
   qtsmarthome             \
   "

# INSTALL addons
#
CORE_IMAGE_EXTRA_INSTALL += " \
    systemd-networkd-configuration \
    \
    packagegroup-framework-tools-core-base      \
    packagegroup-framework-tools-kernel-base    \
    packagegroup-framework-tools-network-base   \
    packagegroup-framework-tools-audio-base     \
    packagegroup-framework-tools-ui-base        \
    \
    packagegroup-framework-tools-core       \
    packagegroup-framework-tools-kernel     \
    packagegroup-framework-tools-network    \
    packagegroup-framework-tools-audio      \
    packagegroup-framework-tools-ui         \
    \
    packagegroup-core-eclipse-debug         \
    \
    \
    ${IMAGE_DISPLAY_PART}       \
    ${IMAGE_MM_PART}            \
    \
    ${IMAGE_QT_MANDATORY_PART}  \
    ${IMAGE_QT_OPTIONAL_PART}   \
    ${IMAGE_QT_EXAMPLES_PART}   \
    "
