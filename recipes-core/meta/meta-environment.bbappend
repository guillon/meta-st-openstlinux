# Add HOSTCFLAGS to environment setup for SDK
# This allow for example kernel tools to be built using include from SDK host
# sysroots (e.g. need bio.h from openssl)

toolchain_shared_env_script_prepend() {
    echo 'export HOSTCFLAGS=$OECORE_NATIVE_SYSROOT' >> $script
}
