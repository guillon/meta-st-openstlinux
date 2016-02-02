require opensdk-image-core.bb

SUMMARY = "OpenSDK multimedia image based on Wayland."

inherit distro_features_check

CONFLICT_DISTRO_FEATURES = "x11"
REQUIRED_DISTRO_FEATURES = "wayland"

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
    ${IMAGE_MM_PART} \
    "
