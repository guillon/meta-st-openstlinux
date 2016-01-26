DESCRIPTION = "OpenSDK image plus some dev tools."

PV = "1.0.0"
PR = "r1"

#
# INSTALL addons
#

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
# networking part addons
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

CORE_IMAGE_EXTRA_INSTALL += " \
    ${IMAGE_NETWORKING_PART} \
    ${IMAGE_DVB-APPS_PART} \
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

IMAGE_FEATURES += "tools-sdk"

include recipes-core-st/images/st-mm-image.bb
