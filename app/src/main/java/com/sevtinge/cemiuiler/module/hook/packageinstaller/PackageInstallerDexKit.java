package com.sevtinge.cemiuiler.module.hook.packageinstaller;

import com.sevtinge.cemiuiler.module.base.BaseHook;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class PackageInstallerDexKit extends BaseHook {

  /*  public static Map<String, List<DexMethodDescriptor>> mPackageInstallerResultMethodsMap;*/

    @Override
    public void init() {
       /* System.loadLibrary("dexkit");
        String apkPath = lpparam.appInfo.sourceDir;
        DexKitBridge bridge = DexKitBridge.create(apkPath);
        try {
            if (bridge == null) {
                return;
            }
            mPackageInstallerResultMethodsMap =
                bridge.batchFindMethodsUsingStrings(
                    BatchFindArgs.builder()
                        .addQuery("SecureVerifyEnable", Set.of("secure_verify_enable"))
                        .addQuery("DisableSecurityModeFlag", Set.of("user_close_security_mode_flag"))
                        .addQuery("InstallerOpenSafetyModel", Set.of("installerOpenSafetyModel"))
                        .addQuery("AppStoreRecommend", Set.of("app_store_recommend"))
                        .addQuery("EnableAds", Set.of("ads_enable"))
                        .addQuery("isInstallRiskEnabled", Set.of("virus_scan_install"))
                        .addQuery("DisableSafeModelTip", Set.of("android.provider.MiuiSettings$Ad"))
                        .matchType(MatchType.CONTAINS)
                        .build()
                );
        } catch (Throwable e) {
            e.printStackTrace();
        }
        bridge.close();*/
    }
}
