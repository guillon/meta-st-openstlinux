require opensdk-image-mm.bb

SUMMARY = "OpenSDK multimedia image with X11 support (No Wayland)."

CONFLICT_DISTRO_FEATURES = "wayland"
REQUIRED_DISTRO_FEATURES = "x11"

#
# Display part addons: X11
#
IMAGE_X11_DISPLAY_PART = " \
    x11-common \
    xf86-input-evdev \
    xf86-input-mouse \
    xf86-input-keyboard \
    xf86-video-armsoc \
    xauth \
    xeyes \
    xclock \
    xterm \
    xorg-minimal-fonts \
    xkbcomp \
    xinput \
    xinit \
    "

#
# INSTALL addons
#
CORE_IMAGE_EXTRA_INSTALL += " \
    ${IMAGE_X11_DISPLAY_PART} \
    "
