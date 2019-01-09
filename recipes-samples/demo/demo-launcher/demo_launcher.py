#!/usr/bin/python3
# to debug this script:
#      python3 -m pdb ./demo_launcher.py
#
import gi
gi.require_version('Gtk', '3.0')
from gi.repository import Gtk
from gi.repository import GObject
from gi.repository import Gdk
from gi.repository import GLib
from gi.repository import GdkPixbuf

import subprocess
import random
import math
import os
import socket
import fcntl
import struct
from collections import deque
from time import sleep, time

#
# For simulating UI on PC , please use
# the variable SIMULATE = 1
# If SIMULATE = 1 then
#    the picture/icon must be present on pictures directory
#
SIMULATE = 0


if SIMULATE > 0:
    DEMO_PATH = "/home/frq08471/temp/launcher/"
else:
    DEMO_PATH = "/usr/local/demo"

# -------------------------------------------------------------------
# -------------------------------------------------------------------
# CONSTANT VALUES
#
SIMULATE_SCREEN_SIZE_WIDTH  = 800
SIMULATE_SCREEN_SIZE_HEIGHT = 480

WIFI_HOTSPOT_IP="192.168.72.1"

WIFI_DEFAULT_SSID="STDemoNetwork"
WIFI_DEFAULT_PASSWD="stm32mp1"

# -------------------------------------------------------------------
# -------------------------------------------------------------------
# SPLASH SCREEN class
#    the splash screen display a logo and the different step of boot
#
class SplashScreen():
    def __init__(self, picture_filename, timeout):
        #DONT connect 'destroy' event here!
        self.window = Gtk.Window()
        self.window.set_title('ST Launcher')
        self.window.set_position(Gtk.WindowPosition.CENTER)
        self.window.set_decorated(False)
        if SIMULATE > 0:
            screen_width = SIMULATE_SCREEN_SIZE_WIDTH
            screen_height = SIMULATE_SCREEN_SIZE_HEIGHT
        else:
            self.window.fullscreen()
            #self.maximize()
            screen_width = self.window.get_screen().get_width()
            screen_height = self.window.get_screen().get_height()

        self.window.set_default_size(screen_width, screen_height)
        self.window.set_border_width(1)

        # Add Vbox with image and label
        main_vbox = Gtk.VBox(False, 1)
        self.window.add(main_vbox)
        # load picture
        print("[DEBUG] Splash screen with picture: %s" % picture_filename)
        if os.path.exists(picture_filename):
            pixbuf = GdkPixbuf.Pixbuf.new_from_file_at_scale(
                filename=picture_filename,
                width=400, # TODO: change size
                height=600, # TODO: change size
                preserve_aspect_ratio=True)
            image = Gtk.Image.new_from_pixbuf(pixbuf)
            main_vbox.pack_start(image, True, True, 0)

        #self.lbl = Gtk.Label("Init: splash screen")
        #self.lbl.set_alignment(0, 0.5)
        #main_vbox.pack_start(self.lbl, True, True, 0)

        self.window.set_auto_startup_notification(False)
        self.window.show_all()
        self.window.set_auto_startup_notification(True)

        # Ensure the splash is completely drawn before moving on
        GLib.timeout_add(1000, self.loop)
        self.loops = 0
        self.loops_timeout = timeout
        self.loops_break = 0

    def update_text(self, text):
        self.lbl.set_text(text)

    def loop_stop(self):
        self.loops_break = 1

    def loop(self):
        global var
        var = time ()
        print ("[DEBUG] ",  var)
        self.loops += 1
        if self.loops_break or self.loops == self.loops_timeout:
            Gtk.main_quit()
            self.window.destroy()
            return False
        return True


class DialogError(Gtk.Dialog):

    def __init__(self, parent):
        Gtk.Dialog.__init__(self, "Error", parent, 0,
            (Gtk.STOCK_OK, Gtk.ResponseType.OK))

        self.set_default_size(200, 100)
        label = Gtk.Label("Webcam is not connected :\n/dev/video0 doesn't exist")
        box = self.get_content_area()
        box.add(label)
        self.show_all()

# -------------------------------------------------------------------
# -------------------------------------------------------------------
# prerequisite:
# gstreamer1.0-plugins-base: for gir file
# gstreamer1.0-plugins-bad-gtk: for sink compliant with wayland
try:
    HAVE_VIDEO_SINK = 1
    gi.require_version('Gst', '1.0')
    gi.require_version('GstVideo', '1.0')
    from gi.repository import Gst
