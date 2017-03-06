SUMMARY = "OpenSDK weston image with basic Wayland support (if enable in distro)."
LICENSE = "MIT"

inherit core-image distro_features_check

# let's make sure we have a good image..
REQUIRED_DISTRO_FEATURES = "wayland"

IMAGE_LINGUAS = "en-gb"

IMAGE_FEATURES += "splash package-management ssh-server-dropbear hwcodecs tools-profile"

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
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'weston weston-init weston-examples', '', d)} \
    fb-test         \
    libdrm          \
    libdrm-tests    \
    "

#
# Display part addons: X11 via Xwayland
#
IMAGE_X11_XWAYLAND_DISPLAY_PART = " \
    xserver-xorg-xwayland \
    xkbcomp \
    libxcb \
    libxcursor \
    xf86-input-evdev \
    xf86-input-mouse \
    xf86-input-keyboard \
    xterm \
    xinput \
    xeyes \
    xclock \
    xorg-minimal-fonts \
    "

IMAGE_X11_DISPLAY_PART = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11 wayland', '${IMAGE_X11_XWAYLAND_DISPLAY_PART}', '', d)} \
    "

#
# INSTALL addons
#
CORE_IMAGE_EXTRA_INSTALL += " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'bluez5', '', d)} \
    \
    packagegroup-framework-tools-core    \
    packagegroup-framework-tools-kernel  \
    packagegroup-framework-tools-network \
    packagegroup-framework-tools-audio   \
    packagegroup-framework-tools-ui      \
    \
    packagegroup-core-eclipse-debug     \
    \
    ${IMAGE_DISPLAY_PART}               \
    ${IMAGE_MM_PART}                    \
    \
    ${IMAGE_X11_DISPLAY_PART}           \
    "
