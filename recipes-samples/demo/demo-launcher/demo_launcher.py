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
    DEMO_PATH = os.environ['HOME']+"/Desktop/launcher"
else:
    DEMO_PATH = "/usr/local/demo"

# -------------------------------------------------------------------
# -------------------------------------------------------------------
# CONSTANT VALUES
#
SIMULATE_SCREEN_SIZE_WIDTH  = 800
SIMULATE_SCREEN_SIZE_HEIGHT = 480

ICON_SIZE_BIG = 180
ICON_SIZE_SMALL = 128

WIFI_HOTSPOT_IP="192.168.72.1"

WIFI_DEFAULT_SSID="STDemoNetwork"
WIFI_DEFAULT_PASSWD="stm32mp1"

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
        rgba = Gdk.RGBA(0.31, 0.32, 0.31, 0.8)
        self.override_background_color(Gtk.StateType.NORMAL, rgba)

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
            self.filename = "%s/media/ST2297_visionv3.webm" % DEMO_PATH
            self.set_video_filename("%s/media/ST2297_visionv3.webm" % DEMO_PATH)

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

        #width, height = self.get_size()
        #self.set_default_size(width, height)
        self.maximize()
        self.set_decorated(False)
        rgba = Gdk.RGBA(0.31, 0.32, 0.31, 0.8)
        self.override_background_color(0,rgba)

        mainvbox = self.get_content_area()

        self.page_ip = Gtk.VBox()
        self.page_ip.set_border_width(10)
        self.set_border_width(10)

        self.title = Gtk.Label()
        self.title.set_markup("<span font='30' color='#FFFFFFFF'><b>Wifi Hotspot informations</b></span>")
        self.page_ip.add(self.title)
        self.label_eth = Gtk.Label()
        self.label_eth.set_markup("<span font='20' color='#FFFFFFFF'>\nNetData over Ethernet:</span>")
        self.label_eth.set_xalign (0.0)
        self.label_ip_eth0 = Gtk.Label()
        self.label_ip_eth0.set_xalign (0.0)
        self.label_wifi = Gtk.Label()
        self.label_wifi.set_markup("<span font='20' color='#FFFFFFFF'>\nNetData over Wifi:</span>")
        self.label_wifi.set_xalign (0.0)
        self.label_ip_wlan0 = Gtk.Label()
        self.label_ip_wlan0.set_xalign (0.0)
        self.label_hotspot = Gtk.Label()
        self.label_hotspot.set_xalign (0.0)
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

        self.info_grid.attach(self.label_eth, 0, 1, 2, 1)
        self.info_grid.attach(self.label_ip_eth0, 0, 2, 2, 1)
        self.info_grid.attach(self.label_wifi, 0, 4, 1, 1)

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

            self.info_grid.attach(self.hostspot_switch, 0, 5, 1, 1)
            self.info_grid.attach(self.label_hotspot, 1, 5, 1, 1)
            self.info_grid.attach(self.label_ip_wlan0, 0, 6, 2, 1)
            self.info_grid.attach(self.label_ip_wlan_ssid, 0, 7, 2, 1)
            self.info_grid.attach(self.label_ip_wlan_passwd, 0, 8, 2, 1)
        else:
            print ("wlan0 interface not available")
            self.info_grid.attach(self.label_hotspot, 0, 5, 2, 1)

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
                hotspot_status = "<span font='15' color='#FF0000FF'>  Wifi not started</span>"
                wlan0_status = ''
                self.label_ip_wlan_ssid.set_markup('')
                self.label_ip_wlan_passwd.set_markup('')
            elif ip_wlan0 == WIFI_HOTSPOT_IP:
                hotspot_status = "<span font='15' color='#00AA00FF'>  Wifi hotspot started</span>"
                wlan0_status =  "<span font='15' color='#FFFFFFFF'>  http://%s:19999</span>" % ip_wlan0
                self.label_ip_wlan_ssid.set_markup("<span font='15' color='#FFFFFFFF'>  Wifi SSID : %s</span>" % self.wifi_ssid)
                self.label_ip_wlan_passwd.set_markup("<span font='15' color='#FFFFFFFF'>  Wifi password : %s</span>" % self.wifi_passwd)
            else:
                hotspot_status = "<span font='15' color='#FF0000FF'>Wifi started but not configured as hotspot</span>"
                wlan0_status = "<span font='15' color='#FFFFFFFF'>NetData over Wifi: http://%s:19999</span>" % ip_wlan0
                self.label_ip_wlan_ssid.set_markup('')
                self.label_ip_wlan_passwd.set_markup('')

            self.label_ip_wlan0.set_markup(wlan0_status)
        else:
            print ("wlan0 interface not available")
            hotspot_status = "<span font='15' color='#FF0000FF'>  Wifi not available on board</span>"

        ip_eth0 = get_ip_address('eth0')
        if ip_eth0 != "NA":
            eth0_status = "<span font='15' color='#FFFFFFFF'>  http://%s:19999</span>" % ip_eth0
        else:
            eth0_status = "<span font='15' color='#FF0000FF'>  No Ethernet connection</span>"

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
def _load_image_eventBox(parent, filename, label_text1, label_text2, scale_w, scale_h):
    # Create box for xpm and label
    box = Gtk.VBox(False, 0)
    # Create an eventBox
    eventBox = Gtk.EventBox()
    # Now on to the image stuff
    pixbuf = GdkPixbuf.Pixbuf.new_from_file_at_scale(
            filename=filename,
            width=scale_w,
            height=scale_h,
            preserve_aspect_ratio=True)
    image = Gtk.Image.new_from_pixbuf(pixbuf)

    label = Gtk.Label()
    label.set_markup("<span font='15' color='#39A9DCFF'>%s\n</span>"
                     "<span font='15' color='#002052FF'>%s</span>" % (label_text1, label_text2))
    label.set_justify(Gtk.Justification.CENTER)
    label.set_line_wrap(True)

    # Pack the pixmap and label into the box
    box.pack_start(image, True, False, 0)
    box.pack_start(label, True, False, 0)

    # Add the image to the eventBox
    eventBox.add(box)

    return eventBox

