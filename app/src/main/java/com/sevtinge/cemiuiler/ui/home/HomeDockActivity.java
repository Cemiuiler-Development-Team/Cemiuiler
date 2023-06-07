package com.sevtinge.cemiuiler.ui.home;

import androidx.fragment.app.Fragment;

import com.sevtinge.cemiuiler.R;
import com.sevtinge.cemiuiler.ui.PickerHomeActivity;
import com.sevtinge.cemiuiler.ui.base.SubFragment;
import com.sevtinge.cemiuiler.ui.home.base.BaseHomeActivity;

import moralnorm.preference.Preference;

public class HomeDockActivity extends BaseHomeActivity {

    @Override
    public Fragment initFragment() {
        return new HomeDockFragment();
    }

    public static class HomeDockFragment extends SubFragment {

        Preference mHomeBackgroundBlur;

        @Override
        public int getContentResId() {
            return R.xml.home_dock;
        }

        /*@Override
        public void initPrefs() {
            SwitchPreference mHomeDockCustom = findPreference("prefs_key_home_dock_bg_custom_enable");

            PreferenceCategory mHomeDockCustomCat = findPreference("prefs_key_home_dock_bg_custom_cat");
            mHomeDockCustomCat.setVisible(PrefsUtils.getSharedBoolPrefs(getActivity(),"prefs_key_home_dock_bg_custom_enable",false));
            mHomeBackgroundBlur = findPreference("prefs_key_home_dock_bg_custom");
            mHomeBackgroundBlur.setVisible(SdkHelper.isAndroidTiramisu()||SdkHelper.isAndroidS());

            mHomeDockCustom.setOnPreferenceChangeListener((preference, o) -> {

                if ((Boolean) o) {
                    mHomeDockCustomCat.setVisible(true);
                } else {
                    mHomeDockCustomCat.setVisible(false);
                }

                return true;
            });

            mHomeDockCustom.setOnPreferenceClickListener(preference -> false);

            /*SeekBarPreference mDockHeight = findPreference("prefs_key_home_dock_height");
            mDockHeight.setMax(getActivity().getResources().getDisplayMetrics().heightPixels);*//*
        }*/

        @Override
        public boolean onPreferenceTreeClick(Preference preference) {
            if (preference == findPreference("prefs_key_home_dock_bg_custom")) {
                openMultiAction(preference, null, PickerHomeActivity.Actions.Blur);
            }
            return true;
        }
    }
}
