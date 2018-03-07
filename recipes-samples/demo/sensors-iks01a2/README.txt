How to deploy demonstration:
----------------------------
Materials:
 * Nucleo extension board with mems, here IKS01A2
 * connection between extension board and stm32mp15 board.

Pre-requisite:
--------------
You need to configure the kernel to support the Nucleo extension
board with the devitree configuration and the kernel configuration.

DeviceTree:
-----------
* Enable the sensor on I2C
  For Discovery board (stm32mp157c-dk2), the sensors are linked on ic25.

  Add the following line on your devicetree associateds to the board

&i2c5 {
	status = "okay";
	hts221@5f {
		compatible = "st,hts221";
		reg = <0x5f>;
	};
	/*lps25h@5d {
		compatible = "st,lps25h-press";
		reg = <0x5d>;
	};*/
	/*lps22hb@5d {
		compatible = "st,lps22hb-press";
		reg = <0x5d>;
	};*/
	lsm6dsl@6b {
		compatible = "st,lsm6dsl";
		reg = <0x6b>;
	};
};
NOTE: the i2c depend of the pin-muxing of the board and could be different of
i2c5.

Kernel configuration:
---------------------
Add the following config on your kernel configuraturation
(best way are via a new fragment)
CONFIG_IIO_BUFFER=y
CONFIG_IIO_KFIFO_BUF=y
CONFIG_IIO_TRIGGERED_BUFFER=y
CONFIG_HTS221=y
CONFIG_IIO_ST_PRESS=y
CONFIG_IIO_ST_LSM6DSX=y
CONFIG_IIO_ST_LSM6DSX_I2C=y


Execution of script on board:
-----------------------------
Files:
/usr/share/sensors_temperature/stlogo.png
/usr/bin/sensors_temperature.py

Put the two file on board and launch the python script:
  BOARD > sensors_temperature.py

To quit the application, just click on ST logo.
