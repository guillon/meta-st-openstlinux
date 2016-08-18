SUMMARY = "OpenSDK core image with basic Wayland support (if enable in distro)."
LICENSE = "MIT"

inherit core-image

IMAGE_LINGUAS = "en-gb"

IMAGE_FEATURES += "splash package-management ssh-server-dropbear hwcodecs"

#
# System Core part addons
#
IMAGE_CORE_PART = " \
    grep \
    util-linux \
    procps \
    kbd \
    ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'dhcp-client st-dhcp-client', '', d)} \
    usbutils \
    pciutils \
    file \
    bc \
    coreutils\
    e2fsprogs \
    sysstat \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'bluez5', '', d)} \
    "

#
# Display part addons
#
IMAGE_DISPLAY_PART = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'weston weston-cfg weston-examples', '', d)} \
    fb-test \
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
    ${IMAGE_CORE_PART} \
    ${IMAGE_DISPLAY_PART} \
    ${IMAGE_OPTEE_PART} \
    "
