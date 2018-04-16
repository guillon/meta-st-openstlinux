#!/usr/bin/python3
# to debug this script:
#      python3 -m pdb ./sensors_temperature.py
#
import gi
gi.require_version('Gtk', '3.0')
from gi.repository import Gtk
from gi.repository import GObject
from gi.repository import Gdk
from gi.repository import GLib
from gi.repository import GdkPixbuf
import cairo

import random
import math
import os
import socket
from collections import deque
from time import sleep, time

#
# For simulating the presence of sensor, please use
# the variable SIMULATE_SENSORS = 1
# If SIMULATE_SENSORS = 1 then
#    the picture/icon must be present on pictures directory
#
SIMULATE_SENSORS = 0

ICON_PICTURES_PATH = "/usr/share/sensors_temperature"

WITH_PRESSURE = 1
WITH_GYRO = 0

# -------------------------------------------------------------------
# -------------------------------------------------------------------
# CONSTANT VALUES
#
SIMULATE_SCREEN_SIZE_WIDTH  = 480
SIMULATE_SCREEN_SIZE_HEIGHT = 800
DEFAULT_SCREEN_WIDTH = 400
DEFAULT_SCREEN_HEIGHT = 600

# Maximum of value to display on graph
NUM_GRAPH_SAMPLE = 112
# time between each sensor mesearuement (1s)
TIME_UPATE = 2000

# -------------------------------------------------------------------
# -------------------------------------------------------------------
# SPLASH SCREEN class
#    the splash screen display a logo and the different step of boot
#
class SplashScreen():
    def __init__(self, picture_filename, timeout):
        #DONT connect 'destroy' event here!
        self.window = Gtk.Window()
        self.window.set_title('Sensor IKS01A2')
        self.window.set_position(Gtk.WindowPosition.CENTER)
        self.window.set_decorated(False)
        if SIMULATE_SENSORS > 0:
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

# Gstreamer video playback file widget
class GstVideoWidget(Gtk.Box):
    def __init__(self):
        super().__init__()
        self.connect('realize', self._on_realize)

        self.player = Gst.ElementFactory.make("playbin", "player")
        self.gtksink = Gst.ElementFactory.make("gtksink", "gtksink")
        self.fakesink = Gst.ElementFactory.make("fakesink", "fakesink")
        self.player.set_property("video-sink", self.gtksink)
        self.player.set_property("audio-sink", self.fakesink)
        # TODO: change the audiosink

        bus = self.player.get_bus()
        bus.add_signal_watch()
        bus.connect("message", self.on_message)

    def set_file(self, filename):
        if os.path.isfile(filename):
            filepath = os.path.realpath(filename)
            self.player.set_property("uri", "file://%s" % filepath)

    def start(self):
        self.player.set_state(Gst.State.PLAYING)
    def stop(self):
        self.player.set_state(Gst.State.NULL)
    def pause(self):
        self.player.set_state(Gst.State.PAUSED)

    def on_message(self, bus, message):
        t = message.type
        if t == Gst.MessageType.EOS:
            self.player.set_state(Gst.State.NULL)
            Gtk.main_quit()
        elif t == Gst.MessageType.ERROR:
            self.player.set_state(Gst.State.NULL)
            err, debug = message.parse_error()
            print("Error:           %s" % err, debug)
            Gtk.main_quit()

    def _on_realize(self, widget):
        self.pack_start(self.gtksink.props.widget, True, True, 0)
        self.gtksink.props.widget.show()

