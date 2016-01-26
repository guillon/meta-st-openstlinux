inherit externalsrc

EXTERNALSRC_pn-dvb-apps ?= "${ST_LOCAL_SRC}dvb-apps"
EXTERNALSRC_BUILD_pn-dvb-apps ?= "${ST_LOCAL_SRC}dvb-apps"

DEPENDS += "virtual/kernel"
CFLAGS_append = " -I${STAGING_KERNEL_DIR}/include/uapi -I${STAGING_KERNEL_DIR}/include "

RDEPENDS_dvb-apps = "\
    libdvbapi \
    libdvbcfg \
    libdvben50221 \
    libdvbsec \
    libesg \
    libucsi \
"
PACKAGES =+ " libdvbapi libdvbcfg libdvben50221 libdvbsec libesg libucsi "

RDEPENDS_dvbtraffic = "libdvbapi"
RDEPENDS_dvbnet = "libdvbapi"
RDEPENDS_dvb-femon = "libdvbapi"
RDEPENDS_dvbdate = "libdvbapi libucsi"
RDEPENDS_dvb-scan = "libdvbapi libdvbsec libdvbcfg"

# Rework do_configure to fix build issue when using external source and building for another machine
# In such case ${STAGING_INCDIR} change and so path has to be updated in 'generate-keynames.sh' script
do_configure() {
    sed -i -e s:[^\ ]*/usr/include:${STAGING_INCDIR}:g util/av7110_loadkeys/generate-keynames.sh
}

do_clean_dvbapps() {
    cd ${S}
    git status --porcelain | grep \?\? | cut -d ' ' -f 2 | xargs rm -rf
}
addtask clean_dvbapps after do_clean before do_cleansstate