def _load_image_Box(parent, filename, label_text, scale_w, scale_h):
    # Create box for xpm and label
    box = Gtk.VBox(False, 0)
    # print("[DEBUG] image: %s " % filename)
    # Now on to the image stuff
    pixbuf = GdkPixbuf.Pixbuf.new_from_file_at_scale(
            filename=filename,
            width=scale_w,
            height=scale_h,
            preserve_aspect_ratio=True)
    image = Gtk.Image.new_from_pixbuf(pixbuf)

    # Create a label for the button
    label0 = Gtk.Label() #for padding

    label1 = Gtk.Label()
    label1.set_markup("<span font='14' color='#FFFFFFFF'><b>%s</b></span>\n"
                      "<span font='10' color='#FFFFFFFF'>Dual Arm&#174; Cortex&#174;-A7</span>\n"
                      "<span font='10' color='#FFFFFFFF'>+</span>\n"
                      "<span font='10' color='#FFFFFFFF'>Copro Arm&#174; Cortex&#174;-M4</span>\n" % label_text)
    label1.set_justify(Gtk.Justification.CENTER)
    label1.set_line_wrap(True)

    label2 = Gtk.Label() #for padding

    label3 = Gtk.Label()
    label3.set_markup("<span font='10' color='#FFFFFFFF'><b>Python GTK launcher</b></span>\n")
    label3.set_justify(Gtk.Justification.CENTER)
    label3.set_line_wrap(True)

    # Pack the pixmap and label into the box
    box.pack_start(label0, True, False, 0)
    box.pack_start(image, True, False, 0)
    box.pack_start(label1, True, False, 0)
    box.pack_start(label2, True, False, 0)
    box.pack_start(label3, True, False, 0)

    return box

