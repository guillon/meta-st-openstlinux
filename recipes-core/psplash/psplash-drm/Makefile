SPLASH_IMG ?=  LOGO_cobranded_OpenSDK.png


all: modeset


generate_header: $(SPLASH_IMG)
	@gdk-pixbuf-csource --macros $(SPLASH_IMG) > image_header.tmp
	@(sed -e "s/MY_PIXBUF/SPLASH_IMG/g" -e "s/guint8/uint8_t/g" image_header.tmp > image_header.h && rm image_header.tmp)

psplash:
	$(CC) -o psplash-drm basic_splash_drm.c -I. `pkg-config --cflags --libs libdrm` -Wall -O0 -g

clean:
	rm -rf psplash-drm
