DESCRIPTION = "OpenSDK image."

PV = "1.0.0"
PR = "r1"

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
# Display part addons: wayland
#
IMAGE_WAYLAND_DISPLAY_PART = " \
    weston \
    weston-cfg \
"

#
# Display part addons
#
IMAGE_DISPLAY_PART = " \
   ${@base_contains('DISTRO_FEATURES', 'wayland', '${IMAGE_WAYLAND_DISPLAY_PART}', '', d)} \
   ${@base_contains('DISTRO_FEATURES', 'x11', '${IMAGE_X11_DISPLAY_PART}', '', d)} \
    fb-test \
    libdrm \
    libdrm-tests \
"

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
    ${IMAGE_CORE_PART} \
    ${IMAGE_DISPLAY_PART} \
    ${@base_contains('DISTRO_FEATURES', 'alsa', '${IMAGE_AUDIO_ALSA_PART}', '', d)} \
    ${IMAGE_MM_PART} \
"

include recipes-core-st/images/st-common-core-image.inc

