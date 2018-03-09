SUMMARY = "ST example of image based on GTK framework."
LICENSE = "Proprietary"

include recipes-st/images/st-image.inc

inherit core-image distro_features_check

# let's make sure we have a good image...
REQUIRED_DISTRO_FEATURES = "wayland"

IMAGE_LINGUAS = "en-gb"

IMAGE_FEATURES += " \
    splash \
    package-management \
    ssh-server-dropbear \
    hwcodecs \
    eclipse-debug \
    "

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
    weston-xwayland \
    libxcb-composite0 \
    libxcb-xfixes0 \
    "

IMAGE_X11_DISPLAY_PART = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11 wayland', '${IMAGE_X11_XWAYLAND_DISPLAY_PART}', '', d)} \
    "

#
# GTK part Essentials
#
IMAGE_GTK_MANDATORY_PART = " \
    gtk+3 \
    "

#
# GTK part examples
# florence
IMAGE_GTK_EXAMPLES_PART = " \
    gtk+3-demo \
    kmscube \
    sensors-iks01a2 \
"

#
# Python part examples
IMAGE_PYTHON_PART = " \
    python3-argparse \
    python3-datetime \
    python3-dateutil \
    python3-distutils \
    python3-email \
    python3-enum \
    python3-fcntl \
    python3-importlib \
    python3-io \
    python3-logging \
    python3-misc \
    python3-numbers \
    python3-pycairo \
    python3-pygobject \
    python3-pyparsing \
    python3-re \
    python3-readline \
    python3-shell \
    python3-signal \
    python3-stringold \
    python3-subprocess \
    python3-textutils \
    python3-threading \
    python3-unittest \
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
    packagegroup-framework-tools-core           \
    packagegroup-framework-tools-kernel         \
    packagegroup-framework-tools-network        \
    packagegroup-framework-tools-audio          \
    packagegroup-framework-tools-ui             \
    \
    packagegroup-core-eclipse-debug	            \
    \
    \
    ${IMAGE_DISPLAY_PART}                       \
    ${IMAGE_MM_PART}                            \
    \
    ${IMAGE_X11_DISPLAY_PART}                   \
    \
    ${IMAGE_GTK_MANDATORY_PART}                 \
    ${IMAGE_GTK_EXAMPLES_PART}                  \
    "
