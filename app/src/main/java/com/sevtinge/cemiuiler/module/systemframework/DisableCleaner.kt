package com.sevtinge.cemiuiler.module.systemframework

import com.sevtinge.cemiuiler.module.base.BaseHook

object DisableCleaner : BaseHook() {
    override fun init() {
        hookAllMethods("com.android.server.am.ActivityManagerService", "checkExcessivePowerUsage",
            object : MethodHook() {
                override fun before(param: MethodHookParam?) {
                    param?.result = null
                }
            }
        )
        hookAllMethods("com.android.server.am.OomAdjuster", "shouldKillExcessiveProcesses",
            object : MethodHook() {
                override fun before(param: MethodHookParam?) {
                    param?.result = false
                }
            }
        )
        hookAllMethods("com.android.server.am.OomAdjuster", "updateAndTrimProcessLSP",
            object : MethodHook() {
                override fun before(param: MethodHookParam?) {
                    param?.args?.set(2, 0)
                }
            }
        )
        hookAllMethods("com.android.server.am.PhantomProcessList", "trimPhantomProcessesIfNecessary",
            object : MethodHook() {
                override fun before(param: MethodHookParam?) {
                    param?.result = null
                }
            }
        )
        hookAllMethods("com.android.server.am.ProcessMemoryCleaner", "checkBackgroundProcCompact",
            object : MethodHook() {
                override fun before(param: MethodHookParam?) {
                    param?.result = null
                }
            }
        )
        hookAllMethods("com.android.server.am.SystemPressureController", "nStartPressureMonitor",
            object : MethodHook() {
                override fun before(param: MethodHookParam?) {
                    param?.result = null
                }
            }
        )
        hookAllMethods("com.android.server.wm.RecentTasks", "trimInactiveRecentTasks",
            object : MethodHook() {
                override fun before(param: MethodHookParam?) {
                    param?.result = null
                }
            }
        )
    }
}
