package com.sevtinge.cemiuiler.module.hook.screenrecorder;

import com.sevtinge.cemiuiler.module.base.BaseHook;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ScreenRecorderDexKit extends BaseHook {

    // public static Map<String, List<DexMethodDescriptor>> mScreenRecorderResultMethodsMap;

    @Override
    public void init() {
    /*    System.loadLibrary("dexkit");
        String apkPath = lpparam.appInfo.sourceDir;
        DexKitBridge bridge = DexKitBridge.create(apkPath);
        try {
            if (bridge == null) {
                return;
            }
            mScreenRecorderResultMethodsMap =
                bridge.batchFindMethodsUsingStrings(
                    BatchFindArgs.builder()
                        .addQuery("ScreenRecorderConfigA", Set.of("Error when set frame value, maxValue = "))
                        .addQuery("ScreenRecorderConfigB", Set.of("defaultBitRate = "))
                        .matchType(MatchType.CONTAINS)
                        .build()
                );
        } catch (Throwable e) {
            e.printStackTrace();
        }
        bridge.close();*/
    }
}
