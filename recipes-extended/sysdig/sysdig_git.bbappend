DEPENDS += "jsoncpp openssl curl jq"
DEPENDS += "libb64"
DEPENDS += "elfutils"

SRC_URI = "git://github.com/draios/sysdig.git;protocol=https"
SRCREV = "153e3951f7c0fb449a5ed23949eceaf2c25d3877"

PV = "0.22.1+git${SRCPV}"

EXTRA_OECMAKE += ' -DUSE_BUNDLED_JSONCPP="OFF" '
EXTRA_OECMAKE += ' -DUSE_BUNDLED_OPENSSL="OFF" '
EXTRA_OECMAKE += ' -DUSE_BUNDLED_CURL="OFF" '
EXTRA_OECMAKE += ' -DUSE_BUNDLED_B64="OFF" '
EXTRA_OECMAKE += ' -DUSE_BUNDLED_JQ="OFF" '
