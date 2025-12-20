package com.whgkswo.tesm.audio.bgm.selector;

import com.whgkswo.tesm.core.region.Province;
import com.whgkswo.tesm.core.time.TimePeriod;
import com.whgkswo.tesm.core.time.TimePeriodOfDay;

public class MusicPathHelper {

    public static String getAmbientTrackListPath(Province province, TimePeriodOfDay timePeriod) {
        return String.format("musics/%s/ambient/%s", province.name().toLowerCase(), timePeriod.name().toLowerCase());
    }

    public static String getCombatTrackListPath(Province province) {
        return String.format("musics/%s/combat", province.name().toLowerCase());
    }

    public static String getMainThemePath(){
        return "musics/main";
    }
}
