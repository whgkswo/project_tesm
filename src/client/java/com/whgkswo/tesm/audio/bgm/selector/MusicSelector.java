package com.whgkswo.tesm.audio.bgm.selector;

import com.whgkswo.tesm.audio.core.gamestate.GameState;
import com.whgkswo.tesm.core.region.Province;
import com.whgkswo.tesm.core.time.TimeOfDay;

public class MusicSelector {

    public String selectMusic(GameState state){

        if(state.equals(GameState.MENU)) return MusicPathHelper.getMainThemePath();

        return MusicPathHelper.getAmbientTrackListPath(Province.CYRODIIL, TimeOfDay.DAY);
    }
}