except ImportError:
    print("[DEBUG]: No gst/gstvideo detected")
    HAVE_VIDEO_SINK = None
if HAVE_VIDEO_SINK:
    try:
        Gst.init(None)
        Gst.init_check(None)
    except Exception as exc:
        pass

# Gstreamer videotestsrc widget
class GstVideoTestSrcWidget(Gtk.Box):
    def __init__(self):
        super().__init__()
        self.connect('realize', self._on_realize)
        self._bin = Gst.parse_bin_from_description('videotestsrc', True)

    def _on_realize(self, widget):
        self.pipeline = Gst.Pipeline()
        factory = self.pipeline.get_factory()
        gtksink = factory.make('gtksink')
        self.pipeline.add(gtksink)
        self.pipeline.add(self._bin)
        self._bin.link(gtksink)
        self.pack_start(gtksink.props.widget, True, True, 0)
        gtksink.props.widget.show()
        self.pipeline.set_state(Gst.State.PLAYING)
    def set_file(self, filename):
        ''' '''
        return True
    def start(self):
        print("[DEBUG] [MIR] ask to start")
        self.pipeline.set_state(Gst.State.PLAYING)
    def stop(self):
        print("[DEBUG] [MIR] ask to stop")
        self.pipeline.set_state(Gst.State.NULL)
    def pause(self):
        print("[DEBUG] [MIR] ask to pause")
        self.pipeline.set_state(Gst.State.PAUSED)

class GstCameraSrcWidget(Gtk.Box):
    def __init__(self):
        super().__init__()
        self.connect('realize', self._on_realize)
        self._bin = Gst.parse_bin_from_description('v4l2src io-mode=4 ! video/x-raw,width=640,height=480 ! queue ! videoconvert', True)

    def _on_realize(self, widget):
        self.pipeline = Gst.Pipeline()
        factory = self.pipeline.get_factory()
        gtksink = factory.make('gtksink')
        self.pipeline.add(gtksink)
        self.pipeline.add(self._bin)
        self._bin.link(gtksink)
        self.pack_start(gtksink.props.widget, True, True, 0)
        gtksink.props.widget.show()
        self.pipeline.set_state(Gst.State.PLAYING)
    def set_file(self, filename):
        ''' '''
        return True
    def start(self):
        print("[DEBUG] [Camera] ask to start")
        self.pipeline.set_state(Gst.State.PLAYING)
    def stop(self):
        print("[DEBUG] [Camera] ask to stop")
        self.pipeline.set_state(Gst.State.NULL)
    def pause(self):
        print("[DEBUG] [Camera] ask to pause")
        self.pipeline.set_state(Gst.State.PAUSED)

# Gstreamer video playback file widget
class GstVideoWidget(Gtk.Box):
    def __init__(self):
        super().__init__()
        self.connect('realize', self._on_realize)

        self.pipeline = Gst.Pipeline()
        self.player = Gst.ElementFactory.make("playbin", "player")
        self.gtksink = Gst.ElementFactory.make("gtksink", "gtksink")
        self.bus = self.pipeline.get_bus()
        self.bus.add_signal_watch()
        self.bus.connect("message::eos", self.on_eos)

    def _on_realize(self, widget):
        print("[DEBUG] [Video] _on_realize")
        self.pipeline.add(self.player)
        self.player.set_property("video-sink", self.gtksink)

        self.pack_start(self.gtksink.props.widget, True, True, 0)
        self.gtksink.props.widget.show()
        self.start()

    def set_file(self, filename):
        if os.path.isfile(filename):
            filepath = os.path.realpath(filename)
            self.player.set_property("uri", "file://%s" % filepath)
            print("VIDEO: set filename %s" % filepath)
            #self.start()

    def start(self):
        print("[DEBUG] [Video] ask to start")
        self.pipeline.set_state(Gst.State.PLAYING)
    def stop(self):
        print("[DEBUG] [Video] ask to stop")
        self.pipeline.set_state(Gst.State.NULL)
    def pause(self):
        print("[DEBUG] [Video] ask to pause")
        self.pipeline.set_state(Gst.State.PAUSED)
    def on_eos(self, bus, message):
        print("EOS")
        self.pipeline.set_state(Gst.State.NULL)
        self.pipeline.set_state(Gst.State.PLAYING)
    def _closewindow(self, widget):
        self.stop()
        Gtk.main_quit()

