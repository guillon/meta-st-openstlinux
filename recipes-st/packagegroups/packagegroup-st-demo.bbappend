RDEPENDS_${PN} += "\
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'demo-hotspot-wifi', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'demo-launcher', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'weston-cube', '', d)} \
    "

AI_DEMO_APPLICATION = "${@bb.utils.contains('MACHINE_FEATURES', 'm4copro', 'ai-hand-char-reco-launcher', '', d)} "
RDEPENDS_${PN}_append_stm32mpcommon = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', '${AI_DEMO_APPLICATION}', '', d)} \
    "

