#! /bin/sh
### BEGIN INIT INFO
# remove package which are present on the database but not present on userfs
### END INIT INFO

DESC="cleanup apt database"

case $1 in
	start)
		echo "Starting $DESC"
		if [ ! -e /dev/disk/by-partlabel/userfs ]
		then
			# userfs partition are not present
			# we need to cleanup apt database
			grep -l "^/usr/local/" /var/lib/dpkg/info/* | sed -e "s|/var/lib/dpkg/info/\(.*\).list|\1|" | xargs apt-get purge -y
		fi
	;;
  *)
		echo "Usage: @sysconfdir@/init.d/userfs-cleanup-package.sh {start}" >&2
		exit 1
	;;
esac

exit 0

# vim:noet
