How to deploy demonstration:
----------------------------
Materials:
 * Nucleo extension board with mems, here IKS01A2
 * connection between extension board and stm32mp15 board.

Pre-requisite:
--------------
1. Kernel:
 You need to configure the kernel to support the Nucleo extension
 board with the devitree configuration and the kernel configuration.

1.1 DeviceTree:
 * Enable the sensor on I2C
   For Discovery board (stm32mp157c-dk2), the sensors are linked on ic25.
   Add the following line on your devicetree associateds to the board

&i2c5 {
	status = "okay";
	hts221@5f {
		compatible = "st,hts221";
		reg = <0x5f>;
	};
	lps22hb@5d {
		compatible = "st,lps22hb-press";
		reg = <0x5d>;
		st,drdy-int-pin = <1>;
	};
	lsm6dsl@6b {
		compatible = "st,lsm6dsl";
		reg = <0x6b>;
	};
};
 NOTE: the i2c depend of the pin-muxing of the board and could be different of
 i2c5.

1.2 Kernel configuration:
 Add the following config on your kernel configuraturation
 (best way are via a new fragment)
CONFIG_IIO_BUFFER=y
CONFIG_IIO_KFIFO_BUF=y
CONFIG_IIO_TRIGGERED_BUFFER=y
CONFIG_HTS221=y
CONFIG_IIO_ST_PRESS=y
CONFIG_IIO_ST_LSM6DSX=y
CONFIG_IIO_ST_LSM6DSX_I2C=y

2. Software
 You need to have some framework available on the board for executing the
 python script:

List of packages already present on st-example-image-gtk:
 weston
 gtk+3
 python3-argparse
 python3-datetime
 python3-dateutil
 python3-distutils
 python3-email \
 python3-enum
 python3-fcntl
 python3-importlib
 python3-io
 python3-logging
 python3-misc
 python3-numbers
 python3-pycairo
 python3-pygobject
 python3-pyparsing
 python3-re
 python3-readline
 python3-shell
 python3-signal
 python3-stringold
 python3-subprocess
 python3-textutils
 python3-threading
 python3-unittest

List of packages to add manually:
 gstreamer1.0-plugins-base (with gir file on /usr/lib/girepository-1.0/:
      Gst-1.0.typelib and GstVideo-1.0.typelib)
 gstreamer1.0-plugins-bad-gtk

Note: for generating the package gstreamer1.0-plugins-bad-gtk, you need to
change the default configuration provided by adding gtk

on meta-st-openstlinux/recipes-multimedia/gstreamer/gstreamer1.0-plugins-bad_*.bbappend
PACKAGECONFIG ?= " \
    ${GSTREAMER_ORC} \
    ${PACKAGECONFIG_GL} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'bluez', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'directfb', 'directfb', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'wayland', '', d)} \
    bz2 curl dash hls neon sbc smoothstreaming sndfile uvch264  \
    faac kms gtk\
"

Execution of script on board:
-----------------------------
Files:
/usr/share/sensors_temperature/
/usr/bin/sensors_temperature.py

Put the two file on board and launch the python script:
  BOARD > sensors_temperature.py

To quit the application, just click on ST logo.
