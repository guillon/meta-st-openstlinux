require opensdk-image-mm.bb

SUMMARY = "OpenSDK multimedia image plus some dev tools."

# Tools alreay installed by opensdk-image-core
# generic:
#   grep
#   util-linux
#   coreutils
#   kbd
#   file
#   bc
# bus/fs:
#   procps
#   usbutils
#   pciutils
#   e2fsprogs
# CPU:
#   sysstat
# DISPLAY:
#   fb-test



#
# Kernel tools part addons
#
IMAGE_TOOLS_KERNEL_PART += " \
    strace          \
    trace-cmd       \
    \
    cpuburn-neon    \
    memtester       \
    lmbench         \
    nbench-byte     \
    \
    latencytop      \
    powertop        \
    \
    cpufrequtils    \
    sysfsutils      \
    \
    evtest          \
    \
    ltp             \
    lttng-modules   \
    \
    elfutils        \
    formfactor      \
    "

#
# Kernel tools part addons
#
IMAGE_TOOLS_MM_PART += " \
    tslib-calibrate \
    "

#
# Generic tools part addons
#
IMAGE_TOOLS_GENERIC_PART += " \
    minicom         \
    ntp             \
    systemtap       \
    "


#
# Networking part addons
#
IMAGE_TOOLS_NETWORKING_PART += " \
    iperf           \
    tcpdump         \
    ethtool         \
    bridge-utils    \
    hostap-utils    \
    iproute2        \
    iptables        \
    iw              \
    vlan            \
    "

#
# BUS tools part addons
#
IMAGE_TOOLS_BUS_PART += " \
    can-utils       \
    i2c-tools       \
    "

#
# BUS tools part addons
#
IMAGE_TOOLS_FS_PART += " \
    dosfstools      \
    mmc-utils       \
    nfs-utils       \
    blktool         \
    blktrace        \
    "


#
# INSTALL addons
#
CORE_IMAGE_EXTRA_INSTALL += " ${IMAGE_TOOLS_KERNEL_PART} "
CORE_IMAGE_EXTRA_INSTALL += " ${IMAGE_TOOLS_MM_PART} "
CORE_IMAGE_EXTRA_INSTALL += " ${IMAGE_TOOLS_GENERIC_PART} "
CORE_IMAGE_EXTRA_INSTALL += " ${IMAGE_TOOLS_NETWORKING_PART} "
CORE_IMAGE_EXTRA_INSTALL += " ${IMAGE_TOOLS_BUS_PART} "
CORE_IMAGE_EXTRA_INSTALL += " ${IMAGE_TOOLS_FS_PART} "