# video Window
class GstVideoWindow(Gtk.Dialog):
    def __init__(self, parent, pipeline):
        Gtk.Dialog.__init__(self, "Video", parent, 0)

        #self.fullscreen()
        self.maximize()
        self.set_decorated(False)
        self.override_background_color(Gtk.StateType.NORMAL, Gdk.RGBA(.5,.5,.5,.5))

        self.previous_click_time=0
        self.stream_is_paused=0
        mainvbox = self.get_content_area()

        if "videotestsrc" == pipeline:
            self.video_widget = GstVideoTestSrcWidget ()
            self.filename = "videotestsrc"
        elif "camera" == pipeline:
            self.video_widget = GstCameraSrcWidget ()
            self.filename = "camera"
        else:
            self.video_widget = GstVideoWidget()
            self.filename = "%s/media/Teaser-STM32MP1.webm" % DEMO_PATH
            self.set_video_filename("%s/media/Teaser-STM32MP1.webm" % DEMO_PATH)

        self.video_widget.set_halign(Gtk.Align.CENTER)
        self.video_widget.set_valign(Gtk.Align.CENTER)
        mainvbox.pack_start(self.video_widget, True, True, 0)
        self.video_widget.connect("button-press-event", self.on_video_press_event)
        #self.show_all()

    def on_video_press_event(self, widget, event):
        self.click_time = time()
        print(self.click_time - self.previous_click_time)
        if (self.click_time - self.previous_click_time) < 0.4:
            print ("double click")
            if not self.filename is None:
                self.video_widget.stop()
                self.destroy()
        else:
            print ("simple click")
            self.previous_click_time = self.click_time
            if (self.stream_is_paused == 1):
                self.video_widget.start()
                self.stream_is_paused = 0
            else:
                self.video_widget.pause()
                self.stream_is_paused = 1

    def set_video_filename(self, filename):
        self.video_widget.set_file(filename)


