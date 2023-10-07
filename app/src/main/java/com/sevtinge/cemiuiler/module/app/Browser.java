package com.sevtinge.cemiuiler.module.app;

import com.sevtinge.cemiuiler.module.base.BaseModule;
import com.sevtinge.cemiuiler.module.base.CloseHostDir;
import com.sevtinge.cemiuiler.module.base.LoadHostDir;
import com.sevtinge.cemiuiler.module.hook.browser.DebugMode;
import com.sevtinge.cemiuiler.module.hook.various.UnlockSuperClipboard;

public class Browser extends BaseModule {

    @Override
    public void handleLoadPackage() {
        initHook(LoadHostDir.INSTANCE);
        // initHook(new BrowserDexKit());
        initHook(DebugMode.INSTANCE, mPrefsMap.getBoolean("browser_debug_mode"));
        initHook(UnlockSuperClipboard.INSTANCE, mPrefsMap.getBoolean("various_super_clipboard_enable"));
        initHook(CloseHostDir.INSTANCE);
    }
}
