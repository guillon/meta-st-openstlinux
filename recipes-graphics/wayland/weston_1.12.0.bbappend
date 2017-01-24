FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}/st-1.12:"

SRC_URI_append = " \
    file://0001-configure.ac-bump-version-to-1.12.90-for-open-develo.patch \
    file://0002-configure-Also-update-libweston-version-to-match-wes.patch \
    file://0003-compositor-set-the-opaque-region-for-some-views-with.patch \
    file://0004-zunitc-junit-reporter-Silence-pointer-sign-warning.patch \
    file://0005-share-cairo-util-Use-wl_pointer_button_state-enum-di.patch \
    file://0006-string-helpers.h-Fix-usage-on-musl-libc.patch \
    file://0007-libweston-desktop-Fix-some-clang-warnings.patch \
    file://0008-shared-platform-include-weston-egl-ext.h-only-if-ENA.patch \
    file://0009-configure-remove-double-equal-test-bashism.patch \
    file://0010-libweston-desktop-fix-sending-the-configure-event-wi.patch \
    file://0011-clients-stacking-Silence-a-compiler-warning.patch \
    file://0012-gl-renderer-Add-support-for-DRM_FORMAT_YUV444-buffer.patch \
    file://0013-libweston-Add-more-functionality-for-handling-weston.patch \
    file://0014-libweston-Add-initial-output-API-for-windowed-output.patch \
    file://0015-compositor-Implement-output-configuration-using-wind.patch \
    file://0016-weston-Port-DRM-backend-to-new-output-handling-API.patch \
    file://0017-weston-Port-fbdev-backend-to-new-output-handling-API.patch \
    file://0018-weston-Port-headless-backend-to-new-output-handling-.patch \
    file://0019-weston-Port-RDP-backend-to-new-output-handling-API.patch \
    file://0020-weston-Port-Wayland-backend-to-new-output-handling-A.patch \
    file://0021-weston-Port-X11-backend-to-new-output-handling-API.patch \
    file://0022-libweston-Merge-weston_output_init-into-weston_outpu.patch \
    file://0023-weston-Rename-weston_output_init_pending-to-weston_o.patch \
    file://0024-libweston-Remove-weston_backend_output_config-struct.patch \
    file://0025-libweston-Drop-requirement-of-setting-mm_width-mm_he.patch \
    file://0026-compositor-rdp-Properly-destroy-the-renderer-and-pix.patch \
    file://0027-libweston-include-weston-egl-ext.h-in-drm-x11-and-wa.patch \
    file://0028-gl-renderer-add-support-of-WL_SHM_FORMAT_YUV420.patch \
    file://0029-gl-renderer-add-support-of-WL_SHM_FORMAT_NV12.patch \
    file://0030-gl-renderer-conditionally-call-query_buffer-while-gl.patch \
    file://0031-linux-dmabuf-align-DMABUF-exposed-formats-with-EGL-s.patch \
    file://0032-libweston-fix-building-issue-when-EGL-support-is-not.patch \
    file://0034-libinput-seat-Don-t-regard-no-input-devices-as-failu.patch \
    file://0035-DEMO-add-simple-egl-cube-and-slideshow-applications.patch \
    file://0036-compositor-add-xlog-debug-info.patch \
    file://0037-pixman-renderer-support-of-linux-dmabuf.patch \
    file://0038-pixman-renderer-support-SHM-and-DMABUF-NV12-pixel-fo.patch \
    file://0039-pixman-renderer-support-SHM-and-DMABUF-RGB-BGR-24-bi.patch \
    file://0040-pixman-renderer-support-SHM-and-DMABUF-BGR-24-32-bit.patch \
    file://0041-pixman-renderer-support-SHM-I420-pixel-format-debug-.patch \
    file://0042-compositor-st-initial-commit-copy-of-compositor-drm.patch \
    file://0043-compositor-st-enable-sprites.patch \
    file://0044-compositor-st-cursors_are_broken-per-output.patch \
    file://0045-compositor-st-add-sprite-and-cursor-logs-when-they-a.patch \
    file://0046-compositor-st-assign-dmabuf-buffers-in-DRM-planes-ov.patch \
    file://0047-compositor-st-manager-zorder.patch \
    file://0048-compositor-st-interlaced-buffers-support.patch \
    file://0049-compositor-st-allow-HQVDP-scaling.patch \
    file://0050-compositor-st-ignore-the-waylandsink-1x1-area-surfac.patch \
    file://0051-compositor-st-use-DRM-planes-with-pixman.patch \
    file://0052-compositor-st-forbid-sprite-on-several-crtc.patch \
    file://0053-compositor-st-wait-for-vblank-of-all-sprites.patch \
    file://0054-compositor-st-fix-fd-leak-for-gbm_bo.patch \
    file://0055-ST-display-capture-support.patch \
    file://0056-gl-renderer-force-BT709-full-range-for-NV12-dmabuf-b.patch \
    file://0057-gl-renderer-implements-the-EGL_KHR_partial_update-ex.patch \
    file://0058-gl-renderer-add-support-of-WL_SHM_FORMAT_YUYV.patch \
    file://0059-compositor-st-drmModeSetPlane-error-management.patch \
    file://0060-linux-dmabuf-add-backend-private-data-in-linux_dmabu.patch \
    file://0061-compositor-st-use-backend_user_data-to-store-dmabuf-.patch \
    file://0062-compositor-st-don-t-repaint-primary-plane-if-no-dama.patch \
    file://0063-compositor-st-lastly-call-DRM_IOCTL_MODE_PAGE_FLIP-i.patch \
    file://0064-compositor-st-release-buffer-when-drmModeSetPlane-fa.patch \
    file://0065-compositor-force-repaint-immediately-on-receiving-th.patch \
    "

# Weston on KMS
PACKAGECONFIG[stkms] = "--enable-st-compositor,--disable-st-compositor,drm udev virtual/mesa mtdev"
# Composition capture
PACKAGECONFIG[capture] = "--enable-display-capture,--disable-display-capture"
