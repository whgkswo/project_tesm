package com.whgkswo.tesm.audio.bgm.selector;

import com.whgkswo.tesm.audio.core.gamestate.GameState;
import com.whgkswo.tesm.core.region.Province;
import com.whgkswo.tesm.core.time.TimePeriodOfDay;
import net.minecraft.client.MinecraftClient;

public class MusicSelector {

    public String selectTrackList(GameState state){

        if(state.equals(GameState.MENU)) return MusicPathHelper.getMainThemePath();

        long tick = MinecraftClient.getInstance().world.getTimeOfDay();
        TimePeriodOfDay period = TimePeriodOfDay.of(tick);

        if(period == null) return null;

        return MusicPathHelper.getAmbientTrackListPath(Province.CYRODIIL, period);
    }
}
