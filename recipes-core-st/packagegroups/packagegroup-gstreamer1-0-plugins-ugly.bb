DESCRIPTION = "Gstreamer 1.0 pluggins ugly components"
LICENSE = "LGPLv2+"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/LGPL-2.0;md5=9427b8ccf5cf3df47c29110424c9641a"

PR = "r0"

PACKAGE_ARCH = "${TUNE_PKGARCH}"

inherit packagegroup

PACKAGES = "\
    packagegroup-gstreamer1-0-plugins-ugly \
    packagegroup-gstreamer1-0-plugins-ugly-all \
    "

#---------------------------------------------------
# Information
# Possible configuration for version 1.4.5
# a52dec: a52dec
# cdio: cdio library
# dvdread: dvdread library
# lame: lame mp3 encoder library
# mad: mad mp3 decoder
# mpeg2dec: mpeg2dec
# x264: x264 plug-in
# orc:


# Default configuration used:
# PACKAGECONFIG_pn-gstreamer1.0-plugins-ugly = " \
#    orc a52dec lame mad mpeg2dec \
#    "

# List of package generated (not complete):
#    a52dec: Decodes ATSC A/52 encoded audio streams
#    amrnb: Adaptive Multi-Rate Narrow-Band
#    amrwb: decAdaptive Multi-Rate Wide-Band Decoder
#    asf: Demuxes and muxes audio and video in Microsofts ASF format
#    cdio: Read audio from audio CDs
#    dvdlpcmdec: Decode DVD LPCM frames into standard PCM
#    dvdread: Access a DVD with dvdread
#    dvdsub: DVD subtitle parser and decoder
#    lame: Encode MP3s with LAME
#    mad: mp3 decoding based on the mad library
#    mpeg2dec: LibMpeg2 decoder
#    realmedia: RealMedia support plugins
#    siddec: Uses libsidplay to decode .sid files
#    twolame: Encode MP2s with TwoLAME
#    x264: libx264-based H264 plugins


PROVIDES = "${PACKAGES}"

#The following list of package are sorted by:
# mandatory
# \
# selected by PACKAGECONFIG
# \
# others
RDEPENDS_packagegroup-gstreamer1-0-plugins-ugly = "\
    gstreamer1.0-plugins-ugly-meta \
    \
    gstreamer1.0-plugins-ugly \
    gstreamer1.0-plugins-ugly-mpeg2dec \
"

#The following list of package are sorted by:
# mandatory
# \
# selected by PACKAGECONFIG
# \
# others
RDEPENDS_packagegroup-gstreamer1-0-plugins-good-all = "\
    gstreamer1.0-plugins-ugly-meta \
    \
    gstreamer1.0-plugins-ugly \
    gstreamer1.0-plugins-ugly-mpeg2dec \
    \
    gstreamer1.0-plugins-ugly-asf \
    gstreamer1.0-plugins-ugly-dvdlpcmdec \
    gstreamer1.0-plugins-ugly-dvdsub \
    gstreamer1.0-plugins-ugly-rmdemux \
    gstreamer1.0-plugins-ugly-xingmux \
"

