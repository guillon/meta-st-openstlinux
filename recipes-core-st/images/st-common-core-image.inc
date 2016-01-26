
IMAGE_INSTALL = " \
    packagegroup-core-boot \
    ${ROOTFS_PKGMANAGE_BOOTSTRAP} \
    ${CORE_IMAGE_EXTRA_INSTALL} \
    grep \
    util-linux \
    procps \
    kbd \
    ${@base_contains('DISTRO_FEATURES', 'systemd', 'dhcp-client st-dhcp-client', '', d)} \
    usbutils \
    pciutils \
    file \
    bc \
    coreutils\
    e2fsprogs \
    sysstat \
    "

#
# System part addons
#
IMAGE_CORE_PART = " \
    apt \
"

IMAGE_LINGUAS = "en-gb"

IMAGE_FEATURES += "package-management ssh-server-dropbear hwcodecs"

LICENSE = "MIT"

inherit core-image

IMAGE_ROOTFS_SIZE ?= "8192"