def _load_image_on_button(parent, filename, label_text, scale_w, scale_h):
    # Create box for xpm and label
    box = Gtk.HBox(False, 0)
    box.set_border_width(2)
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
    box.pack_start(image, True, False, 3)

    image.show()
    label.show()
    return box

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
        self.set_decorated(False)
        self.modify_bg(Gtk.StateType.NORMAL, Gdk.Color(65535, 65535, 65535))
        if SIMULATE > 0:
            self.screen_width = SIMULATE_SCREEN_SIZE_WIDTH
            self.screen_height = SIMULATE_SCREEN_SIZE_HEIGHT
        else:
            #self.fullscreen()
            self.maximize()
            self.screen_width = self.get_screen().get_width()
            self.screen_height = self.get_screen().get_height()

        if self.screen_width == 720:
            self.icon_size = ICON_SIZE_BIG
            self.board_name = "Evaluation board"
        else:
            self.icon_size = ICON_SIZE_SMALL
            self.board_name = "Discovery kit"

        self.set_default_size(self.screen_width, self.screen_height)
        print("[DEBUG] screen size: %dx%d" % (self.screen_width, self.screen_height))
        self.set_position(Gtk.WindowPosition.CENTER)
        self.connect('destroy', Gtk.main_quit)

        self.previous_click_time=0

        # page for basic information
        self.create_page_icon()

    def no_camera(self):
        dialog = Gtk.Dialog("Error", self, 0, (Gtk.STOCK_OK, Gtk.ResponseType.OK))
        dialog.set_decorated(False)
        width, height = self.get_size()
        dialog.set_default_size(width, height)
        rgba = Gdk.RGBA(0.31, 0.32, 0.31, 0.8)
        dialog.override_background_color(0,rgba)

        label0 = Gtk.Label() #for padding

        label1 = Gtk.Label("Webcam is not connected :\n/dev/video0 doesn't exist")
        label1.set_markup("<span font='15' color='#FFFFFFFF'>Webcam is not connected:\n</span>"
                          "<span font='15' color='#FFFFFFFF'>/dev/video0 doesn't exist\n</span>")
        label1.set_justify(Gtk.Justification.CENTER)
        label1.set_line_wrap(True)

        label2 = Gtk.Label() #for padding

        # Create a centering alignment object
        align = Gtk.Alignment()
        align.set(0.5, 0, 0, 0)

        dialog.vbox.pack_start(label0, True, False, 0)
        dialog.vbox.pack_start(label1, True, True,  0)
        dialog.vbox.pack_start(align,  True, True,  0)
        dialog.vbox.pack_start(label2, True, False, 0)

        dialog.action_area.reparent(align)
        dialog.show_all()

        dialog.run()
        print("INFO dialog closed")

        dialog.destroy()

    # Button event of main screen
    def highlight_eventBox(self, widget, event):
        ''' highlight the eventBox widget '''
        print("[highlight_eventBox start]\n")
        rgba = Gdk.RGBA(0.0, 0.0, 0.0, 0.1)
        widget.override_background_color(0,rgba)
        print("[highlight_eventBox stop]\n")

    def wifi_hotspot_event(self, widget, event):
        ''' start hotspot wifi on board '''
        print("[wifi_hotspot_event start]\n")
        wifi_window = WifiWindow(self, "Wifi hotspot")

        #put it at diaglog http://python-gtk-3-tutorial.readthedocs.io/en/latest/dialogs.html#example
        #self.hide()
        wifi_window.show_all()
        response = wifi_window.run()
        wifi_window.destroy()
        print("[wifi_hotspot_event stop]\n")
        rgba = Gdk.RGBA(0.0, 0.0, 0.0, 0.0)
        widget.override_background_color(0,rgba)

    def videoplay_event(self, widget, event):
        print("[videoplay_event start]\n");
        video_window = GstVideoWindow(self, "playback")

        #put it at diaglog http://python-gtk-3-tutorial.readthedocs.io/en/latest/dialogs.html#example
        # self.hide()
        video_window.show_all()
        response = video_window.run()
        video_window.destroy()
        print("[videoplay_event stop]\n");
        rgba = Gdk.RGBA(0.0, 0.0, 0.0, 0.0)
        widget.override_background_color(0,rgba)

    def camera_event(self, widget, event):
        print("[camera_event start]\n")

        if os.path.exists("/dev/video0"):
            video_window = GstVideoWindow(self, "camera")

            #put it at diaglog http://python-gtk-3-tutorial.readthedocs.io/en/latest/dialogs.html#example
            # ????? self.hide()
            video_window.show_all()
            response = video_window.run()
            video_window.destroy()
        else:
            print("[ERROR] camera not detected\n")
            self.no_camera()

        print("[camera_event stop]\n")
        rgba = Gdk.RGBA(0.0, 0.0, 0.0, 0.0)
        widget.override_background_color(0,rgba)

    def ai_event(self, widget, event):
        print("[ai_event start]\n")
        demo_AI_start()
        print("[ai_event stop]\n")
        rgba = Gdk.RGBA(0.0, 0.0, 0.0, 0.0)
        widget.override_background_color(0,rgba)

    def gpu3d_event(self, widget, event):
        print("[gpu3d_event start]\n")
        cube_3D_start()
        print("[gpu3d_event stop]\n")
        rgba = Gdk.RGBA(0.0, 0.0, 0.0, 0.0)
        widget.override_background_color(0,rgba)

    def bluetooth_event(self, widget, event):
        print("[bluetooth_event start]\n")
        print("[WARNING] bluetooth demo not yet supported\n")
        #subprocess.call(["python", "%s/bin/bluetooth_panel.py" % DEMO_PATH])
        print("[bluetooth_event stop]\n")
        rgba = Gdk.RGBA(0.0, 0.0, 0.0, 0.0)
        widget.override_background_color(0,rgba)

    def create_page_icon(self):
        page_main = Gtk.HBox(False, 0)
        page_main.set_border_width(0)

        # create a grid of icon
        icon_grid = Gtk.Grid(column_homogeneous=True, row_homogeneous=True)
        icon_grid.set_column_spacing(20)
        icon_grid.set_row_spacing(20)

        # STM32MP1 Logo and info area
        logo_info_area = _load_image_Box(self, "%s/pictures/ST11249_Module_STM32MP1_alpha.png" % DEMO_PATH, self.board_name, -1, 160)
        rgba = Gdk.RGBA(0.31, 0.32, 0.31, 1.0)
        logo_info_area.override_background_color(0,rgba)

        # Button: Wifi web server icon
        eventBox_webserv = _load_image_eventBox(self, "%s/pictures/ST12556_Gateway.png" % DEMO_PATH, "Wifi", "Hotspot", -1, self.icon_size)
        eventBox_webserv.connect("button_release_event", self.wifi_hotspot_event)
        eventBox_webserv.connect("button_press_event", self.highlight_eventBox)

        # Button: camera icon
        eventBox_camera = _load_image_eventBox(self, "%s/pictures/ST1077_webcam_dark_blue.png" % DEMO_PATH, "Camera",  "preview", -1, self.icon_size)
        eventBox_camera.connect("button_release_event", self.camera_event)
        eventBox_camera.connect("button_press_event", self.highlight_eventBox)

        # Button: video icon
        eventBox_videoplay = _load_image_eventBox(self, "%s/pictures/Video_playback_logo.png" % DEMO_PATH, "Video", "playback", -1, self.icon_size)
        eventBox_videoplay.connect("button_release_event", self.videoplay_event)
        eventBox_videoplay.connect("button_press_event", self.highlight_eventBox)

        # Button: ai icon
        eventBox_ai = _load_image_eventBox(self, "%s/pictures/ST7079_AI_neural_pink.png" % DEMO_PATH, "Artificial", "Intelligence", -1, self.icon_size)
        eventBox_ai.connect("button_release_event", self.ai_event)
        eventBox_ai.connect("button_press_event", self.highlight_eventBox)

        # Button: gpu3d icon
        eventBox_gpu3d = _load_image_eventBox(self, "%s/pictures/ST153_cube_purple.png" % DEMO_PATH, "3D", "GPU", -1, self.icon_size)
        eventBox_gpu3d.connect("button_release_event", self.gpu3d_event)
        eventBox_gpu3d.connect("button_press_event", self.highlight_eventBox)

        # Button: BT icon
        eventBox_bluetooth = _load_image_eventBox(self, "%s/pictures/Bluetooth_speaker_logo.png" % DEMO_PATH, "Bluetooth", "speaker", -1, self.icon_size)
        eventBox_bluetooth.connect("button_release_event", self.bluetooth_event)
        eventBox_bluetooth.connect("button_press_event", self.highlight_eventBox)

        icon_grid.attach(logo_info_area, 3, 0, 1, 2)
        icon_grid.attach(eventBox_webserv, 0, 0, 1, 1)
        icon_grid.attach(eventBox_camera, 1, 0, 1, 1)
        icon_grid.attach(eventBox_videoplay, 2, 0, 1, 1)

        icon_grid.attach(eventBox_ai, 0, 1, 1, 1)
        icon_grid.attach(eventBox_gpu3d, 1, 1, 1, 1)
        icon_grid.attach(eventBox_bluetooth, 2, 1, 1, 1)

        page_main.add(icon_grid)

        overlay = Gtk.Overlay()
        overlay.add(page_main)
        button_exit = Gtk.Button()
        button_exit.connect("clicked", Gtk.main_quit)
        button_exit_image = _load_image_on_button(self, "%s/pictures/close_70x70_white.png" % DEMO_PATH, "Exit", -1, 50)
        button_exit.set_halign(Gtk.Align.END)
        button_exit.set_valign(Gtk.Align.START)
        button_exit.add(button_exit_image)
        button_exit.set_relief(Gtk.ReliefStyle.NONE)
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
        win = MainUIWindow()
        win.connect("delete-event", Gtk.main_quit)
        win.show_all()
    except Exception as exc:
        print("Main Exception: ", exc )

    Gtk.main()
