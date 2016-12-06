# Specific ST class to automatically add Gerrit commit-msg hook for git
# that use externalscr class to ease push with ChangeId for review.

def _run(cmd, cwd=''):
    bb.debug(1, "Running command %s> %s" % (cwd,cmd))
    return bb.process.run('%s' % cmd, cwd=cwd)

python __anonymous () {
    if bb.data.inherits_class('externalsrc', d):
        srcpath = d.getVar('EXTERNALSRC', True)
        # When dealing with nativesdk package, EXTERNALSRC is reset to 'None' even if package is using externalsrc class
        # Add a specific test to handle this use case
        if srcpath != None:
            # Update scrpath to .git root folder
            while not os.path.exists(os.path.join(srcpath, '.git')):
                srcpath = os.path.realpath(os.path.join(srcpath, os.pardir))
                if srcpath == "/":
                    bb.fatal("Cannot find .git folder for in %s" % d.getVar('EXTERNALSRC', True))
            if not os.path.isfile(os.path.join(srcpath, '.git/hooks/commit-msg')):
                # Check if module relies on ST gerrit server to host source
                cmd = d.expand('echo ${SRC_URI} | grep "git://gerrit.st.com"')
                (status, result) = oe.utils.getstatusoutput(cmd)
                if status !=0:
                    # Command succeeded
                    if result != "":
                        # Git is hosted by ST Gerrit
                        bb.note(">>> Add Gerrit commit-msg hook to " + srcpath)
                        _run('cd $srcpath', srcpath)
                        _run('scp -p -P 29418 $USER@gerrit.st.com:hooks/commit-msg .git/hooks/', srcpath)
                else:
                    bb.fatal("Cmd grep failed with exit code %s (cmd was %s)%s" % (status, cmd, ":\n%s" % result if result else ""))
}
