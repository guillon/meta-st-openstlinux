LICENSE = "CLOSED"

RDEPENDS_${PN}_append += "\
   qtcharts-dev \
   qtcharts-mkspecs \
   "

RDEPENDS_${PN}_remove += "\
   qtenginio-dev \
   qtenginio-mkspecs \
   ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'qtenginio-qmlplugins', '', d)} \
   "

RDEPENDS_${PN}_remove += "\
   ${@bb.utils.contains('DISTRO_FEATURES', 'x11', '${USE_X11}', , d)} \
   "
