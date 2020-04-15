# Init INITRD_IMAGE to avoid issue with var expansion
INITRD_IMAGE ?= "st-image-resize-initrd"
INITRD_SHORTNAME ?= "uInitrd"

INITRD_DOROOTFS_DEPENDS = "${@'${INITRD_IMAGE}:do_image_complete' if '${INITRD_IMAGE}' else ''}"
do_rootfs[depends] += "${INITRD_DOROOTFS_DEPENDS}"

# Append if required InitRD file
reformat_rootfs_append_stm32mpcommon() {
    if [ -z "${INITRD_IMAGE}" ]; then
        bbnote "No InitRD file set"
    else
        INITRD_IMAGE_FILE=$(find ${DEPLOY_DIR_IMAGE} -name ${INITRD_IMAGE}*-${MACHINE}.${INITRAMFS_FSTYPES})
        if [ -f ${INITRD_IMAGE_FILE} ]; then
            install -m 0644 ${INITRD_IMAGE_FILE} ${IMAGE_ROOTFS}/${INITRD_SHORTNAME}
        else
            bbfatal "cannot find ${INITRD_IMAGE_FILE} file in ${DEPLOY_DIR_IMAGE} folder"
        fi
    fi
}
