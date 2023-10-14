package com.sevtinge.cemiuiler.module.hook.systemui.controlcenter;

import static com.sevtinge.cemiuiler.utils.devicesdk.SystemSDKKt.isMoreAndroidVersion;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.util.ArrayMap;

import androidx.annotation.Nullable;

import com.sevtinge.cemiuiler.utils.ShellUtils;
import com.sevtinge.cemiuiler.utils.TileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class FlashLight extends TileUtils {
    String mQSFactoryClsName = isMoreAndroidVersion(Build.VERSION_CODES.TIRAMISU) ? "com.android.systemui.qs.tileimpl.MiuiQSFactory" :
        "com.android.systemui.qs.tileimpl.QSFactoryImpl";

    public final String flashFileMtk = "/sys/class/flashlight_core/flashlight/torchbrightness";
    public final String flashFileTorch = "/sys/class/leds/led:torch_0/brightness";
    public final String flashFileOther = "/sys/class/leds/flashlight/brightness";
    public final String flashFileSwitch = "/sys/class/leds/led:switch_0/brightness";
    public final String maxFile = "/sys/class/leds/led:torch_0/max_brightness";
    public boolean isListening = false;

    public boolean suGet;

    @Override
    public void init() {
        super.init();
    }

    @Override
    public Class<?> customQSFactory() {
        return findClassIfExists(mQSFactoryClsName);
    }

    @Override
    public Class<?> customClass() {
        return findClassIfExists("com.android.systemui.qs.tiles.MiuiFlashlightTile");
    }

    @Override
    public String[] customTileProvider() {
        String[] TileProvider = new String[3];
        TileProvider[0] = isMoreAndroidVersion(Build.VERSION_CODES.TIRAMISU) ? "flashlightTileProvider" : "mFlashlightTileProvider";
        TileProvider[1] = isMoreAndroidVersion(Build.VERSION_CODES.TIRAMISU) ? "createTileInternal" : "interceptCreateTile";
        TileProvider[2] = "createTile";
        return TileProvider;
    }

    @Override
    public boolean needCustom() {
        return false;
    }

    @Override
    public boolean needAfter() {
        return true;
    }

    @Override
    public void tileClickAfter(XC_MethodHook.MethodHookParam param, String tileName) {
    }


    @Override
    public ArrayMap<String, Integer> tileUpdateState(XC_MethodHook.MethodHookParam param, Class<?> mResourceIcon, String tileName) {
        Context mContext = (Context) XposedHelpers.getObjectField(param.thisObject, "mContext");
        ContentObserver contentObserver;
        // logE("tileUpdateState: args: " + param.args[1]);
        if (param.args[1] != null) {
            boolean enabled = (boolean) param.args[1];
            Object flash = XposedHelpers.getObjectField(param.thisObject, "flashlightController");
            if (enabled) {
                if (getFlashLightEnabled(mContext) == 1 && !isListening) {
                    setFlashLightEnabled(mContext, 0);
                }
                if (!isListening) listening(mContext, param, flash, isListening);
                setFlashLightEnabled(mContext, 1);
                // logE("tileUpdateState: isListening1: " + isListening);
            } else if ((boolean) XposedHelpers.callMethod(flash, "isEnabled")) {
                if (getFlashLightEnabled(mContext) == 1 && !isListening) {
                    setFlashLightEnabled(mContext, 0);
                }
                if (!isListening) listening(mContext, param, flash, isListening);
                setFlashLightEnabled(mContext, 1);
                // logE("tileUpdateState: isListening2: " + isListening + " call: " + (boolean) XposedHelpers.callMethod(flash, "isEnabled"));
            } else {
                setFlashLightEnabled(mContext, 0);
                if (isListening) {
                    listening(mContext, param, flash, isListening);
                    contentObserver = (ContentObserver) XposedHelpers.getAdditionalInstanceField(param.thisObject, "tileListener");
                    if (contentObserver != null) {
                        mContext.getContentResolver().unregisterContentObserver(contentObserver);
                    }
                }
                // logE("tileUpdateState: isListening3: " + isListening);
            }
        }
        return null;
    }


    public void listening(Context mContext, XC_MethodHook.MethodHookParam param, Object flash, boolean isListening) {
        if (!isListening) {
            ContentObserver contentObserver = new ContentObserver(new Handler(mContext.getMainLooper())) {
                @Override
                public void onChange(boolean selfChange, @Nullable Uri uri) {
                    super.onChange(selfChange, uri);
                    hookFlash(param.thisObject, flash, mContext, readFile());
                    // logE("listening: listening: selfChange: " + selfChange + " uri: " + uri);
                }
            };
            mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor("flash_light_enabled"), false, contentObserver);
            XposedHelpers.setAdditionalInstanceField(param.thisObject, "tileListener", contentObserver);
            this.isListening = true;
        } else this.isListening = false;
    }

    public void hookFlash(Object o, Object flash, Context context, int max) {
        final int maxPath = max;
        findAndHookMethod("com.android.systemui.controlcenter.policy.MiuiBrightnessController",
            "lambda$onChanged$0", boolean.class, float.class, new MethodHook() {
                @Override
                protected void before(MethodHookParam param) {
                    // logE("MiuiBrightnessController lambda$onChanged$0: " + param.args[0] + " 2: " + param.args[1]);
                    Context mContext = (Context) XposedHelpers.getObjectField(param.thisObject, "mContext");
                    int enabled = getFlashLightEnabled(mContext);
                    // logE("lambda$onChanged$0 enabled: " + enabled);
                    int i;
                    if (enabled == 1) {
                        float floatValue = (Float) param.args[1];
                        // if (floatValue < 0.03f && floatValue > 0.01f) {
                        //     i = Math.round(floatValue * 600);
                        // } else if (floatValue < 0.009f) {
                        //     i = Math.round(floatValue * 700);
                        // } else if (floatValue < 0.005f) {
                        //     i = Math.round(floatValue * 800);
                        // } else
                        i = Math.round(floatValue * 500);
                        if (i != 0) {
                            if (maxPath != -1 && i > maxPath) {
                                i = maxPath;
                                // } else if (i > 400) {
                                //     i = 400;
                                //     writeFile(i);
                            }
                            // logE("lambda$onChanged$0 i: " + i);
                            writeFile(i);
                        } else {
                            XposedHelpers.callMethod(flash, "setFlashlight", false);
                            XposedHelpers.callMethod(o, "refreshState");
                        }
                        param.setResult(null);
                    }
                }
            }
        );

        findAndHookMethod("com.android.systemui.controlcenter.policy.MiuiBrightnessController$5",
            "run", new MethodHook() {
                @Override
                protected void before(MethodHookParam param) {
                    int enabled = getFlashLightEnabled(context);
                    // logE("MiuiBrightnessController$5 enabled: " + enabled);
                    if (enabled == 1) {
                        // logE("MiuiBrightnessController$5 run");
                        param.setResult(null);
                    }
                }
            }
        );
    }

    public int getFlashLightEnabled(Context context) {
        try {
            return Settings.System.getInt(context.getContentResolver(), "flash_light_enabled");
        } catch (Settings.SettingNotFoundException e) {
            logE("No Found flash_light_enabled: " + e);
            return -1;
        }
    }

    public void setFlashLightEnabled(Context context, int set) {
        Settings.System.putInt(context.getContentResolver(), "flash_light_enabled", set);
    }

    public int readFile() {
        String line;
        BufferedReader reader = null;
        StringBuilder builder = null;
        File file = new File(maxFile);
        if (file.exists()) {
            try {
                reader = new BufferedReader(new FileReader(maxFile));
                builder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } catch (IOException e) {
                logE("Error to read: " + maxFile, e);
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    logE("Close reader error: ", e);
                }
            }
        } else {
            logE("Not Found FlashLight File: " + maxFile);
        }

        if (builder != null) {
            return Integer.parseInt(builder.toString());
        }
        return -1;
    }

    public void writeFile(int flashInt) {
        File file = new File(flashFileMtk);
        if (file.exists()) {
            writeFileModule(flashFileMtk, flashInt);
        } else {
            File file1 = new File(flashFileTorch);
            File file2 = new File(flashFileSwitch);
            File file3 = new File(flashFileOther);
            if (file1.exists()) {
                writeFileModule(flashFileTorch, flashInt);
                if (file3.exists()) {
                    writeFileModule(flashFileOther, flashInt);
                }
            } else
                logE("Not Found FlashLight File: " + flashFileMtk + " And: " + flashFileTorch);
            // if (file1.exists() && file2.exists()) {
            //     writeFileModule(flashFileTorch, flashInt);
            //     writeFileModule(flashFileSwitch, 1);
            //     writeFileModule(flashFileSwitch, 0);
            // } else if (file1.exists()) {
            //     writeFileModule(flashFileTorch, flashInt);
            // } else
            //     logE("Not Found FlashLight File: " + flashFileMtk + " And: " + flashFileTorch);
        }
    }

    public void writeFileModule(String filePath, int flashInt) {
        try (FileWriter writer = new FileWriter(filePath, false)) {
            writer.write(Integer.toString(flashInt));
            writer.flush();
        } catch (IOException e) {
            if (!suGet) {
                ShellUtils.execCommand("chmod 777 " + filePath, true, false);
                suGet = true;
            }
            try (FileWriter writer = new FileWriter(filePath, false)) {
                writer.write(Integer.toString(flashInt));
                writer.flush();
            } catch (IOException f) {
                logE("Write FlashLight File Error: " + f + " File Path: " + filePath);
            }
        }
    }
}
