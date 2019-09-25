# With this image, we want to generate additionnal packages that could be
# used to populate a package repository server.
# The default st-image-weston is "extended" with extra IMAGE_FEATURES, and is also
# extended with as much as possible 'packagegroup-framework-*'.

SUMMARY = "OpenSTLinux image based on ST weston image to generate all packages supported by ST."
LICENSE = "Proprietary"

include recipes-st/images/st-image-weston.bb

# let's make sure we have a good image...
REQUIRED_DISTRO_FEATURES = "wayland"

#
# Introduce specific function to disable 'do_image' and 'do_image_complete' tasks
#
python __anonymous () {
    # Gather all current tasks
    tasks = filter(lambda k: d.getVarFlag(k, "task", True), d.keys())
    for task in tasks:
        # Check that we are dealing with image recipe
        if task == 'do_image_complete':
            # Init current image name
            current_image_name = d.getVar('PN') or ""
            # Init RAMFS image if any
            initramfs = d.getVar('INITRAMFS_IMAGE') or ""
            # We want to disable 'do_image' and 'do_image_complete' tasks to avoid
            # wasting time as we're only interested in package generation.
            # We anyway need to execute these tasks for InitRAMFS image unless we
            # may get issue with kernel generation
            if current_image_name not in initramfs:
                d.setVarFlag('do_image', 'noexec', '1')
                d.setVarFlag('do_image_complete', 'noexec', '1')
}

#
# IMAGE_FEATURES addons
#
IMAGE_FEATURES_append = " \
    x11-base        \
    x11-sato        \
    tools-debug     \
    tools-sdk       \
    dbg-pkgs        \
    dev-pkgs        \
    doc-pkgs        \
    nfs-server      \
    staticdev-pkgs  \
    "

#
# INSTALL addons (manage to add all available openstlinux packages)
#
CORE_IMAGE_EXTRA_INSTALL_append = " \
    packagegroup-framework-tools-extra              \
    \
    packagegroup-framework-sample-x11               \
    packagegroup-framework-sample-xfce              \
    packagegroup-framework-sample-qt                \
    packagegroup-framework-sample-qt-examples       \
    packagegroup-framework-sample-qt-extra          \
    packagegroup-framework-sample-qt-extra-examples \
    "

