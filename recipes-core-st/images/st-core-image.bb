DESCRIPTION = "OpenSDK small image just capable of allowing a device to boot with UI"

PV = "1.0.0"
PR = "r1"

IMAGE_INSTALL_append = " \
    fb-test \
    weston \
    weston-cfg \
"

include recipes-core-st/images/st-common-core-image.inc
