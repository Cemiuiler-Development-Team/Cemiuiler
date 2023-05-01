package com.sevtinge.cemiuiler.module.fileexplorer

import android.widget.TextView
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.getObject
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.sevtinge.cemiuiler.module.base.BaseHook
import com.sevtinge.cemiuiler.utils.XSPUtils.getBoolean

object SelectName : BaseHook() {
    override fun init() {
        findMethod("com.android.fileexplorer.view.FileListItem") { name == "onFinishInflate" }.hookAfter {
            (it.thisObject.getObject("mFileNameTextView") as TextView).apply {
                setTextIsSelectable(getBoolean("file_explorer_can_selectable", false))
                isSingleLine = getBoolean("file_explorer_is_single_line", false)
            }
        }
    }
}