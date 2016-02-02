require opensdk-image-mm.bb

SUMMARY = "OpenSDK multimedia image plus some dev tools."

IMAGE_FEATURES += "tools-sdk"

#
# Dev-tools part addons
#
IMAGE_DEVTOOLS_PART += " \
    perf \
    strace \
    trace-cmd \
    gstreamer1.0-devtools \
    i2c-tools \
    v4l-utils \
    szap-s2 \
    smcroute \
    bridge-utils \
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
    iperf \
    tcpdump \
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