# video Window
class GstVideoWindow(Gtk.Dialog):
    def __init__(self, parent, pipeline):
        Gtk.Dialog.__init__(self, "Video", parent, 0)

        self.fullscreen()
        #self.maximize()

        mainvbox = self.get_content_area()

        button_hbox = Gtk.HBox(False, 0)
        if "videotestsrc" == pipeline:
            self.video_widget = GstVideoTestSrcWidget ()
            self.filename = "videotestsrc"
        else:
            self.video_widget = GstVideoWidget()
            self.filename = None

        mainvbox.pack_start(button_hbox, False, False, 3)
        mainvbox.pack_start(self.video_widget, True, True, 3)

        #start_button = Gtk.Button().new_with_label("Start")
        start_button = Gtk.Button()
        start_button.connect("clicked", self.start_videoplayback)
        start_image = _load_image_on_button(self, "%s/RS10261_play_button_pink.png" % ICON_PICTURES_PATH, "start", -1, 50)
        start_button.add(start_image)
        start_button.show()

        pause_button = Gtk.Button()
        pause_button.connect("clicked", self.pause_videoplayback)
        pause_image = _load_image_on_button(self, "%s/RS10258_pause_button_pink.png" % ICON_PICTURES_PATH, "pause", -1, 50)
        pause_button.add(pause_image)
        pause_button.show()

        stop_button = Gtk.Button()
        stop_button.connect("clicked", self.stop_videoplayback)
        stop_image = _load_image_on_button(self, "%s/RS10259_stop_button_pink.png" % ICON_PICTURES_PATH, "stop", -1, 50)
        stop_button.add(stop_image)
        stop_button.show()

        if not "videotestsrc" == pipeline:
            search_button = Gtk.Button()
            search_button.connect("clicked", self.search_videoplayback)
            search_image = _load_image_on_button(self, "%s/RS9890_Icon3_pink.png" % ICON_PICTURES_PATH, "search", -1, 50)
            search_button.add(search_image)
            search_button.show()

        quit_button = Gtk.Button()
        quit_button.connect("clicked", self.quit_videoplayback)
        quit_image = _load_image_on_button(self, "%s/RS518_house_light_blue.png" % ICON_PICTURES_PATH, "stop", -1, 50)
        quit_button.add(quit_image)
        quit_button.show()

        button_hbox.add(start_button)
        button_hbox.add(pause_button)
        button_hbox.add(stop_button)
        if not "videotestsrc" == pipeline:
            button_hbox.add(search_button)
        button_hbox.add(quit_button)

        self.show_all()


    def set_video_filename(self, filename):
        self.video_widget.set_file(filename)
    def start_videoplayback(self, button):
        if not self.filename is None:
            self.video_widget.start()
    def pause_videoplayback(self, button):
        print("[DEBUG] [VIDEO WIN] ask to pause")
        print("   filename = ", self.filename)
        if not self.filename is None:
            self.video_widget.pause()
    def stop_videoplayback(self, button):
        if not self.filename is None:
            self.video_widget.stop()
    def search_videoplayback(self, button):
        ''' '''
        dialog = Gtk.FileChooserDialog("Please choose a file", self,
                                       Gtk.FileChooserAction.OPEN,
                                       (Gtk.STOCK_CANCEL, Gtk.ResponseType.CANCEL,
                                        Gtk.STOCK_OPEN, Gtk.ResponseType.OK))

        # TODO: add video filter
        #self.add_filters(dialog)
        dialog.fullscreen()
        response = dialog.run()
        if response == Gtk.ResponseType.OK:
            print("File selected: " + dialog.get_filename())
            self.filename = dialog.get_filename()
            self.set_video_filename(self.filename)
        elif response == Gtk.ResponseType.CANCEL:
            print("Close FileChooser")
            self.filename = None

        dialog.destroy()

    def add_filters(self, dialog):
        filter_text = Gtk.FileFilter()
        filter_text.set_name("Text files")
        filter_text.add_mime_type("text/plain")
        dialog.add_filter(filter_text)

        filter_py = Gtk.FileFilter()
        filter_py.set_name("Python files")
        filter_py.add_mime_type("text/x-python")
        dialog.add_filter(filter_py)

        filter_any = Gtk.FileFilter()
        filter_any.set_name("Any files")
        filter_any.add_pattern("*")
        dialog.add_filter(filter_any)


    def quit_videoplayback(self, button):
        ''' '''
        self.stop_videoplayback(button)
        self.destroy()

# -------------------------------------------------------------------
# -------------------------------------------------------------------
# Get the ip address of board
def board_get_ip_address():
    ip = "0.0.0.0"
    try:
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        s.connect(("8.8.8.8", 80))
        ip =  s.getsockname()[0]
        s.close()
    except socket.error:
        pass
    return ip

# -------------------------------------------------------------------
# -------------------------------------------------------------------
def _load_image_on_button(parent, filename, label_text, scale_w, scale_h):
    # Create box for xpm and label
    box1 = Gtk.HBox(False, 0)
    box1.set_border_width(2)
    # Now on to the image stuff
    #image = Gtk.Image()
    #image.set_from_file(filename)
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
    #box1.pack_start(label, False, False, 3)

    image.show()
    label.show()
    return box1
# -------------------------------------------------------------------
# -------------------------------------------------------------------
def _load_image(parent, filename_without_prefix):
    img = Gtk.Image()
    img.set_from_file("%s/%s" % (ICON_PICTURES_PATH, filename_without_prefix))
    return img

# scale_width and scale_height are sht siez disired after scale,
# It can be -1 for no scale on one value
def _load_image_constrained(parent, filename_without_prefix, scale_width, scale_height):
    pixbuf = GdkPixbuf.Pixbuf.new_from_file_at_scale(
            filename="%s/%s" % (ICON_PICTURES_PATH, filename_without_prefix),
            width=scale_width,
            height=scale_height,
            preserve_aspect_ratio=True)
    image = Gtk.Image.new_from_pixbuf(pixbuf)
    return image

