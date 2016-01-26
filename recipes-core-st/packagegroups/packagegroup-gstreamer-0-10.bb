DESCRIPTION = "Gstreamer 0.10 components"
LICENSE = "LGPLv2+"
#LIC_FILES_CHKSUM = "file://${ST_LOCAL_SRC}/gstreamer/COPYING;md5=6762ed442b3822387a51c92d928ead0d"

PR = "r0"

PACKAGE_ARCH = "${TUNE_PKGARCH}"

inherit packagegroup

PACKAGES = "\
    packagegroup-gstreamer-0-10 \
    packagegroup-gstreamer-0-10-base \
    packagegroup-gstreamer-0-10-good \
    packagegroup-gstreamer-0-10-ugly \
    packagegroup-gstreamer-0-10-bad \
    "

PROVIDES = "${PACKAGES}"
RDEPENDS_packagegroup-gstreamer-0-10 = "\
    libsoup-2.4 \
    librsvg \
    libcroco \
    gst-ffmpeg \
    gst-fluendo-mp3 \
    gstreamer \
"

RDEPENDS_packagegroup-gstreamer-0-10-base = "\
    gst-plugins-base \
    gst-plugins-base-playbin \
    gst-plugins-base-alsa \
    gst-plugins-base-audioconvert \
    gst-plugins-base-audiorate \
    gst-plugins-base-adder \
    gst-plugins-base-apps \
    gst-plugins-base-audioresample \
    gst-plugins-base-decodebin \
    gst-plugins-base-decodebin2 \
    gst-plugins-base-encodebin \
    gst-plugins-base-ffmpegcolorspace \
    gst-plugins-base-gdp \
    gst-plugins-base-gio \
    gst-plugins-base-ivorbisdec \
    gst-plugins-base-meta \
    gst-plugins-base-ogg \
    gst-plugins-base-subparse \
    gst-plugins-base-tcp \
    gst-plugins-base-theora \
    gst-plugins-base-typefindfunctions \
    gst-plugins-base-videorate \
    gst-plugins-base-videoscale \
    gst-plugins-base-videotestsrc \
    gst-plugins-base-volume \
    gst-plugins-base-vorbis \
    gst-plugins-base-ximagesink \
    gst-plugins-base-xvimagesink \
"

RDEPENDS_packagegroup-gstreamer-0-10-good = "\
    gst-plugins-good \
    gst-plugins-good-alaw \
    gst-plugins-good-alpha \
    gst-plugins-good-alphacolor \
    gst-plugins-good-annodex \
    gst-plugins-good-apetag \
    gst-plugins-good-audiofx \
    gst-plugins-good-audioparsers \
    gst-plugins-good-auparse \
    gst-plugins-good-autodetect \
    gst-plugins-good-avi \
    gst-plugins-good-cairo \
    gst-plugins-good-cutter \
    gst-plugins-good-deinterlace \
    gst-plugins-good-efence \
    gst-plugins-good-effectv \
    gst-plugins-good-equalizer \
    gst-plugins-good-flac \
    gst-plugins-good-flv \
    gst-plugins-good-flxdec \
    gst-plugins-good-goom \
    gst-plugins-good-goom2k1 \
    gst-plugins-good-icydemux \
    gst-plugins-good-id3demux \
    gst-plugins-good-imagefreeze \
    gst-plugins-good-interleave \
    gst-plugins-good-isomp4 \
    gst-plugins-good-jpeg \
    gst-plugins-good-level \
    gst-plugins-good-matroska \
    gst-plugins-good-meta \
    gst-plugins-good-mulaw \
    gst-plugins-good-multifile \
    gst-plugins-good-multipart \
    gst-plugins-good-navigationtest \
    gst-plugins-good-oss4audio \
    gst-plugins-good-ossaudio \
    gst-plugins-good-png \
    gst-plugins-good-pulse \
    gst-plugins-good-replaygain \
    gst-plugins-good-rtp \
    gst-plugins-good-rtpmanager \
    gst-plugins-good-rtsp \
    gst-plugins-good-shapewipe \
    gst-plugins-good-smpte \
    gst-plugins-good-spectrum \
    gst-plugins-good-speex \
    gst-plugins-good-udp \
    gst-plugins-good-video4linux2 \
    gst-plugins-good-videobox \
    gst-plugins-good-videocrop \
    gst-plugins-good-videofilter \
    gst-plugins-good-videomixer \
    gst-plugins-good-wavenc \
    gst-plugins-good-wavparse \
    gst-plugins-good-ximagesrc \
    gst-plugins-good-y4menc \
"

