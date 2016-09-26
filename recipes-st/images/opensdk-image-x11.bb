SUMMARY = "OpenSDK multimedia image based on X11."
#based on core-image-sato

PV = "1.0.0"
PR = "r2"

LICENSE = "MIT"

IMAGE_FEATURES += "splash package-management x11-base x11-sato ssh-server-dropbear hwcodecs"

require opensdk-image-core.bb

IMAGE_INSTALL += "packagegroup-core-x11-sato-games"

#
# Display part addons
#
IMAGE_INSTALL +=" \
    libdrm \
    libdrm-tests \
"

#
# Network part
#
IMAGE_INSTALL += "\
    wireless-tools \
    libnl \
    wpa-supplicant \
    connman \
    connman-client \
    hostapd \
"

# X11
IMAGE_INSTALL += " \
    xterm \
    xeyes \
    xclock \
    \
    encodings \
    font-alias \
    font-util \
    mkfontdir \
    mkfontscale \
    \
    libxkbfile \
    \
    pcmanfm \
    "

#
# Audio part addons
#
IMAGE_INSTALL += " \
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
    ${@bb.utils.contains('DISTRO_FEATURES', 'gstreamer', 'packagegroup-gstreamer1-0', '', d)} \
    tiff \
    libv4l \
    rc-keymaps \
    "

# others
#
IMAGE_INSTALL += " \
    grep \
    util-linux \
    procps \
    kbd \
    dhcp-client \
    usbutils \
    pciutils \
    file \
    bc \
    coreutils\
    e2fsprogs \
    e2fsprogs-resize2fs \
    e2fsprogs-mke2fs \
    sysstat \
    minicom \
    \
    gptfdisk \
"