# -------------------------------------------------------------------
# -------------------------------------------------------------------
class Sensors():
    key_temperature = 'temp'
    key_humidity    = 'humidity'
    key_pressure    = 'pressure'

    key_accelerometer = 'accel'
    key_gyroscope     = 'gyro'
    key_magnetometer  = 'magneto'

    driver_name_temperature   = 'driver_temp'
    driver_name_humidity      = 'driver_humidity'
    driver_name_pressure      = 'driver_pressure'
    driver_name_accelerometer = 'driver_accel'
    driver_name_gyroscope     = 'driver_gyro'
    driver_name_magnetometer  = 'driver_magneto'


    prefix_temp     = "in_temp_"
    prefix_humidity = "in_humidityrelative_"
    prefix_pressure = "in_pressure_"

    prefix_accel   = "in_accel_"
    prefix_gyro    = "in_anglvel_"
    prefix_magneto = "in_magn_"    # TODO: verify

    def __init__(self):
        ''' '''
        self.sensor_dictionnary = {}

    def init_pressure_sampling_frequency(self):
        if not self.sensor_dictionnary[self.key_pressure] is None and len(self.sensor_dictionnary[self.key_pressure]) > 5:
            with open(self.sensor_dictionnary[self.key_pressure] + 'sampling_frequency', 'w') as f:
                f.write('10')

    def found_iio_device(self, data):
        prefix = "/sys/bus/iio/devices/"
        try:
            for filefolder in os.listdir(prefix):
                if os.path.exists(prefix + '/' + filefolder + '/' + data):
                    ''' return directory which contains "data" '''
                    return (prefix + '/' + filefolder + '/')
        except OSError:
            pass
        return None

    def driver_name_iio_device(self, key):
        if SIMULATE_SENSORS > 0:
            val_name = "-Simulated-"
        else:
            if self.sensor_dictionnary[key] == None:
                val_name = "-Not present / Simulated-"
            else:
                try:
                    with open(self.sensor_dictionnary[key] + "name", 'r') as f:
                        val_name = f.read()
                except Exception as exc:
                    val_name = "-Not present-"
        return val_name

    def found_all_sensor_path(self):
        self.sensor_dictionnary[self.key_temperature] = self.found_iio_device("in_temp_raw")
        self.sensor_dictionnary[self.key_humidity]    = self.found_iio_device("in_humidityrelative_raw")
        self.sensor_dictionnary[self.key_pressure]    = self.found_iio_device("in_pressure_raw")

        self.sensor_dictionnary[self.key_accelerometer] = self.found_iio_device("in_accel_x_raw")
        self.sensor_dictionnary[self.key_gyroscope] = self.found_iio_device("in_anglvel_x_raw")
        self.sensor_dictionnary[self.key_magnetometer] = "" #TODO

        self.sensor_dictionnary[self.driver_name_temperature] = self.driver_name_iio_device(self.key_temperature)
        self.sensor_dictionnary[self.driver_name_humidity]    = self.driver_name_iio_device(self.key_humidity)
        self.sensor_dictionnary[self.driver_name_pressure]    = self.driver_name_iio_device(self.key_pressure)

        self.sensor_dictionnary[self.driver_name_accelerometer] = self.driver_name_iio_device(self.key_accelerometer)
        self.sensor_dictionnary[self.driver_name_gyroscope]     = self.driver_name_iio_device(self.driver_name_humidity)
        self.sensor_dictionnary[self.driver_name_magnetometer]  = self.driver_name_iio_device(self.driver_name_pressure)

        self.init_pressure_sampling_frequency()
        print("[DEBUG] " , self.key_temperature, " -> ", self.sensor_dictionnary[self.key_temperature])
        print("[DEBUG] " , self.key_humidity, " -> ", self.sensor_dictionnary[self.key_humidity])
        print("[DEBUG] " , self.key_pressure, " -> ", self.sensor_dictionnary[self.key_pressure])
        print("[DEBUG] " , self.key_accelerometer, " -> ", self.sensor_dictionnary[self.key_accelerometer])
        print("[DEBUG] " , self.key_gyroscope, " -> ", self.sensor_dictionnary[self.key_gyroscope])
        print("[DEBUG] " , self.key_magnetometer, " -> ", self.sensor_dictionnary[self.key_magnetometer])


    def read_sensor_basic(self, key, prefix):
        if SIMULATE_SENSORS > 0:
            if self.key_temperature == key:
                return random.uniform(0.0, 50.0)
            elif self.key_humidity == key:
                return random.uniform(0.0, 100.0)
            else:
                return random.uniform(800.0, 1200.0)
        else:
            if self.sensor_dictionnary[key] is None or len(self.sensor_dictionnary[key]) < 5:
                if self.key_temperature == key:
                   return random.uniform(0.0, 50.0)
                elif self.key_humidity == key:
                    return random.uniform(0.0, 100.0)
                else:
                    return random.uniform(800.0, 1200.0)
            with open(self.sensor_dictionnary[key] + prefix + 'raw', 'r') as f:
                raw = float(f.read())
            with open(self.sensor_dictionnary[key] + prefix + 'scale', 'r') as f:
                scale = float(f.read())
            if self.key_pressure != key:
                with open(self.sensor_dictionnary[key] + prefix + 'offset', 'r') as f:
                    offset = float(f.read())
            else:
                offset = 0.0
                scale = scale * 10

            temp = (offset + raw) * scale
            #print("[DEBUG] [%s] %f" % (key, temp))
            return temp
    def read_sensor_move(self, key, prefix):
        if SIMULATE_SENSORS > 0:
            in_x = random.randint(-180, 180)
            in_y = random.randint(-180, 180)
            in_z = random.randint(-180, 180)
            return [in_x, in_y, in_z]
        else:
            if self.sensor_dictionnary[key] is None or len(self.sensor_dictionnary[key]) < 5:
                in_x = random.randint(-180, 180)
                in_y = random.randint(-180, 180)
                in_z = random.randint(-180, 180)
                return [in_x, in_y, in_z]
            with open(self.sensor_dictionnary[key] + prefix + 'x_raw', 'r') as f:
                scale = float(f.read())
            with open(self.sensor_dictionnary[key] + prefix + 'x_scale', 'r') as f:
                raw = float(f.read())
            in_x = int(raw * scale * 256.0 / 9.81)
            with open(self.sensor_dictionnary[key] + prefix + 'y_raw', 'r') as f:
                scale = float(f.read())
            with open(self.sensor_dictionnary[key] + prefix + 'y_scale', 'r') as f:
                raw = float(f.read())
            in_y = int(raw * scale * 256.0 / 9.81)
            with open(self.sensor_dictionnary[key] + prefix + 'z_raw', 'r') as f:
                scale = float(f.read())
            with open(self.sensor_dictionnary[key] + prefix + 'z_scale', 'r') as f:
                raw = float(f.read())
            in_z = int(raw * scale * 256.0 / 9.81)
            return [in_x, in_y, in_z]

    def read_temperature(self):
        return self.read_sensor_basic(self.key_temperature, self.prefix_temp)
    def read_humidity(self):
        return self.read_sensor_basic(self.key_humidity, self.prefix_humidity)
    def read_pressure(self):
        return self.read_sensor_basic(self.key_pressure, self.prefix_pressure)

    def read_accelerometer(self):
        return self.read_sensor_move(self.key_accelerometer, self.prefix_accel)
    def read_gyroscope(self):
        return self.read_sensor_move(self.key_gyroscope, self.prefix_gyro)
    def read_magnetometer(self):
        return self.read_sensor_move(self.key_magnetometer, self.prefix_magneto)

    def get_driver_name_temperature(self):
        return self.sensor_dictionnary[self.driver_name_temperature]
    def get_driver_name_humidity(self):
        return self.sensor_dictionnary[self.driver_name_humidity]
    def get_driver_name_pressure(self):
        return self.sensor_dictionnary[self.driver_name_pressure]
    def get_driver_name_accelerometer(self):
        return self.sensor_dictionnary[self.driver_name_accelerometer]
    def get_driver_name_gyroscope(self):
        return self.sensor_dictionnary[self.driver_name_gyroscope]
    def get_driver_name_magnetometer(self):
        return self.sensor_dictionnary[self.driver_name_magnetometer]

    def calculate_imu(self, accel):
        x = accel[0]
        y = accel[1]
        z = accel[2]
        pitch = round(math.atan(x / math.sqrt(y * y + z * z)) * 180.0 * math.pi)
        roll  = round(math.atan(y / math.sqrt(x * x + z * z)) * 180.0 * math.pi)
        yaw   = round(math.atan(z / math.sqrt(x * x + z * z)) * 180.0 * math.pi)

        return [pitch, roll, yaw]
    def get_imu_orientation(self, pitch, roll):
        if abs(pitch) > 35:
            ''' (rotation > 0) ? ORIENTATION_LEFT_UP : ORIENTATION_RIGHT_UP;'''
            if pitch > 0:
                return "left-up"
            else:
                return "right-up"
        else:
            if abs(roll) > 35:
                '''ret = (rotation > 0) ? ORIENTATION_BOTTOM_UP : ORIENTATION_NORMAL;'''
                if roll > 0:
                    return "bottom-up"
                else:
                    return "normal"