RDEPENDS_packagegroup-gstreamer-0-10-ugly = "\
    gst-plugins-ugly \
    gst-plugins-ugly-a52dec \
    gst-plugins-ugly-asf \
    gst-plugins-ugly-lame \
    gst-plugins-ugly-mad \
    gst-plugins-ugly-meta \
    gst-plugins-ugly-mpeg2dec \
    gst-plugins-ugly-mpegaudioparse \
    gst-plugins-ugly-mpegstream \
    gst-plugins-ugly-rmdemux \
"

RDEPENDS_packagegroup-gstreamer-0-10-bad = "\
    gst-plugins-bad \
    gst-plugins-bad-adpcmdec \
    gst-plugins-bad-adpcmenc \
    gst-plugins-bad-aiff \
    gst-plugins-bad-asfmux \
    gst-plugins-bad-audiovisualizers \
    gst-plugins-bad-autoconvert \
    gst-plugins-bad-bayer \
    gst-plugins-bad-bz2 \
    gst-plugins-bad-camerabin \
    gst-plugins-bad-camerabin2 \
    gst-plugins-bad-cdxaparse \
    gst-plugins-bad-coloreffects \
    gst-plugins-bad-colorspace \
    gst-plugins-bad-curl \
    gst-plugins-bad-dataurisrc \
    gst-plugins-bad-dccp \
    gst-plugins-bad-debugutilsbad \
    gst-plugins-bad-decklink \
    gst-plugins-bad-doc \
    gst-plugins-bad-dtmf \
    gst-plugins-bad-dvb \
    gst-plugins-bad-dvbsuboverlay \
    gst-plugins-bad-dvdspu \
    gst-plugins-bad-faceoverlay \
    gst-plugins-bad-festival \
    gst-plugins-bad-fieldanalysis \
    gst-plugins-bad-fragmented \
    gst-plugins-bad-freeverb \
    gst-plugins-bad-freeze \
    gst-plugins-bad-frei0r \
    gst-plugins-bad-gaudieffects \
    gst-plugins-bad-geometrictransform \
    gst-plugins-bad-gsettingselements \
    gst-plugins-bad-h264parse \
    gst-plugins-bad-hdvparse \
    gst-plugins-bad-id3tag \
    gst-plugins-bad-inter \
    gst-plugins-bad-interlace \
    gst-plugins-bad-ivfparse \
    gst-plugins-bad-jp2kdecimator \
    gst-plugins-bad-jpegformat \
    gst-plugins-bad-legacyresample \
    gst-plugins-bad-linsys \
    gst-plugins-bad-liveadder \
    gst-plugins-bad-meta \
    gst-plugins-bad-mpegdemux \
    gst-plugins-bad-mpegpsmux \
    gst-plugins-bad-mpegtsdemux \
    gst-plugins-bad-mpegtsmux \
    gst-plugins-bad-mpegvideoparse \
    gst-plugins-bad-mve \
    gst-plugins-bad-mxf \
    gst-plugins-bad-nsf \
    gst-plugins-bad-nuvdemux \
    gst-plugins-bad-patchdetect \
    gst-plugins-bad-pcapparse \
    gst-plugins-bad-pnm \
    gst-plugins-bad-rawparse \
    gst-plugins-bad-removesilence \
    gst-plugins-bad-rfbsrc \
    gst-plugins-bad-rsvg \
    gst-plugins-bad-rtpmux \
    gst-plugins-bad-rtpvp8 \
    gst-plugins-bad-scaletempoplugin \
    gst-plugins-bad-sdi \
    gst-plugins-bad-sdpelem \
    gst-plugins-bad-segmentclip \
    gst-plugins-bad-shm \
    gst-plugins-bad-siren \
    gst-plugins-bad-smooth \
    gst-plugins-bad-speed \
    gst-plugins-bad-stereo \
    gst-plugins-bad-subenc \
    gst-plugins-bad-tta \
    gst-plugins-bad-vcdsrc \
    gst-plugins-bad-videofiltersbad \
    gst-plugins-bad-videomaxrate \
    gst-plugins-bad-videomeasure \
    gst-plugins-bad-videoparsersbad \
    gst-plugins-bad-videosignal \
    gst-plugins-bad-vmnc \
    gst-plugins-bad-y4mdec \
"
