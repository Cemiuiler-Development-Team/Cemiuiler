package com.sevtinge.cemiuiler.module.systemframework

import com.sevtinge.cemiuiler.module.base.BaseHook

object DisableCleaner : BaseHook() {
    override fun init() {
        hookAllMethod("com.android.server.am.ActivityManagerService", "checkExcessivePowerUsage",
            object : MethodHook() {
                override fun before(param: MethodHookParam?) {
                    param?.result = null
                }
            }
        )
        hookAllMethod("com.android.server.am.OomAdjuster", "shouldKillExcessiveProcesses",
            object : MethodHook() {
                override fun before(param: MethodHookParam?) {
                    param?.result = false
                }
            }
        )
        hookAllMethod("com.android.server.am.OomAdjuster", "updateAndTrimProcessLSP",
            object : MethodHook() {
                override fun before(param: MethodHookParam?) {
                    param?.args?.set(2, 0)
                }
            }
        )
        hookAllMethod("com.android.server.am.PhantomProcessList", "trimPhantomProcessesIfNecessary",
            object : MethodHook() {
                override fun before(param: MethodHookParam?) {
                    param?.result = null
                }
            }
        )
        hookAllMethod("com.android.server.am.ProcessMemoryCleaner", "checkBackgroundProcCompact",
            object : MethodHook() {
                override fun before(param: MethodHookParam?) {
                    param?.result = null
                }
            }
        )
        hookAllMethod("com.android.server.am.SystemPressureController", "nStartPressureMonitor",
            object : MethodHook() {
                override fun before(param: MethodHookParam?) {
                    param?.result = null
                }
            }
        )
        hookAllMethod("com.android.server.wm.RecentTasks", "trimInactiveRecentTasks",
            object : MethodHook() {
                override fun before(param: MethodHookParam?) {
                    param?.result = null
                }
            }
        )
    }
}
