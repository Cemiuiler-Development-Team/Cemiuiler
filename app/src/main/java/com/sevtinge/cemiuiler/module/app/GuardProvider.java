package com.sevtinge.cemiuiler.module.app;

import com.sevtinge.cemiuiler.module.base.BaseModule;
import com.sevtinge.cemiuiler.module.base.CloseHostDir;
import com.sevtinge.cemiuiler.module.base.LoadHostDir;
import com.sevtinge.cemiuiler.module.hook.guardprovider.DisableUploadAppListNew;
import com.sevtinge.cemiuiler.module.hook.guardprovider.GuardProviderDexKit;

public class GuardProvider extends BaseModule {
    @Override
    public void handleLoadPackage() {
        initHook(LoadHostDir.INSTANCE);
        // initHook(new GuardProviderDexKit());
        // initHook(new DisableUploadAppList(), mPrefsMap.getBoolean("disable_upload_applist"));
        initHook(new DisableUploadAppListNew(), mPrefsMap.getBoolean("disable_upload_applist"));
        initHook(CloseHostDir.INSTANCE);
    }
}
