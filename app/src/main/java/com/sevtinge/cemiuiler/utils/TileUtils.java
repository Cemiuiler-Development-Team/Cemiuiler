package com.sevtinge.cemiuiler.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;

import com.sevtinge.cemiuiler.module.base.BaseHook;

import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import de.robv.android.xposed.XposedHelpers;

public abstract class TileUtils extends BaseHook {
    Class<?> mResourceIcon;
    Class<?> mQSFactory;
    final boolean[] isListened = {false};

    /*固定语法，必须调用。
     * 调用方法：
     * @Override
     * public void init() {
     *     super.init();
     * }
     * */
    @Override
    public void init() {
        mQSFactory = customQSFactory();
        Class<?> myTile = customClass();
        mResourceIcon = findClass("com.android.systemui.qs.tileimpl.QSTileImpl$ResourceIcon");
        SystemUiHook(); // 不需要覆写
        tileAllName(mQSFactory); // 不需要覆写
        findAndHookMethod(myTile, "isAvailable", new MethodHook() {
            @Override
            protected void before(MethodHookParam param) {
                String tileName = (String) XposedHelpers.getAdditionalInstanceField(param.thisObject, "customName");
                if (tileName != null) {
                    tileCheck(param, tileName);
                }
            }
        });
        tileName(myTile); // 不需要覆写
        findAndHookMethod(myTile, "handleSetListening", boolean.class, new MethodHook() {
            @Override
            protected void before(MethodHookParam param) {
                String tileName = (String) XposedHelpers.getAdditionalInstanceField(param.thisObject, "customName");
                if (tileName != null) {
                    tileListening(param, tileName);
                }
            }
        });
        findAndHookMethod(myTile, "getLongClickIntent", new MethodHook() {
            @Override
            protected void before(MethodHookParam param) {
                String tileName = (String) XposedHelpers.getAdditionalInstanceField(param.thisObject, "customName");
                if (tileName != null) {
                    tileLongClickIntent(param, tileName);
                }
            }
        });
        findAndHookMethod(myTile, "handleClick", View.class, new MethodHook() {
            @Override
            protected void before(MethodHookParam param) {
                String tileName = (String) XposedHelpers.getAdditionalInstanceField(param.thisObject, "customName");
                if (tileName != null) {
                    tileClick(param, tileName);
                }
            }
        });
        hookAllMethods(myTile, "handleUpdateState", new MethodHook() {
            @Override
            protected void before(MethodHookParam param) {
                String tileName = (String) XposedHelpers.getAdditionalInstanceField(param.thisObject, "customName");
                if (tileName != null) {
                    tileUpdateState(param, mResourceIcon, tileName);
                }
            }
        });
    }

    public Class<?> customQSFactory() {
        return null;
    }

    /*需要Hook的磁贴Class*/
    public Class<?> customClass() {
        return null;
    }

    /*需要Hook执行的Class方法*/
    public String[] customTileProvider() {
        return null;
    }

    /*请在这里输入你需要的自定义快捷方式名称。*/
    public String customName() {
        return "";
    }

    /*在这里为你的自定义磁贴打上标题
   需要传入资源Id*/
    public int customValue() {
        return -1;
    }

    /*
     * 在第一次Hook时把新的快捷方式加载进快捷方式列表中。
     * */
    public void SystemUiHook() {
        String custom = customName();
        if (custom.equals("")) {
            logI("Error custom:" + custom);
            return;
        }
        findAndHookMethod("com.android.systemui.SystemUIApplication", "onCreate", new MethodHook() {
            @Override
            protected void after(MethodHookParam param) {
                if (!isListened[0]) {
                    isListened[0] = true;
                    // 获取Context
                    Context mContext = (Context) XposedHelpers.callMethod(param.thisObject, "getApplicationContext");
                    // 获取miui_quick_settings_tiles_stock字符串的值
                    @SuppressLint("DiscouragedApi") int stockTilesResId = mContext.getResources().getIdentifier("miui_quick_settings_tiles_stock", "string", lpparam.packageName);
                    String stockTiles = mContext.getString(stockTilesResId) + "," + custom; // 追加自定义的磁贴
                    // 将拼接后的字符串分别替换下面原有的字符串。
                    mResHook.setObjectReplacement(lpparam.packageName, "string", "miui_quick_settings_tiles_stock", stockTiles);
                    mResHook.setObjectReplacement("miui.systemui.plugin", "string", "miui_quick_settings_tiles_stock", stockTiles);
                    mResHook.setObjectReplacement("miui.systemui.plugin", "string", "quick_settings_tiles_stock", stockTiles);
                }
            }
        });
    }

    /*
     * 判断是否是自定义磁贴，如果是则在自定义磁贴前加上Key，用于定位磁贴。
     */
    public void tileAllName(Class<?> QSFactory) {
        findAndHookMethod(QSFactory, customTileProvider()[1], String.class, new MethodHook() {
            @Override
            protected void before(MethodHookParam param) {
                String tileName = (String) param.args[0];
                if (tileName.equals(customName())) {
                    String myTileProvider = customTileProvider()[0];
                    Object provider = XposedHelpers.getObjectField(param.thisObject, myTileProvider);
                    Object tile = XposedHelpers.callMethod(provider, "get");
                    XposedHelpers.setAdditionalInstanceField(tile, "customName", tileName);
                    param.setResult(tile);
                }
            }
        });
    }

    /*在这里可以为你的自定义磁贴判断系统是否支持
     此方法需要覆写*/
    public void tileCheck(MethodHookParam param, String tileName) {
    }


    /*为按键打上自定义名称*/
    public void tileName(Class<?> myTile) {
        int customValue = customValue();
        String custom = customName();
        if (customValue == -1 || custom.equals("")) {
            logI("Error customValue:" + customValue);
            return;
        }
        findAndHookMethod(myTile, "getTileLabel", new MethodHook() {
            @Override
            protected void before(MethodHookParam param) throws Throwable {
                String tileName = (String) XposedHelpers.getAdditionalInstanceField(param.thisObject, "customName");
                if (tileName != null) {
                    if (tileName.equals(custom)) {
                        Context mContext = (Context) XposedHelpers.getObjectField(param.thisObject, "mContext");
                        Resources modRes = Helpers.getModuleRes(mContext);
                        param.setResult(modRes.getString(customValue));
                    }
                }
            }
        });
    }

    /*这个方法用于监听系统设置变化
    用于实时刷新开关状态
    此方法需要自行覆写*/
    public void tileListening(MethodHookParam param, String tileName) {
    }

    /*这个方法用于设置长按磁贴的动作
     此方法需要自行覆写*/
    public void tileLongClickIntent(MethodHookParam param, String tileName) {
    }

    /*这个方法用于设置单击磁贴的动作
    此方法需要自行覆写*/
    public void tileClick(MethodHookParam param, String tileName) {
    }

    /*这个方法用于设置更新磁贴状态
     此方法需要自行覆写*/
    public void tileUpdateState(MethodHookParam param, Class<?> mResourceIcon, String tileName) {
    }
}
