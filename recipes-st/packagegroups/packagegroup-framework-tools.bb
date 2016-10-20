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
    "

PROVIDES = "${PACKAGES}"
RDEPENDS_packagegroup-framework-tools-core = "\
    grep            \
    util-linux      \
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
    \
    tslib-calibrate \
    pointercal      \
"

RDEPENDS_packagegroup-framework-tools-kernel = "\
    usbutils        \
    pciutils        \
    cpufrequtils    \
    sysfsutils      \
    latencytop      \
    powertop        \
    can-utils       \
    i2c-tools       \
    dosfstools      \
    mmc-utils       \
    mtd-utils       \
    blktool         \
    fio             \
    \
    memtester       \
    lmbench         \
    nbench-byte     \
    iozone3         \
    bonnie++        \
    ltp             \
    elfutils        \
    formfactor      \
    \
    strace          \
    \
    evtest          \
"

RDEPENDS_packagegroup-framework-tools-network = "\
    iperf           \
    iperf3          \
    tcpdump         \
    ethtool         \
    bridge-utils    \
    iproute2        \
    iptables        \
    vlan            \
    libnl           \
    connman         \
    connman-client  \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'hostap-utils', '', d)}    \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'iw', '', d)}              \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'wireless-tools', '', d)}  \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'wpa-supplicant', '', d)}  \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'hostapd', '', d)}         \
    openssh-sftp-server \
    openssh-sftp    \
    net-snmp        \
    ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'dhcp-client st-dhcp-client', '', d)} \
    \
    neard           \
"

RDEPENDS_packagegroup-framework-tools-audio = "\
    ${@bb.utils.contains('DISTRO_FEATURES', 'alsa', 'alsa-lib', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'alsa', 'alsa-utils', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'alsa', 'alsa-plugins', '', d)} \
    pulseaudio \
    pulseaudio-server \
    pulseaudio-misc \
    pulseaudio-module-combine-sink \
"

RDEPENDS_packagegroup-framework-tools-ui = "\
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'xvinfo', '', d)}    \
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'xvideo-tests', '', d)}  \
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'x11perf', '', d)}   \
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'fstests', '', d)}   \
    gtkperf         \
"
