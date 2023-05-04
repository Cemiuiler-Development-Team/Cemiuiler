package com.sevtinge.cemiuiler.module.systemsettings

import android.app.Activity
import android.os.Bundle
import com.sevtinge.cemiuiler.module.base.BaseHook
import com.sevtinge.cemiuiler.utils.setObjectField

class QuickInstallPermission : BaseHook() {
    override fun init() {
        findAndHookMethod(
            "com.android.settings.SettingsActivity",
            "redirectTabletActivity",
            Bundle::class.java,
            object : MethodHook() {
                override fun before(param: MethodHookParam) {
                    val intent = (param.thisObject as Activity).intent
                    if ("android.settings.MANAGE_UNKNOWN_APP_SOURCES" == intent.action && intent.data != null && "package" == intent.data!!.scheme) {
                        param.thisObject.setObjectField(
                            "initialFragmentName",
                            "com.android.settings.applications.appinfo.ExternalSourcesDetails"
                        )
                    }
                }
            })
    }
}