Compilation of Optee-os (Trusted Execution Environment):
1. Pre-requisite
2. Initialise cross-compilation via SDK
3. Prepare optee-os source code
4. Management of optee-os source code
5. Compile optee-os source code
6. Update software on board

1. Pre-requisite:
-----------------
OpenSTLinux SDK must be installed.

For optee-os build you need to install:
- Wand python and/or python crypto package
    Ubuntu: sudo apt-get install python-wand python-crypto python-pycryptopp
    Fedora: sudo yum install python-wand python-crypto
- git:
    Ubuntu: sudo apt-get install git-core gitk
    Fedora: sudo yum install git

If you have never configured you git configuration:
    $> git config --global user.name "your_name"
    $> git config --global user.email "your_email@example.com"

2. Initialise cross-compilation via SDK:
---------------------------------------
 Source SDK environment:
    $> source <path to SDK>/environment-setup-cortexa9hf-neon-openstlinux_weston-linux-gnueabi

 To verify if your cross-compilation environment have put in place:
    $> set | grep CROSS
    CROSS_COMPILE=arm-openstlinux_weston-linux-gnueabi-

Warning: the environment are valid only on the shell session where you have
 sourced the sdk environment.

3. Prepare optee-os source:
------------------------
If you have the tarball and the list of patch then you must extract the
tarball and apply the patch.
    $> tar xfz <optee-os source>.tar.gz
    or
    $> tar xfj <optee-os source>.tar.bz2
    or
    $> tar xfJ <optee-os source>.tar.xz
    $> cd <directory to optee-os source code>
NB: if there is no git management on source code and you would like to have a git management
on the code see section 4 [Management of optee-os source code]
    if there is some patch, please apply it on source code
    $> for p in `ls -1 <path to patch>/*.patch`; do patch -p1 < $p; done

4. Management of optee-os source code:
-----------------------------------
If you like to have a better management of change made on optee-os source, you
can use git:
    $> cd <optee-os source>
    $> test -d .git || git init . && git add . && git commit -m "optee-ossource code" && git gc
    $> git checkout -b WORKING
    $> for p in `ls -1 <path to patch>/*.patch`; do git am $p; done

NB: you can use directly the source from the community:
    URL: ssh://$USER@gerrit.st.com:29418/mpu/oe/optee/optee_os
    Branch: ##GIT_BRANCH##
    Revision: ##GIT_SRCREV##

    $> git clone ssh://$USER@gerrit.st.com:29418/mpu/oe/optee/optee_os
    $> cd optee-os
    $> cp pre-push .git/hooks/
    $> scp -p -P 29418 $USER@gerrit.st.com:hooks/commit-msg .git/hooks/
    $> git checkout -b WORKING ##GIT_SRCREV##

5. Build optee-os source code:
--------------------------------
To compile optee-os source code
    $> cd <directory to optee-os source code>
    $> make -f $PWD/../Makefile.sdk

6. Update software on board:
----------------------------
6.1. partitioning of binaries:
-----------------------------
* Bootfs:
  Bootfs contains the optee-os binary

6.2. update via network:
------------------------
    optee-os.bin
    $> cd <directory to optee-os source code>
    $> ssh root@<ip of board> mount <device corresponding to bootfs> /boot
    $> scp -r out/optee.bin root@<ip of board>:/root/b2260/
    $> ssh root@<ip of board> umount /boot

6.3. update via SDCARD on your Linux PC:
----------------------------------------
    optee-os.bin
    $> cd <path to build directory>
    Verify sdcard are mounted on your Linux PC: /media/$USER/bootfs
    $> cp out/optee.bin /media/$USER/bootfs/b2260/
    Depending of your Linux configuration, you may call the command under sudo
        $> sudo cp out/optee.bin /media/$USER/bootfs/b2260/
    Don't forget to unmount properly sdcard


