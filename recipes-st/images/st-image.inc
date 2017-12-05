LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${EULA_FILE_ST};md5=53c833ad0ab25e1ff2245e8a16f62355"

image_copy_image_license() {
    cp ${EULA_FILE_ST} ${IMGDEPLOYDIR}/${IMAGE_NAME}.license
    cd ${IMGDEPLOYDIR}; ln -s ${IMAGE_NAME}.license ${IMAGE_LINK_NAME}.license
}
IMAGE_POSTPROCESS_COMMAND_prepend = "image_copy_image_license;"

