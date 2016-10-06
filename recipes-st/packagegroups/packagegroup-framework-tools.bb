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
    blktool         \
    blktrace        \
    \
    memtester       \
    lmbench         \
    nbench-byte     \
    ltp             \
    lttng-modules   \
    elfutils        \
    formfactor      \
    \
    strace          \
    trace-cmd       \
    \
    evtest          \
"

RDEPENDS_packagegroup-framework-tools-network = "\
    iperf           \
    tcpdump         \
    ethtool         \
    bridge-utils    \
    hostap-utils    \
    iproute2        \
    iptables        \
    iw              \
    vlan            \
    wireless-tools  \
    libnl           \
    wpa-supplicant  \
    connman         \
    connman-client  \
    hostapd         \
    ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'dhcp-client st-dhcp-client', '', d)} \
"

RDEPENDS_packagegroup-framework-tools-audio = "\
    alsa-lib \
    alsa-utils \
    alsa-plugins \
    pulseaudio \
    pulseaudio-server \
    pulseaudio-misc \
    pulseaudio-module-combine-sink \
"
