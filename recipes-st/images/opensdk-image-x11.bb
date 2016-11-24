SUMMARY = "OpenSDK multimedia image based on X11."
LICENSE = "MIT"

inherit core-image distro_features_check

# let's make sure we have a good image..
REQUIRED_DISTRO_FEATURES = "x11"

IMAGE_LINGUAS = "en-gb"

IMAGE_FEATURES += "splash package-management x11-base x11-sato ssh-server-dropbear hwcodecs tools-profile"

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
# Display part addons: X11
#
IMAGE_X11_DISPLAY_PART = " \
    xf86-video-modesetting \
    xkbcomp \
    libxcb \
    libxcursor \
    xf86-input-mouse \
    xf86-input-keyboard \
    xterm \
    xinput \
    xeyes \
    xclock \
    xorg-minimal-fonts \
    xinit \
    \
    encodings \
    font-alias \
    font-util \
    mkfontdir \
    mkfontscale \
    \
    libxkbfile \
    "

#
# Optee part addons
#
IMAGE_OPTEE_PART = " \
    ${@bb.utils.contains('COMBINED_FEATURES', 'optee', 'packagegroup-optee-core', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'optee', 'packagegroup-optee-test', '', d)} \
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
    packagegroup-core-eclipse-debug      \
    \
    packagegroup-core-x11-sato-games     \
    \
    ${IMAGE_DISPLAY_PART}               \
    ${IMAGE_MM_PART}                    \
    \
    ${IMAGE_X11_DISPLAY_PART}           \
    \
    ${IMAGE_OPTEE_PART}                 \
    "
