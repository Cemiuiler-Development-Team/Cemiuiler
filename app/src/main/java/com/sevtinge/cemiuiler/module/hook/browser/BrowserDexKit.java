package com.sevtinge.cemiuiler.module.hook.browser;

import static com.sevtinge.cemiuiler.utils.Helpers.getPackageVersionCode;

import com.sevtinge.cemiuiler.module.base.BaseHook;

import java.util.Set;


/*public class BrowserDexKit extends BaseHook {

    public static Map<String, List<DexMethodDescriptor>> mBrowserResultMethodsMap;

    @Override
    public void init() {
        System.loadLibrary("dexkit");
        String apkPath = lpparam.appInfo.sourceDir;
        DexKitBridge bridge = DexKitBridge.create(apkPath);
        try {
            if (bridge == null) {
                return;
            }
            mBrowserResultMethodsMap =
                bridge.batchFindMethodsUsingStrings(
                    BatchFindArgs.builder()
                        .addQuery("DebugMode", Set.of("pref_key_debug_mode_new"))
                        .addQuery("DebugMode1", Set.of("pref_key_debug_mode"))
                        .addQuery("DebugMode2", Set.of("pref_key_debug_mode_" + getPackageVersionCode(lpparam)))
                        .matchType(MatchType.CONTAINS)
                        .build()
                );
        } catch (Throwable e) {
            e.printStackTrace();
        }
        bridge.close();
    }
}*/
