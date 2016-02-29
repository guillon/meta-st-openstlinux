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
# Multimedia part addons
#
IMAGE_MM_PART = " \
    ${@base_contains('DISTRO_FEATURES', 'gstreamer', 'packagegroup-gstreamer1-0', '', d)} \
    tiff \
    libv4l \
    rc-keymaps \
    "

#
# INSTALL addons
#
CORE_IMAGE_EXTRA_INSTALL += " \
    ${@base_contains('DISTRO_FEATURES', 'alsa', '${IMAGE_AUDIO_ALSA_PART}', '', d)} \
    ${IMAGE_DISPLAY_DRM_PART} \
    ${@base_contains('DISTRO_FEATURES', 'x11', '${IMAGE_X11_DISPLAY_PART}', '', d)} \
    ${IMAGE_MM_PART} \
    "