# Wifi hotspot view
class WifiWindow(Gtk.Dialog):
    def __init__(self, parent, pipeline):
        Gtk.Dialog.__init__(self, "Wifi", parent, 0)

        self.fullscreen()
        mainvbox = self.get_content_area()

        self.page_ip = Gtk.VBox()
        self.page_ip.set_border_width(10)
        self.set_border_width(10)

        self.title = Gtk.Label()
        self.title.set_markup('<span font_desc="LiberationSans 40" face="sans"><b>Wifi Hotspot informations</b></span>')
        self.page_ip.add(self.title)
        self.label_hotspot = Gtk.Label()
        self.label_hotspot.set_xalign (0.0)
        self.label_ip_wlan0 = Gtk.Label()
        self.label_ip_wlan0.set_xalign (0.0)
        self.label_ip_eth0 = Gtk.Label()
        self.label_ip_eth0.set_xalign (0.0)
        self.label_ip_wlan_ssid = Gtk.Label()
        self.label_ip_wlan_ssid.set_xalign (0.0)
        self.label_ip_wlan_passwd = Gtk.Label()
        self.label_ip_wlan_passwd.set_xalign (0.0)

        self.previous_click_time=0
        self.wifi_ssid=WIFI_DEFAULT_SSID
        self.wifi_passwd=WIFI_DEFAULT_PASSWD

        self.info_grid = Gtk.Grid()
        self.info_grid.set_column_spacing(2)
        self.info_grid.set_row_spacing(2)

        if self.is_wifi_available():
            print ("wlan0 is available")
            self.hostspot_switch = Gtk.Switch()
            self.get_wifi_config()

            # set wlan switch state on first execution
            ip_wlan0 = get_ip_address('wlan0')
            if ip_wlan0 == WIFI_HOTSPOT_IP:
                self.hostspot_switch.set_active(True)
            else:
                self.hostspot_switch.set_active(False)
            self.hostspot_switch.connect("notify::active", self.on_switch_activated)

            self.info_grid.attach(self.hostspot_switch, 0, 2, 1, 1)
            self.info_grid.attach(self.label_hotspot, 1, 2, 1, 1)
            self.info_grid.attach(self.label_ip_wlan0, 0, 0, 2, 1)
            self.info_grid.attach(self.label_ip_wlan_ssid, 0, 3, 2, 1)
            self.info_grid.attach(self.label_ip_wlan_passwd, 0, 4, 2, 1)
        else:
            print ("wlan0 interface not available")
            self.info_grid.attach(self.label_hotspot, 0, 0, 2, 1)

        self.info_grid.attach(self.label_ip_eth0, 0, 1, 2, 1)
        self.page_ip.add(self.info_grid)
        self.refresh_network_page()
        self.connect("button-release-event", self.on_page_press_event)

        mainvbox.pack_start(self.page_ip, False, True, 3)
        self.show_all()

    def is_wifi_available(self):
        if 'wlan0' in open('/proc/net/dev').read():
            return True
        return False

    def get_wifi_config(self):
        filepath = "/etc/default/hostapd"
        if os.path.isfile(filepath):
            file = open(filepath,"r")
            i=0
            for line in file:
                if "HOSTAPD_SSID" in line:
                    self.wifi_ssid = (line.split('=')[1]).rstrip('\r\n')
                    i+=1
                if "HOSTAPD_PASSWD" in line:
                    self.wifi_passwd=(line.split('=')[1]).rstrip('\r\n')
                    i+=1
            file.close()
            if (i==2):
                print("[Wifi: use hostapd configuration: ssid=%s, passwd=%s]\n" %(self.wifi_ssid, self.wifi_passwd))
            else:
                self.wifi_ssid=WIFI_DEFAULT_SSID
                self.wifi_passwd=WIFI_DEFAULT_PASSWD
                print("[Wifi: use default configuration: ssid=%s, passwd=%s]\n" %(self.wifi_ssid, self.wifi_passwd))
        else:
            print("[Wifi: use default configuration: ssid=%s, passwd=%s]\n" %(self.wifi_ssid, self.wifi_passwd))

    def refresh_network_page(self):
        print("[Refresh network page]\n")

        if self.is_wifi_available():
            print ("wlan0 is available")
            ip_wlan0 = get_ip_address('wlan0')
            if ip_wlan0 == "NA":
                hotspot_status = '<span foreground="#FF0000" font_desc="LiberationSans 25" face="sans">Wifi not started</span>'
                wlan0_status = ''
                self.label_ip_wlan_ssid.set_markup('')
                self.label_ip_wlan_passwd.set_markup('')
            elif ip_wlan0 == WIFI_HOTSPOT_IP:
                hotspot_status = '<span foreground="#00AA00" font_desc="LiberationSans 25" face="sans">Wifi hotspot started</span>'
                wlan0_status =  '<span font_desc="LiberationSans 25" face="sans">NetData over Wifi: http://<b>%s</b>:19999</span>' % ip_wlan0
                self.label_ip_wlan_ssid.set_markup('<span font_desc="LiberationSans 25" face="sans">Wifi SSID : %s</span>' % self.wifi_ssid)
                self.label_ip_wlan_passwd.set_markup('<span font_desc="LiberationSans 25" face="sans">Wifi password : %s</span>' % self.wifi_passwd)
            else:
                hotspot_status = '<span foreground="#FF0000" font_desc="LiberationSans 25" face="sans">Wifi started but not configured as hotspot</span>'
                wlan0_status = '<span font_desc="LiberationSans 25" face="sans">NetData over Wifi: http://<b>%s</b>:19999</span>' % ip_wlan0
                self.label_ip_wlan_ssid.set_markup('')
                self.label_ip_wlan_passwd.set_markup('')

            self.label_ip_wlan0.set_markup(wlan0_status)
        else:
            print ("wlan0 interface not available")
            hotspot_status = '<span foreground="#FF0000" font_desc="LiberationSans 25" face="sans">Wifi not available on board</span>'

        ip_eth0 = get_ip_address('eth0')
        if ip_eth0 != "NA":
            eth0_status = "<span font_desc='LiberationSans 25'>NetData over Ethernet: http://<b>%s</b>:19999</span>" % ip_eth0
        else:
            eth0_status = ''

        self.label_hotspot.set_markup(hotspot_status)
        self.label_ip_eth0.set_markup(eth0_status)


    def on_page_press_event(self, widget, event):
        self.click_time = time()
        print(self.click_time - self.previous_click_time)
        # TODO : a fake click is observed, workaround hereafter
        if (self.click_time - self.previous_click_time) < 0.01:
            self.previous_click_time = self.click_time
        elif (self.click_time - self.previous_click_time) < 0.4:
            print ("double click")
            self.destroy()
        else:
            print ("simple click")
            self.previous_click_time = self.click_time

    def on_switch_activated(self, switch, gparam):
        if switch.get_active():
            wifi_hotspot_start()
        else:
            wifi_hotspot_stop()
        self.refresh_network_page()


