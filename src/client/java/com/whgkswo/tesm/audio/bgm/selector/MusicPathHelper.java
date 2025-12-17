package com.whgkswo.tesm.audio.bgm.selector;

import com.whgkswo.tesm.core.region.Province;
import com.whgkswo.tesm.core.time.TimeOfDay;

public class MusicPathHelper {

    public static String getAmbientTrackListPath(Province province, TimeOfDay timeOfDay) {
        return String.format("musics/%s/ambient/%s", province.name().toLowerCase(), timeOfDay.name().toLowerCase());
    }

    public static String getCombatTrackListPath(Province province) {
        return String.format("musics/%s/combat", province.name().toLowerCase());
    }

    public static String getMainThemePath(){
        return "musics/main";
    }
}
