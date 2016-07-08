require opensdk-image-tools.bb

SUMMARY = "OpenSDK tools image plus dev tools and modules (i.e. opensdk-image-tools + dev)."

#
# Dev-tools part addons
#
IMAGE_DEVTOOLS_PART += " \
    gstreamer1.0-devtools \
    v4l-utils \
    smcroute \
    "

#
# dvb-apps part addons
#
IMAGE_DVB-APPS_PART += " \
    dvb-apps \
    dvbapp-tests \
    dvb-azap \
    dvb-czap \
    dvbdate \
    dvb-femon \
    dvbnet \
    dvb-scan \
    dvb-szap \
    dvbtraffic \
    dvb-tzap \
    "

#
# Networking part addons
#
IMAGE_NETWORKING_PART += " \
    net-snmp-client \
    net-snmp-libs \
    net-snmp-mibs \
    net-snmp-server \
    net-snmp-server-snmpd \
    net-snmp-server-snmptrapd \
    "

#
# INSTALL addons
#
CORE_IMAGE_EXTRA_INSTALL += " \
    ${IMAGE_DEVTOOLS_PART} \
    ${IMAGE_DVB-APPS_PART} \
    ${IMAGE_NETWORKING_PART} \
    "
