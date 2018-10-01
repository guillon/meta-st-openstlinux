#!/bin/sh

#add stm32_msc_eth_config script to enable USB Ethernet & MSC gadget This script configures USB Gadget
#configfs to use USB OTG as a USB Ethernet Gadget with Remote NDIS (RNDIS), well supported by Microsoft
#Windows and Linux, and also as a USB Mass-Storage Gadget. Mass-Storage may be only relevant on Linux if #exported partitions are in EXT4 format, but could also be used on Microsoft Windows if exported
#partitions are in FAT or NTFS. Take care of Microsoft Windows "You need to format the disk..." popup,
#choose Cancel, otherwise you risk to erase your µSD content.

configfs="/sys/kernel/config/usb_gadget"
g=g1
c=c.1
d="${configfs}/${g}"
func_eth=rndis.0
func_ms=mass_storage.0

VENDOR_ID="0x1d6b"
PRODUCT_ID="0x0104"

IP="192.168.7.2"
NETMASK="255.255.255.0"

do_start() {
  if [ ! -d ${configfs} ]; then
    modprobe libcomposite
    if [ ! -d ${configfs} ]; then
      exit 1
    fi
  fi

  if [ -d ${d} ]; then
    exit 0
  fi

  udc=$(ls -1 /sys/class/udc/)
	if [ -z $udc ]; then
    echo "No UDC driver registered"
    exit 1
  fi

  mkdir "${d}"
  echo ${VENDOR_ID} > "${d}/idVendor"
  echo ${PRODUCT_ID} > "${d}/idProduct"
  echo 0x0200 > "${d}/bcdUSB"
  echo "0xEF" > "${d}/bDeviceClass"
  echo "0x02" > "${d}/bDeviceSubClass"
  echo "0x01" > "${d}/bDeviceProtocol"
  echo "0x0100" > "${d}/bcdDevice"

  mkdir -p "${d}/strings/0x409"
  echo "0" > "${d}/strings/0x409/serialnumber"
  echo "STMicroelectronics" > "${d}/strings/0x409/manufacturer"
  echo "STM32MP1" > "${d}/strings/0x409/product"

  # Config
  mkdir -p "${d}/configs/${c}"
  mkdir -p "${d}/configs/${c}/strings/0x409"
  echo "Config 1: RNDIS" > "${d}/configs/${c}/strings/0x409/configuration"
  echo 250 > "${d}/configs/${c}/MaxPower"
  echo 0xC0 > "${d}/configs/${c}/bmAttributes" # self powered device

  mkdir -p "${d}/functions/${func_eth}"
  mkdir -p "${d}/functions/${func_ms}"

  # Mass-Storage function
  echo 0 > "${d}/functions/${func_ms}/stall"
  echo 0 > "${d}/functions/${func_ms}/lun.0/cdrom"
  echo 0 > "${d}/functions/${func_ms}/lun.0/ro"
  echo 0 > "${d}/functions/${func_ms}/lun.0/nofua"
  echo 1 > "${d}/functions/${func_ms}/lun.0/removable"
  if [ -z "$1" ]; then
	  echo "Using /dev/mmcblk0"
	  echo "/dev/mmcblk0" > "${d}/functions/${func_ms}/lun.0/file"
	else
	  echo "Using $1"
	  echo "$1" > "${d}/functions/${func_ms}/lun.0/file"
	fi

	# Windows extension to force RNDIS config
  mkdir -p "${d}/os_desc"
  echo "1" > "${d}/os_desc/use"
  echo "0xbc" > "${d}/os_desc/b_vendor_code"
  echo "MSFT100" > "${d}/os_desc/qw_sign"

  mkdir -p "${d}/functions/${func_eth}/os_desc/interface.rndis"
  echo "RNDIS" > "${d}/functions/${func_eth}/os_desc/interface.rndis/compatible_id"
  echo "5162001" > "${d}/functions/${func_eth}/os_desc/interface.rndis/sub_compatible_id"

  # Set up the rndis device only first
  ln -s "${d}/functions/${func_eth}" "${d}/configs/${c}"
  ln -s "${d}/functions/${func_ms}" "${d}/configs/${c}"
  ln -s "${d}/configs/${c}" "${d}/os_desc"

  echo "${udc}" > "${d}/UDC"

  sleep 0.2

  if [ -e /etc/network/interfaces ]; then
    ifup usb0
  else
	ifconfig usb0 $IP $NETMASK
	ifconfig usb0 up
  fi
}

do_stop() {
  if [ -e /etc/network/interfaces ]; then
    ifdown usb0
  else
    ifconfig usb0 down
  fi

  sleep 0.2

  echo "" > "${d}/UDC"

	rm -f "${d}/os_desc/${c}"
  rm -f "${d}/configs/${c}/${func_eth}"
  rm -f "${d}/configs/${c}/${func_ms}"

  [ -d "${d}/strings/0x409/" ] && rmdir "${d}/strings/0x409/"
  [ -d "${d}/configs/${c}/strings/0x409" ] && rmdir "${d}/configs/${c}/strings/0x409"
  [ -d "${d}/configs/${c}" ] && rmdir "${d}/configs/${c}"
  [ -d "${d}/functions/${func_ms}" ] && rmdir "${d}/functions/${func_ms}"
  [ -d "${d}/functions/${func_eth}" ] && rmdir "${d}/functions/${func_eth}"
  [ -d "${d}" ] && rmdir "${d}"
}

case $1 in
	start)
		do_start $2
		;;
	stop)
		do_stop
		;;
	*)
		echo "Usage: $0 (stop | start) [/dev/xxx (if not specified /dev/mmcblk0 will be used by default)]"
		;;
esac
