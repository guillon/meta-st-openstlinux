# Copyright (C) 2018, STMicroelectronics - All Rights Reserved

SUMMARY = "Python script which launch several use-cases"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${OPENSTLINUX_BASE}/files/licenses/ST-Proprietary;md5=7cb1e55a9556c7dd1a3cae09db9cc85f"

DEPENDS += "demo-hotspot-wifi"
DEPENDS += "ai-hand-char-reco-launcher"

# Needed to update dynamic library name in elf file
DEPENDS += "patchelf-native"

SRC_URI = " \
    file://demo_launcher.py \
    file://start_up_demo_launcher.sh \
    file://bin \
    file://pictures \
    file://media \
    "

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${prefix}/local/demo/
    install -d ${D}${prefix}/local/demo/bin
    install -d ${D}${prefix}/local/demo/pictures
    install -d ${D}${prefix}/local/demo/media
    install -m 0755 ${WORKDIR}/demo_launcher.py ${D}${prefix}/local/demo/
    install -m 0755 ${WORKDIR}/bin/* ${D}${prefix}/local/demo/bin
    install -m 0644 ${WORKDIR}/pictures/* ${D}${prefix}/local/demo/pictures/
    install -m 0644 ${WORKDIR}/media/* ${D}${prefix}/local/demo/media/

    # start at startup
    install -d ${D}${prefix}/local/weston-start-at-startup/
    install -m 0755 ${WORKDIR}/start_up_demo_launcher.sh ${D}${prefix}/local/weston-start-at-startup/

    # Fix wrong library name in bin file
    if [ ${PREFERRED_PROVIDER_virtual/egl} = "mesa" ]; then
        patchelf --replace-needed libEGL.so libEGL.so.1 ${D}${prefix}/local/demo/bin/weston-simple-st-egl-tex
    fi
    if [ ${PREFERRED_PROVIDER_virtual/libgles2} = "mesa" ]; then
        patchelf --replace-needed libGLESv2.so libGLESv2.so.2 ${D}${prefix}/local/demo/bin/weston-simple-st-egl-tex
    fi
}

FILES_${PN} += "${prefix}/local/demo/ ${prefix}/local/weston-start-at-startup/"

RDEPENDS_${PN} += "python3 python3-pygobject gtk+3 gstreamer1.0-plugins-base python3-ptyprocess python3-pexpect python3-terminal python3-resource"
