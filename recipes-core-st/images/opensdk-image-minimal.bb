SUMMARY = "OpenSDK small image just capable of allowing the target device hardware to boot."

LICENSE = "MIT"

inherit core-image

IMAGE_INSTALL = " \
    packagegroup-core-boot \
    ${ROOTFS_PKGMANAGE_BOOTSTRAP} \
    ${CORE_IMAGE_EXTRA_INSTALL} \
    "

IMAGE_LINGUAS = "en-gb"
