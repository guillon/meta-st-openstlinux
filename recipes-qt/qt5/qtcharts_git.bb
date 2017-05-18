require recipes-qt/qt5/qt5.inc
require recipes-qt/qt5/qt5-git.inc

# Disabled until update to QT version higher than 5.6
#LICENSE = "GPLv3"
#LIC_FILES_CHKSUM = "file://LICENSE.GPL3;md5=afa7b20e727eb31f6e9104a375c07f52"
LICENSE = "CLOSED"

inherit qmake5

QT_MODULE_BRANCH = "5.6"
QT_GIT = "git://github.com/qt"

DEPENDS += "qtbase"

RDEPENDS_${PN}-qmlplugins += "qtdeclarative"
RDEPENDS_${PN}-examples += "qtdeclarative qtmultimedia"

SRCREV = "4cdf7b5ca4e72c815ead5a1b2044adb54e10d9d0"
