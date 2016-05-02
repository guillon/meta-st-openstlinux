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
