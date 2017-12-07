LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${EULA_FILE_ST};md5=${EULA_FILE_ST_MD5SUM}"

image_copy_image_license() {
    cp ${EULA_FILE_ST} ${IMGDEPLOYDIR}/${IMAGE_NAME}.license
    cd ${IMGDEPLOYDIR}; ln -sf ${IMAGE_NAME}.license ${IMAGE_LINK_NAME}.license
}
IMAGE_POSTPROCESS_COMMAND_prepend = "image_copy_image_license;"

