LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${EULA_FILE_ST};md5=${EULA_FILE_ST_MD5SUM}"

inherit st-image-license-summary
image_copy_image_license() {
    if [ -n ${EULA_FILE_ST} ]; then
        cp ${EULA_FILE_ST} ${IMGDEPLOYDIR}/${IMAGE_NAME}.license
        cd ${IMGDEPLOYDIR}; ln -sf ${IMAGE_NAME}.license ${IMAGE_LINK_NAME}.license
    else
        bbwarn "Missing 'EULA_FILE_ST' var : no image license copied..."
    fi
}
IMAGE_POSTPROCESS_COMMAND_prepend = "image_copy_image_license;"

