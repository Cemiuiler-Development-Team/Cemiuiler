package com.sevtinge.cemiuiler.module.app;

import com.sevtinge.cemiuiler.module.base.BaseModule;
import com.sevtinge.cemiuiler.module.hook.mms.DisableAd;

public class Mms extends BaseModule {
    @Override
    public void handleLoadPackage() {
        initHook(DisableAd.INSTANCE, mPrefsMap.getBoolean("mms_disable_ad"));
    }
}
