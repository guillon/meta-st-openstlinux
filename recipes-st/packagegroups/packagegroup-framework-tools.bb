DESCRIPTION = "Frameworks components"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

PR = "r0"

PACKAGE_ARCH = "${TUNE_PKGARCH}"

inherit packagegroup

PACKAGES = "\
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

RDEPENDS_packagegroup-framework-tools-core = "\
    grep            \
    util-linux      \
    util-linux-lscpu \
    procps          \
    kbd             \
    file            \
    bc              \
    coreutils       \
    e2fsprogs       \
    e2fsprogs-resize2fs \
    sysstat         \
    minicom         \
    ntp             \
    systemtap       \
    gptfdisk        \
    ckermit         \
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

RDEPENDS_packagegroup-framework-tools-kernel = "\
    usbutils        \
    pciutils        \
    cpufrequtils    \
    sysfsutils      \
    can-utils       \
    i2c-tools       \
    dosfstools      \
    mmc-utils       \
    blktool         \
    \
    strace          \
"
RDEPENDS_packagegroup-framework-tools-kernel-extra = "\
    latencytop      \
    powertop        \
    mtd-utils       \
    fio             \
    \
    memtester       \
    lmbench         \
    nbench-byte     \
    iozone3         \
    bonnie++        \
    bonnie-scripts  \
    ltp             \
    elfutils        \
    formfactor      \
    \
    evtest          \
    lirc \
"

RDEPENDS_packagegroup-framework-tools-network = "\
    tcpdump         \
    iproute2        \
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
    ethtool         \
    bridge-utils    \
    vlan            \
    libnl           \
    connman         \
    connman-client  \
    net-snmp        \
    \
    neard           \
"

RDEPENDS_packagegroup-framework-tools-audio = "\
    ${@bb.utils.contains('DISTRO_FEATURES', 'alsa', 'libasound alsa-conf', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'alsa', 'alsa-utils', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'alsa', 'alsa-plugins', '', d)} \
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
