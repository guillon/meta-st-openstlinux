DESCRIPTION = "Frameworks components"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

PR = "r0"

PACKAGE_ARCH = "${TUNE_PKGARCH}"

inherit packagegroup

PACKAGES = "\
    packagegroup-framework-tools-core-base \
    packagegroup-framework-tools-kernel-base \
    packagegroup-framework-tools-network-base \
    packagegroup-framework-tools-audio-base \
    packagegroup-framework-tools-ui-base \
    \
    packagegroup-framework-tools-core \
    packagegroup-framework-tools-kernel \
    packagegroup-framework-tools-network \
    packagegroup-framework-tools-audio \
    packagegroup-framework-tools-ui \
    \
    packagegroup-framework-tools-core-extra \
    packagegroup-framework-tools-kernel-extra \
    packagegroup-framework-tools-network-extra \
    packagegroup-framework-tools-audio-extra \
    packagegroup-framework-tools-ui-extra \
    "

PROVIDES = "${PACKAGES}"

RDEPENDS_packagegroup-framework-tools-core-base = "\
    ckermit         \
    coreutils       \
    libiio-iiod     \
    libiio-tests    \
    "
RDEPENDS_packagegroup-framework-tools-core = "\
    grep            \
    util-linux      \
    util-linux-lscpu \
    procps          \
    kbd             \
    file            \
    bc              \
    e2fsprogs       \
    e2fsprogs-resize2fs \
    sysstat         \
    minicom         \
    ntp             \
    systemtap       \
    gptfdisk        \
    lsb             \
    "
RDEPENDS_packagegroup-framework-tools-core-extra = "\
    tslib-calibrate \
    pointercal \
    \
    acl \
    bzip2 \
    cronie \
    rng-tools \
    \
    python-lxml \
    python-modules \
    python-nose \
    python-pkgutil \
    python-pytest \
    python-setuptools \
    python-unittest \
    "

RDEPENDS_packagegroup-framework-tools-kernel-base = "\
    can-utils       \
    i2c-tools       \
    strace          \
    usbutils        \
    \
    evtest          \
    memtester       \
    mtd-utils       \
    "
RDEPENDS_packagegroup-framework-tools-kernel = "\
    pciutils        \
    cpufrequtils    \
    sysfsutils      \
    dosfstools      \
    mmc-utils       \
    blktool         \
    "
RDEPENDS_packagegroup-framework-tools-kernel-extra = "\
    latencytop      \
    powertop        \
    fio             \
    \
    lmbench         \
    nbench-byte     \
    iozone3         \
    bonnie++        \
    bonnie-scripts  \
    ltp             \
    elfutils        \
    formfactor      \
    \
    lirc \
    "

RDEPENDS_packagegroup-framework-tools-network-base = "\
    ethtool         \
    iproute2        \
    "
RDEPENDS_packagegroup-framework-tools-network = "\
    tcpdump         \
    iptables        \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'hostap-utils', '', d)}    \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'iw', '', d)}              \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'wireless-tools', '', d)}  \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'wpa-supplicant', '', d)}  \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'hostapd', '', d)}         \
    openssh-sftp    \
    ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'dhcp-client st-dhcp-client', '', d)} \
    "
RDEPENDS_packagegroup-framework-tools-network-extra = "\
    iperf3          \
    netperf          \
    bridge-utils    \
    vlan            \
    libnl           \
    connman         \
    connman-client  \
    net-snmp        \
    \
    neard           \
    "

RDEPENDS_packagegroup-framework-tools-audio-base = "\
    ${@bb.utils.contains('DISTRO_FEATURES', 'alsa', 'libasound alsa-conf', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'alsa', 'alsa-utils', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'alsa', 'alsa-plugins', '', d)} \
    "
RDEPENDS_packagegroup-framework-tools-audio = "\
    pulseaudio \
    pulseaudio-server \
    pulseaudio-misc \
    pulseaudio-module-combine-sink \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'pulseaudio-module-bluetooth-discover', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'pulseaudio-module-bluetooth-policy', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'pulseaudio-module-bluez5-device', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'pulseaudio-module-bluez5-discover', '', d)} \
    "
RDEPENDS_packagegroup-framework-tools-audio-extra = "\
    ${@bb.utils.contains('DISTRO_FEATURES', 'alsa', 'alsa-utils-aplay', '', d)} \
    "

RDEPENDS_packagegroup-framework-tools-ui-base = "\
    "
RDEPENDS_packagegroup-framework-tools-ui = "\
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'xvinfo', '', d)}    \
    ${@bb.utils.contains('DISTRO_FEATURES', 'gplv3', 'glmark2', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'gplv3', 'netdata', '', d)} \
    "
RDEPENDS_packagegroup-framework-tools-ui-extra = "\
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'xvideo-tests', '', d)}  \
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'x11perf', '', d)}   \
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'fstests', '', d)}   \
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'gtkperf', '', d)}   \
    "
