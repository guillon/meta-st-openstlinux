require opensdk-image-core.bb

SUMMARY = "OpenSDK multimedia image based on Wayland."

#
# Audio part addons
#
IMAGE_AUDIO_ALSA_PART = " \
    alsa-lib \
    alsa-utils \
    alsa-plugins \
    pulseaudio \
    pulseaudio-server \
    pulseaudio-misc \
    pulseaudio-module-combine-sink \
    "

#
# Display part addons
#
IMAGE_DISPLAY_DRM_PART = " \
    libdrm \
    libdrm-tests \
    "

#
# Display part addons: X11
#
IMAGE_X11_SERVER_DISPLAY_PART = " \
    x11-common \
    xf86-input-evdev \
    xf86-input-mouse \
    xf86-input-keyboard \
    xf86-video-modesetting \
    xauth \
    xterm \
    xorg-minimal-fonts \
    xkbcomp \
    xinput \
    xinit \
    xeyes \
    xclock \
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
    "
IMAGE_X11_DISPLAY_PART = "${@bb.utils.contains('DISTRO_FEATURES', 'xwayland', '${IMAGE_X11_XWAYLAND_DISPLAY_PART}', '${IMAGE_X11_SERVER_DISPLAY_PART}', d)}"
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
# INSTALL addons
#
CORE_IMAGE_EXTRA_INSTALL += " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'alsa', '${IMAGE_AUDIO_ALSA_PART}', '', d)} \
    ${IMAGE_DISPLAY_DRM_PART} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11', '${IMAGE_X11_DISPLAY_PART}', '', d)} \
    ${IMAGE_MM_PART} \
    "
