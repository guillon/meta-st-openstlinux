RDEPENDS_${PN} += "\
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'demo-hotspot-wifi', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'demo-launcher', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'weston-cube', '', d)} \
    "

RDEPENDS_${PN}_stm32mpcommon += " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'ai-hand-char-reco-launcher', '', d)} \
    "

