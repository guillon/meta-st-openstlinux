SUMMARY = "ST example of image based on XFCE framework."
LICENSE = "MIT"

inherit core-image distro_features_check extrausers

# let's make sure we have a good image..
CONFLICT_DISTRO_FEATURES = "wayland"
REQUIRED_DISTRO_FEATURES = "x11"

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
# XFCE part addons
#
IMAGE_XFCE_PART = " \
    lxdm \
    xfce4-netload-plugin \
    xfce4-cpugraph-plugin \
    xfce4-cpufreq-plugin \
    xclock \
    xterm \
    \
    gnome-bluetooth \
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

EXTRA_USERS_PARAMS = "\
useradd -p '' st; \
"

