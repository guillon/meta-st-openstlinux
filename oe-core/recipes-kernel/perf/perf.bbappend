# When virtual/kernel inherit externalscr class (through devtool), the 'do_patch'
# task is deleted, so any other task that depends on it raises a bitbake error
# such as "task depends upon non-existent task do_patch"
# Proposal here is to update depends from 'do_patch' to 'do_configure' for impacted
# task

# Set list of tasks that need 'depends' flag update
TASKS_TO_CLEAN  = "do_populate_lic"
TASKS_TO_CLEAN += "do_patch"

python () {
    for do_xxx in d.getVar('TASKS_TO_CLEAN', True).split():
        # Get depends for do_xxx task
        taskdepends = (d.getVarFlag(do_xxx, 'depends', False) or '').split()
        # Reset depends for do_xxx task
        d.setVarFlag(do_xxx, 'depends', '')
        for task in taskdepends:
            if task == 'virtual/kernel:do_patch':
                # Update 'virtual/kernel:do_patch' depends to 'virtual/kernel:do_configure'
                d.appendVarFlag(do_xxx, 'depends', ' ' + 'virtual/kernel:do_configure')
            else:
                # Keep other depends
                d.appendVarFlag(do_xxx, 'depends', ' ' + task)
}