# -------------------------------------------------------------------
# -------------------------------------------------------------------
# Get the ip address of board
def get_ip_address(ifname):
    ip = "NA"
    try:
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        ip = socket.inet_ntoa(fcntl.ioctl(
            s.fileno(),
            0x8915,  # SIOCGIFADDR
            struct.pack('256s', bytes(ifname[:15], 'utf-8'))
        )[20:24])
    except socket.error:
        pass
    return ip


# -------------------------------------------------------------------
# -------------------------------------------------------------------
def _load_image_on_button(parent, filename, label_text, scale_w, scale_h):
    # Create box for xpm and label
    box1 = Gtk.HBox(False, 0)
    box1.set_border_width(2)
    # print("[DEBUG] image: %s " % filename)
    # Now on to the image stuff
    pixbuf = GdkPixbuf.Pixbuf.new_from_file_at_scale(
            filename=filename,
            width=scale_w,
            height=scale_h,
            preserve_aspect_ratio=True)
    image = Gtk.Image.new_from_pixbuf(pixbuf)

    # Create a label for the button
    label = Gtk.Label(label_text)

    # Pack the pixmap and label into the box
    box1.pack_start(image, True, False, 3)

    image.show()
    label.show()
    return box1

def wifi_hotspot_start():
    cmd = ["%s/bin/st-hotspot-wifi-service.sh" % DEMO_PATH, "start"]
    proc = subprocess.Popen(cmd, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    result = proc.stdout.read().decode('utf-8')
    return result

def wifi_hotspot_stop():
    cmd = ["%s/bin/st-hotspot-wifi-service.sh" % DEMO_PATH, "stop"]
    proc = subprocess.Popen(cmd, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    result = proc.stdout.read().decode('utf-8')
    return result

def cube_3D_start():
    cmd = ["%s/bin/launch_cube_3D.sh" % DEMO_PATH]
    proc = subprocess.Popen(cmd, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    result = proc.stdout.read().decode('utf-8')
    return result

def demo_AI_start():
    cmd = ["%s/bin/launch_AI.sh" % DEMO_PATH]
    proc = subprocess.Popen(cmd, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    result = proc.stdout.read().decode('utf-8')
    return result

# -------------------------------------------------------------------
# -------------------------------------------------------------------
class MainUIWindow(Gtk.Window):
    def __init__(self):
        Gtk.Window.__init__(self, title="Demo Launcher")
        if SIMULATE > 0:
            self.screen_width = SIMULATE_SCREEN_SIZE_WIDTH
            self.screen_height = SIMULATE_SCREEN_SIZE_HEIGHT
        else:
            self.fullscreen()
            self.screen_width = self.get_screen().get_width()
            self.screen_height = self.get_screen().get_height()

        self.set_default_size(self.screen_width, self.screen_height)
        print("[DEBUG] screen size: %dx%d" % (self.screen_width, self.screen_height))
        self.set_position(Gtk.WindowPosition.CENTER)
        self.connect('destroy', Gtk.main_quit)

        self.previous_click_time=0

        # page for basic information
        self.create_page_icon()

    # Button event of main screen
    def wifi_hotspot_event(self, button):
        ''' start hotspot wifi on board '''
        print("[wifi_hotspot_event start]\n")
        wifi_window = WifiWindow(self, "Wifi hotspot")

        #put it at diaglog http://python-gtk-3-tutorial.readthedocs.io/en/latest/dialogs.html#example
        #self.hide()
        wifi_window.show_all()
        response = wifi_window.run()
        self.show_all()
        wifi_window.destroy()

    def videoplay_event(self, button):
        print("[videoplay_event]\n");
        video_window = GstVideoWindow(self, "playback")

        #put it at diaglog http://python-gtk-3-tutorial.readthedocs.io/en/latest/dialogs.html#example
        # self.hide()
        video_window.show_all()
        response = video_window.run()
        self.show_all()
        video_window.destroy()

    def camera_event(self, button):
        print("[camera_event]\n")

        if os.path.exists("/dev/video0"):
            video_window = GstVideoWindow(self, "camera")

            #put it at diaglog http://python-gtk-3-tutorial.readthedocs.io/en/latest/dialogs.html#example
            # ????? self.hide()
            video_window.show_all()
            response = video_window.run()
            self.show_all()
            video_window.destroy()
        else:
            print("[ERROR] camera not detected\n")
            #TODO : put error message as parameter
            dialog = DialogError(self)
            dialog.run()
            dialog.destroy()

    def ai_event(self, button):
        print("[ai_event]\n")
        demo_AI_start()

    def gpu3d_event(self, button):
        print("[gpu3d_event]\n")
        cube_3D_start()

    def bluetooth_event(self, button):
        print("[bluetooth_event]\n")
        print("[WARNING] bluetooth demo not yet supported\n")
        #subprocess.call(["python", "%s/bin/bluetooth_panel.py" % DEMO_PATH])

    def create_page_icon(self):
        page_main = Gtk.HBox(False, 0)
        page_main.set_border_width(10)

        # create a grid of icon
        icon_grid = Gtk.Grid(column_homogeneous=True, row_homogeneous=True)
        icon_grid.set_column_spacing(5)
        icon_grid.set_row_spacing(5)
        # Button: Wifi web server icon
        button_webserv = Gtk.Button()
        button_webserv.connect("clicked", self.wifi_hotspot_event)
        button_webserv_image = _load_image_on_button(self, "%s/pictures/ST12556_Gateway.png" % DEMO_PATH, "Wifi web server", -1, 128)
        button_webserv.add(button_webserv_image)
        # Button: video icon
        button_videoplay = Gtk.Button()
        button_videoplay.connect("clicked", self.videoplay_event)
        button_videoplay_image = _load_image_on_button(self, "%s/pictures/ST10261_play_button_dark_blue.png" % DEMO_PATH, "Video playback", -1, 128)
        button_videoplay.add(button_videoplay_image)
        # Button: camera icon
        button_camera = Gtk.Button()
        button_camera.connect("clicked", self.camera_event)
        button_camera_image = _load_image_on_button(self, "%s/pictures/ST1077_webcam_dark_blue.png" % DEMO_PATH, "Camera", -1, 128)
        button_camera.add(button_camera_image)
        # Button: ai icon
        button_ai = Gtk.Button()
        button_ai.connect("clicked", self.ai_event)
        button_ai_image = _load_image_on_button(self, "%s/pictures/ST7079_AI_neural_dark_blue.png" % DEMO_PATH, "Artificial Intelligence", -1, 128)
        button_ai.add(button_ai_image)
        # Button: gpu3d icon
        button_gpu3d = Gtk.Button()
        button_gpu3d.connect("clicked", self.gpu3d_event)
        button_gpu3d_image = _load_image_on_button(self, "%s/pictures/ST153_cube_dark_blue.png" % DEMO_PATH, "3D GPU", -1, 128)
        button_gpu3d.add(button_gpu3d_image)
        # Button: BT icon
        button_bluetooth = Gtk.Button()
        button_bluetooth.connect("clicked", self.bluetooth_event)
        button_bluetooth_image = _load_image_on_button(self, "%s/pictures/ST11012_bluetooth_speaker_light_blue.png" % DEMO_PATH, "Bluetooth demo", -1, 128)
        button_bluetooth.add(button_bluetooth_image)

        icon_grid.attach(button_webserv, 0, 0, 1, 1)
        icon_grid.attach(button_videoplay, 1, 0, 1, 1)
        icon_grid.attach(button_camera, 2, 0, 1, 1)

        icon_grid.attach(button_ai, 0, 1, 1, 1)
        icon_grid.attach(button_gpu3d, 1, 1, 1, 1)
        icon_grid.attach(button_bluetooth, 2, 1, 1, 1)

        page_main.add(icon_grid)

        overlay = Gtk.Overlay()
        overlay.add(page_main)
        button_exit = Gtk.Button()
        button_exit.connect("clicked", Gtk.main_quit)
        button_exit_image = _load_image_on_button(self, "%s/pictures/close_50x50.png" % DEMO_PATH, "Exit", -1, 50)
        button_exit.set_halign(Gtk.Align.END)
        button_exit.set_valign(Gtk.Align.START)
        button_exit.add(button_exit_image)
        overlay.add_overlay(button_exit)
        self.add(overlay)

        self.show_all()

# -------------------------------------------------------------------
# -------------------------------------------------------------------
# Main
if __name__ == "__main__":
    # add signal to catch CRTL+C
    import signal
    signal.signal(signal.SIGINT, signal.SIG_DFL)
    try:
        if SIMULATE:
            splScr = SplashScreen("%s/pictures/RS70_ST_Logo_Qi.png" % "./files", 2)
        else:
            splScr = SplashScreen("%s/pictures/RS70_ST_Logo_Qi.png" % DEMO_PATH, 2)
        Gtk.main()

        win = MainUIWindow()
        win.connect("delete-event", Gtk.main_quit)
        win.show_all()
    except Exception as exc:
        print("Main Exception: ", exc )

    Gtk.main()
