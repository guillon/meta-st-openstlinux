SUMMARY = "ST example of image based on XFCE framework."
LICENSE = "MIT"

inherit core-image distro_features_check extrausers

# let's make sure we have a good image..
CONFLICT_DISTRO_FEATURES = "wayland"
REQUIRED_DISTRO_FEATURES = "x11"

IMAGE_LINGUAS = "en-gb"

IMAGE_FEATURES += "splash package-management ssh-server-dropbear hwcodecs"

# make sure we boot to desktop
# by default and without x11-base in IMAGE_FEATURES we default to multi-user.target
SYSTEMD_DEFAULT_TARGET = "graphical.target"

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
#
IMAGE_XFCE_PART = " \
    packagegroup-xfce-base \
    \
    gnome-bluetooth \
    \
    lxdm \
    xfce4-netload-plugin \
    xfce4-wavelan-plugin \
    xfce4-cpugraph-plugin \
    xfce4-cpufreq-plugin \
    xfce4-systemload-plugin \
    "

IMAGE_X11_PART = " \
    openbox \
    openbox-theme-clearlooks \
    xclock \
    xterm \
    "

#
# INSTALL addons
#
CORE_IMAGE_EXTRA_INSTALL += " \
    packagegroup-core-x11 \
    systemd-networkd-configuration \
    \
    packagegroup-core-boot \
    \
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
    ${IMAGE_X11_PART}		    \
    ${IMAGE_XFCE_PART}		    \
    "

EXTRA_USERS_PARAMS = "\
useradd -p '' st; \
"