# -------------------------------------------------------------------
# -------------------------------------------------------------------
class MainUIWindow(Gtk.Window):
    def __init__(self):
        Gtk.Window.__init__(self, title="Sensor usage")
        if SIMULATE_SENSORS > 0:
            self.screen_width = SIMULATE_SCREEN_SIZE_WIDTH
            self.screen_height = SIMULATE_SCREEN_SIZE_HEIGHT
        else:
            self.fullscreen()
            #self.maximize()
            self.screen_width = self.get_screen().get_width()
            self.screen_height = self.get_screen().get_height()

        self.set_default_size(self.screen_width, self.screen_height)
        print("[DEBUG] screen size: %dx%d" % (self.screen_width, self.screen_height))
        self.set_position(Gtk.WindowPosition.CENTER)
        self.connect('destroy', Gtk.main_quit)

        # search sensor interface
        self.sensors = Sensors()
        self.sensors.found_all_sensor_path()

        # variable for sensor
        self._axis_ranges = {}  # {axis_num: [min_seen, max_seen]}
        self._axis_values = {}  # {axis_num: deque([val0, ..., valN])}
        # create nodebook
        self.notebook = Gtk.Notebook()
        self.add(self.notebook)

        # page for basic information
        # current temperature / Humidity / Pressure
        self.create_notebook_page_basic_sensor()

        # page for graph
        # temperature / Humidity / Pressure
        self.create_notebook_page_graph_sensor()

        # page for accel / gyro / magentic
        self.create_notebook_page_basic_accel()

        # page for IMU
        self.create_notebook_page_imu()

        # page sensor information
        self.create_notebook_page_sensor_information()

        # page to indicate the ip address of board
        self.create_notebook_page_ip_address()

        # page for video playback
        if HAVE_VIDEO_SINK:
            self.create_notebook_page_video()

        # page for quitting application
        self.create_notebook_page_exit()

        # Add a timer callback to update
        # this takes 2 args: (how often to update in millisec, the method to run)
        self.timer = GObject.timeout_add(TIME_UPATE, self.update_ui, None)
        self.timer_enable = True

    def _set_basic_temperature(self, val):
        self.temperature_basic_label.set_markup("<span font_desc='LiberationSans 25'>%.02f °C</span>" % val)
    def _set_basic_humidity(self, val):
        self.humidity_basic_label.set_markup("<span font_desc='LiberationSans 25'>%.02f %c</span>" % (val, '%'))
    def _set_basic_pressure(self, val):
        self.pressure_basic_label.set_markup("<span font_desc='LiberationSans 25'>%.02f hP</span>" % val)
    def _set_basic_accelerometer(self, x, y, z):
        self.liststore[self.accel_store][1] = '%d' % x
        self.liststore[self.accel_store][2] = '%d' % y
        self.liststore[self.accel_store][3] = '%d' % z
    def _set_basic_gyroscope(self, x, y, z):
        self.liststore[self.gyro_store][1] = '%d' % x
        self.liststore[self.gyro_store][2] = '%d' % y
        self.liststore[self.gyro_store][3] = '%d' % z
    def _set_basic_magnetometer(self, x, y, z):
        self.liststore[self.magneto_store][1] = '%d' % x
        self.liststore[self.magneto_store][2] = '%d' % y
        self.liststore[self.magneto_store][3] = '%d' % z
    def _add_data_to_draw_for_sensor(self, index, val, valmin, valmax):
        values = self._axis_values.get(index, None)
        if values is None:
            values = deque(maxlen=NUM_GRAPH_SAMPLE)
            self._axis_values[index] = values
        if len(list(values)) > (NUM_GRAPH_SAMPLE - 1):
            values. popleft()
        values.append(val)
        self._axis_ranges[index] = (valmin, valmax)
    def _update_ip_address(self):
        ip = board_get_ip_address()
        self.label_ip_address.set_markup("<span font_desc='LiberationSans 25'>IP address: %s</span>" % ip)
    def _set_imu_roll(self, val):
        self.imu_liststore[self.roll_store][1] = '%d' % val
    def _set_imu_pitch(self, val):
        self.imu_liststore[self.pitch_store][1] = '%d' % val
    def _set_imu_yaw(self, val):
        self.imu_liststore[self.yaw_store][1] = '%d' % val
    def _update_orientation(self, val):
        self.orientation_label.set_text("Orientation: %s" % val)

    def create_notebook_page_basic_sensor(self):
        '''
        create notebook page for displaying basic current
        sensor information: temperature, humidity, pressure
        '''
        page_basic = Gtk.HBox(False, 0)
        page_basic.set_border_width(10)

        # temperature
        temp_box = Gtk.VBox(False, 0)
        temp_image = _load_image(self, "RS1069_climate_change_light_blue.png")
        self.temperature_basic_label = Gtk.Label('--.-- °C')
        temp_image.show()
        self.temperature_basic_label.show()
        temp_box.pack_start(temp_image, True, False, 3)
        temp_box.add(self.temperature_basic_label)
        # humidity
        humidity_box = Gtk.VBox(False, 0)
        humidity_image = _load_image(self, "RS1902_humidity_light_blue.png")
        self.humidity_basic_label = Gtk.Label('--.-- °C')
        humidity_image.show()
        self.humidity_basic_label.show()
        humidity_box.pack_start(humidity_image, True, False, 3)
        humidity_box.add(self.humidity_basic_label)
        # Pressure
        if WITH_PRESSURE:
            pressure_box = Gtk.VBox(False, 0)
            pressure_image = _load_image(self, "RS6355_FORCE_PRESSURE_light_blue.png")
            self.pressure_basic_label = Gtk.Label('--.-- °C')
            pressure_image.show()
            self.pressure_basic_label.show()
            pressure_box.pack_start(pressure_image, True, False, 3)
            pressure_box.add(self.pressure_basic_label)

        page_basic.add(temp_box)
        page_basic.add(humidity_box)
        if WITH_PRESSURE:
            page_basic.add(pressure_box)
        self.notebook.append_page(page_basic, Gtk.Label('Sensors'))

    def create_notebook_page_graph_sensor(self):
        '''
        create notebook page for displaying graphic of
        sensor information: temperature, humidity, pressure
        '''
        page_graph = Gtk.Box()
        page_graph.set_border_width(10)
        self.drawarea = Gtk.DrawingArea()

        self.drawing_area_width = self.screen_width
        self.drawing_area_height = int(self.screen_height * 1/2)
        self.drawarea.set_size_request(self.drawing_area_width, self.drawing_area_height)
        self.drawarea.connect("draw", self.drawarea_draw_event)
        self.drawarea_window = None
        page_graph.add(self.drawarea)
        self.notebook.append_page(page_graph, Gtk.Label('Graph'))

    def create_notebook_page_basic_accel(self):
        '''
        create notebook page for displaying movement
        sensor information: accellerometer, gyroscope, magentometer
        '''
        page_basic_movement = Gtk.Box()
        page_basic_movement.set_border_width(10)

        self.liststore = Gtk.ListStore(str, str, str, str)
        self.accel_store = self.liststore.append([ "Accelerometer", "0.0", "0.0", "0.0" ])
        if WITH_GYRO:
            self.gyro_store = self.liststore.append([ "Gyroscope", "0.0", "0.0", "0.0" ])
        #if WITH_MAGNETO:
        #    self.magneto_store = self.liststore.append([ "Magnetometer", "0.0", "0.0", "0.0" ])
        treeview = Gtk.TreeView(model=self.liststore)

        renderer_text = Gtk.CellRendererText()
        column_text = Gtk.TreeViewColumn("Sensor", renderer_text, text=0)
        treeview.append_column(column_text)

        renderer_x = Gtk.CellRendererText()
        column_x = Gtk.TreeViewColumn("X", renderer_x, text=1)
        treeview.append_column(column_x)

        renderer_y = Gtk.CellRendererText()
        column_y = Gtk.TreeViewColumn("Y", renderer_y, text=2)
        treeview.append_column(column_y)

        renderer_z = Gtk.CellRendererText()
        column_z = Gtk.TreeViewColumn("Z", renderer_z, text=3)
        treeview.append_column(column_z)

        page_basic_movement.add(treeview)
        self.notebook.append_page(page_basic_movement, Gtk.Label('Move'))


    def create_notebook_page_imu(self):
        '''
            display the IMU: Inertial Measurement Unit
            Roll(X-axis), Pitch(Y-axis) and Yaw(Z-axis).
        '''
        page_imu_all = Gtk.VBox(False, 0)
        imu_frame = Gtk.Frame(label="IMU")
        orientation_frame = Gtk.Frame(label="Orientation")

        page_imu = Gtk.VBox(False, 0)

        #page_imu_all.add(page_imu)
        page_imu_all.add(imu_frame)
        page_imu_all.add(orientation_frame)

        page_imu.set_border_width(10)
        # explanation
        imu_label = Gtk.Label('Inertial Measurement Unit')
        page_imu.add(imu_label)

        imu_h_box = Gtk.HBox(False, 0)
        # picture which describe IMU
        picture_filename = "%s/RS10670_Aereo-lpr.jpg" % ICON_PICTURES_PATH
        image_imu = _load_image(self, "RS10670_Aereo-lpr.jpg")

        imu_h_box.pack_start(image_imu, True, True, 10)
        # add tree view with roll, picth, yaw
        self.imu_liststore = Gtk.ListStore(str, str)
        self.roll_store  = self.imu_liststore.append([ "Roll  (X-axis)", "0.0"])
        self.pitch_store = self.imu_liststore.append([ "Pitch (Y-axis)", "0.0"])
        self.yaw_store   = self.imu_liststore.append([ "Yaw   (Z-axis)", "0.0"])
        imu_treeview = Gtk.TreeView(model=self.imu_liststore)
        imu_h_box.add(imu_treeview)
        page_imu.add(imu_h_box)
        imu_frame.add(page_imu)

        self.orientation_label = Gtk.Label("Orientation: --none--")
        orientation_frame.add(self.orientation_label)

        renderer_text = Gtk.CellRendererText()
        column_imu_text = Gtk.TreeViewColumn("Type", renderer_text, text=0)
        imu_treeview.append_column(column_imu_text)
        renderer_val = Gtk.CellRendererText()
        column_val = Gtk.TreeViewColumn("Value", renderer_val, text=1)
        imu_treeview.append_column(column_val)

        self.notebook.append_page(page_imu_all, Gtk.Label('IMU'))

    def _create_frame_with_image_and_label(self, title, img_file_name, label_text):
        frame = Gtk.Frame(label=title)
        box   = Gtk.VBox(False, 0)
        img   = _load_image(self, img_file_name)
        img   = _load_image_constrained(self, img_file_name, -1, 100)
        label = Gtk.Label(label_text)
        box.add(img)
        box.add(label)
        frame.add(box)
        return frame

    def create_notebook_page_sensor_information(self):
        ''' Display all the sensor used for this demo '''
        page_sensor_info = Gtk.Grid()

        frame_temp = self._create_frame_with_image_and_label(
            "%25s" % "Temperature",
            "RS1069_climate_change_light_blue.png",
            self.sensors.get_driver_name_temperature()
        )
        frame_humidity = self._create_frame_with_image_and_label(
            "%25s" % "Humidity",
            "RS1902_humidity_light_blue.png",
            self.sensors.get_driver_name_humidity()
        )
        if WITH_PRESSURE:
            frame_pressure = self._create_frame_with_image_and_label(
                "%25s" % "Pressure",
                "RS6355_FORCE_PRESSURE_light_blue.png",
                self.sensors.get_driver_name_pressure()
            )
        frame_accel = self._create_frame_with_image_and_label(
            "%25s" % "Accelerometer",
            "RS1761_MEMS_accelerometer_light_blue.png",
            self.sensors.get_driver_name_accelerometer()
        )
        if WITH_GYRO:
            frame_gyro = self._create_frame_with_image_and_label(
                "%25s" % "Gyroscope",
                 "RS1760_MEMS_gyroscope_light_blue.png",
                 self.sensors.get_driver_name_gyroscope()
            )
        #if WITH_MAGNETO:
        #    frame_magneto = self._create_frame_with_image_and_label(
        #        "%25s" % "Magnetometer",
        #        "RS1762_MEMS_compass_light_blue.png",
        #        self.sensors.get_driver_name_magnetometer()
        #    )

        page_sensor_info.set_column_spacing(2)
        page_sensor_info.set_row_spacing(2)

        page_sensor_info.attach(frame_temp, 1, 1, 3, 1)
        page_sensor_info.attach_next_to(frame_humidity, frame_temp,
                                        Gtk.PositionType.RIGHT, 1, 1)
        if WITH_PRESSURE:
            page_sensor_info.attach_next_to(frame_pressure, frame_humidity,
                                            Gtk.PositionType.RIGHT, 1, 1)
        page_sensor_info.attach_next_to(frame_accel, frame_temp,
                                        Gtk.PositionType.BOTTOM, 1, 1)
        if WITH_GYRO:
            page_sensor_info.attach_next_to(frame_gyro, frame_accel,
                                            Gtk.PositionType.RIGHT, 1, 1)
        #if WITH_MAGNETO:
        #    page_sensor_info.attach_next_to(frame_magneto, frame_gyro,
        #                                    Gtk.PositionType.RIGHT, 1, 1)

        #self.notebook.append_page(page_sensor_info,
        #                          Gtk.Image.new_from_icon_name(
        #        "dialog-information",
        #        Gtk.IconSize.MENU
        #        ))
        self.notebook.append_page(page_sensor_info, Gtk.Label('Infos'))

    def create_notebook_page_video(self):
        '''Display a videotestsrc '''
        page_video = Gtk.Grid()
        page_video.set_column_spacing(2)
        page_video.set_row_spacing(2)

        video_button = Gtk.Button()
        video_button.connect("clicked", self.video_videoplayback)
        video_image = _load_image_on_button(self, "%s/RS4898_action_pink.png" % ICON_PICTURES_PATH, "video", -1, 100)
        video_button.add(video_image)
        video_button.show()


        videotestsrc_button = Gtk.Button()
        videotestsrc_button.connect("clicked", self.mir_videoplayback)
        videotestsrc_image = _load_image_on_button(self, "%s/RS157_digital_TV_and_monitor_pink.png" % ICON_PICTURES_PATH, "mir", -1, 100)
        videotestsrc_button.add(videotestsrc_image)
        videotestsrc_button.show()


        page_video.attach(video_button, 1, 1, 3, 1)
        page_video.attach_next_to(videotestsrc_button, video_button,
                                        Gtk.PositionType.RIGHT, 1, 1)

        self.notebook.append_page(page_video, Gtk.Label('Misc'))

    def create_notebook_page_ip_address(self):
        ''' Display the ip address of board '''
        page_ip = Gtk.Box()
        page_ip.set_border_width(10)
        ip = board_get_ip_address()
        self.label_ip_address = Gtk.Label('IP address: %s' % ip)

        page_ip.add(self.label_ip_address)
        self.notebook.append_page(page_ip, Gtk.Label('IP'))

    def create_notebook_page_exit(self):
        ''' notebook page for quitting application '''
        page = Gtk.VBox(False, 0)
        page.set_border_width(10)
        ''' button '''
        lastbutton = Gtk.Button()
        lastbutton.connect("clicked", self.destroy)
        image_to_add = _load_image_on_button(self, "%s/RS70_ST_Logo_Qi.png" % ICON_PICTURES_PATH, "Quit", -1, 200)
        lastbutton.add(image_to_add)
        lastbutton.show()

        page.pack_start(lastbutton, True, True, 0)
        page.pack_start(Gtk.Label('To exit, click on logo.'), False, False, 0)
        #page.add(Gtk.Label('To exit, click on logo.'))

        #self.notebook.append_page(
        #    page,
        #    Gtk.Image.new_from_icon_name(
        #        "system-shutdown",
        #        Gtk.IconSize.MENU
        #        )
        #)
        self.notebook.append_page(page, Gtk.Label('Exit'))
    def destroy(self, widget, data=None):
        Gtk.main_quit()

    def rearm_timer(self):
        if self.timer_enable:
            self.timer = GObject.timeout_add(TIME_UPATE, self.update_ui, None)

    def video_videoplayback(self, button):
        ''' Launch a window with video playback '''
        video_window = GstVideoWindow(self, "playback")

        #put it at diaglog http://python-gtk-3-tutorial.readthedocs.io/en/latest/dialogs.html#example
        self.hide()
        self.timer_enable = False
        #video_window.show_all()
        response = video_window.run()
        self.timer_enable = True
        self.rearm_timer()
        self.show_all()
        video_window.destroy()

    def mir_videoplayback(self, button):
        ''' Launch a window with a mir '''
        video_window = GstVideoWindow(self, "videotestsrc")

        self.hide()
        self.timer_enable = False
        #video_window.show_all()
        response = video_window.run()
        self.timer_enable = True
        self.rearm_timer()
        self.show_all()
        video_window.destroy()

    def drawarea_draw_event(self, widget, user_data):
        ''' Draw graphic data '''
        try:
            #self.drawarea_window = self.get_window()
            self.drawarea_window = widget.get_window()
            ctx = self.drawarea_window.cairo_create()
            if self.get_property("visible"):
                self._draw_cb(self.drawarea_window, ctx)
        except Exception as exc:
            pass

    def _draw_cb(self, drawingarea, ctx):
        ''' '''
        if SIMULATE_SENSORS > 0:
            width = SIMULATE_SCREEN_SIZE_WIDTH
            height = SIMULATE_SCREEN_SIZE_HEIGHT
        else:
            width = self.drawing_area_width
            height = self.drawing_area_height
        graphheight = height / 3

        axis_ids = set(self._axis_ranges.keys())
        axis_ids.intersection_update(set(self._axis_values.keys()))
        offset = 10
        text_offset = 30

        #activate the following rectangle to see the full drawarea
        #ctx.set_source_rgb(1.0, 0.0, 0.)
        #ctx.rectangle(0, 0, width, height)
        #print("[DEBUG] draw rectangle: 0x0, %dx%d" % (width, height))
        #ctx.fill()

        for i in sorted(axis_ids):
            vmin, vmax = self._axis_ranges[i]
            if i == 1:
                ''' Temperature value '''
                #draw rectangle
                ctx.set_source_rgb(0.8, 0.8, 0.8)
                ctx.rectangle(0, 5, width, graphheight - offset)
                ctx.fill()
                #draw axes
                ctx.set_source_rgb(0, 1, 1)
                ctx.move_to(30 + 0.5, graphheight/2)
                ctx.line_to(width - 0.5, graphheight/2)
                ctx.stroke()
                # value (text)
                ctx.select_font_face("sans-serif")
                ctx.set_font_size(20.0)
                ctx.set_source_rgb(0, 0, 0)
                ctx.move_to(0, 20)
                ctx.show_text("T (°C)")
                ctx.set_font_size(10.0)
                ctx.move_to(0, graphheight/2 + 4)
                ctx.show_text("%d °C" % int((vmax-vmin)/2 + vmin) )
                # temperature between vmin and vmax (0, 60)
                values = self._axis_values[i]
                ctx.set_source_rgb(0, 0, 0)
                for x, v in enumerate(values):
                    val = text_offset + x*4 # GRAPH_H_PADDING = 4
                    #print("[DEBUG]: temperature %d %d" % (int(v),val))
                    ctx.move_to(val, graphheight - offset/2)
                    ctx.line_to(val, graphheight - (((v - vmin) * (graphheight - offset)) / (vmax -vmin)) )
                    ctx.stroke()

            elif i == 2:
                ''' Humidity '''
                #draw rectangle
                ctx.set_source_rgb(0.8, 0.8, 0.8)
                ctx.rectangle(0, graphheight + offset/2, width, graphheight - offset)
                ctx.fill()
                #draw axes
                ctx.set_source_rgb(0, 1, 1)
                ctx.move_to(25 + 0.5, graphheight + graphheight/2)
                ctx.line_to(width - 0.5, graphheight + graphheight/2)
                ctx.stroke()
                # value (text)
                ctx.select_font_face("sans-serif")
                ctx.set_font_size(20.0)
                ctx.set_source_rgb(0, 0, 0)
                ctx.move_to(0, graphheight + offset + 20)
                ctx.show_text("H (%)")
                ctx.set_font_size(10.0)
                ctx.set_source_rgb(0, 0, 0)
                ctx.move_to(0, graphheight + graphheight/2 + 4)
                ctx.show_text("%d %c" % (int((vmax-vmin)/2 + vmin), '%'))
                # humidity between vmin and vmax (0, 100)
                values = self._axis_values[i]
                ctx.set_source_rgb(0, 0, 0)
                for x, v in enumerate(values):
                    val = text_offset + x*4 # GRAPH_H_PADDING = 4
                    ctx.move_to(val, 2 * graphheight - offset/2)
                    ctx.line_to(val, 2 * graphheight - offset - (((v - vmin) * (graphheight - offset)) / (vmax -vmin)) )
                    #print("[DEBUG]: humidity %d %d" % (int(v),  ((v - vmin) * (graphheight - offset)) / (vmax -vmin)  ))
                    ctx.stroke()

            elif i == 3:
                ''' Pressure '''
                #draw rectangle
                ctx.set_source_rgb(0.8, 0.8, 0.8)
                ctx.rectangle(0, 2 * graphheight + offset/2, width, graphheight - offset)
                ctx.fill()
                #draw axes
                ctx.set_source_rgb(0, 1, 1)
                ctx.move_to(25 + 0.5, 2 * graphheight + graphheight/2)
                ctx.line_to(width - 0.5, 2 * graphheight + graphheight/2)
                ctx.stroke()
                # value (text)
                ctx.select_font_face("sans-serif")
                ctx.set_font_size(20.0)
                ctx.set_source_rgb(0, 0, 0)
                ctx.move_to(0, 2*graphheight + offset + 20)
                ctx.show_text("Pr (hPa)")
                ctx.set_font_size(10.0)
                ctx.set_source_rgb(0, 0, 0)
                ctx.move_to(0, 2*graphheight + graphheight/2 + 4)
                ctx.show_text("%d" % int((vmax-vmin)/2 + vmin))
                # pressure between vmin and vmax (900, 1100)
                values = self._axis_values[i]
                ctx.set_source_rgb(0, 0, 0)
                for x, v in enumerate(values):
                    val = text_offset + x*4 # GRAPH_H_PADDING = 4
                    ctx.move_to(val, 3 * graphheight - offset/2)
                    ctx.line_to(val, 3 * graphheight - offset/2 - (((v - vmin) * (graphheight - offset)) / (vmax -vmin)) )
                    ctx.stroke()

        return False

    def update_ui(self, user_data):
        if False == self.timer_enable:
            return False;

        # temperature
        temp = self.sensors.read_temperature()
        self._set_basic_temperature(temp)
        self._add_data_to_draw_for_sensor(1, temp, 0.0, 60.0)
        # humidity
        hum = self.sensors.read_humidity()
        self._set_basic_humidity(hum)
        self._add_data_to_draw_for_sensor(2, hum, 0.0, 100.0)
        # pressure
        if WITH_PRESSURE:
            press = self.sensors.read_pressure()
            self._set_basic_pressure(press)
            self._add_data_to_draw_for_sensor(3, press, 900.0, 1100.0)

        # accel
        accel = self.sensors.read_accelerometer()
        self._set_basic_accelerometer(accel[0], accel[1], accel[2])
        # gyro
        if WITH_GYRO:
            gyro = self.sensors.read_gyroscope()
            self._set_basic_gyroscope(gyro[0], gyro[1], gyro[2])
        # magneto
        #if WITH_MAGNETO:
        #    magneto = self.sensors.read_magnetometer()
        #    self._set_basic_magnetometer(magneto[0], magneto[1], magneto[2])

        # imu
        pitch, roll, yaw = self.sensors.calculate_imu(accel)
        self._set_imu_pitch(pitch)
        self._set_imu_roll(roll)
        self._set_imu_yaw(yaw)
        self._update_orientation(self.sensors.get_imu_orientation(pitch, roll))
        # ip address
        self._update_ip_address()

        #print("[DEBUG] visibility: ", self.get_property("visible"))

        try:
            if not self.drawarea_window.cairo is None:
                ctx = self.drawarea_window.cairo_create()
                if self.get_property("visible"):
                    self._draw_cb(self.drawarea_window, ctx)
        except Exception as exc:
            pass

        # As this is a timeout function, return True so that it
        # continues to get called
        return True

# -------------------------------------------------------------------
# -------------------------------------------------------------------
# Main
if __name__ == "__main__":
    # add signal to catch CRTL+C
    import signal
    signal.signal(signal.SIGINT, signal.SIG_DFL)
    try:
        splScr = SplashScreen("%s/RS70_ST_Logo_Qi.png" % ICON_PICTURES_PATH, 5)
        Gtk.main()

        win = MainUIWindow()
        win.connect("delete-event", Gtk.main_quit)
        win.show_all()
    except Exception as exc:
        print("Main Exception: ", exc )

    Gtk.main()
