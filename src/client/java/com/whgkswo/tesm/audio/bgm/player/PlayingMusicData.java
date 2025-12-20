package com.whgkswo.tesm.audio.bgm.player;

import com.whgkswo.tesm.audio.bgm.player.transition.TransitionData;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.Identifier;


public class PlayingMusicData {

    @Getter
    @Setter
    private int sourceId = -1;
    @Getter
    @Setter
    private int bufferId = -1;
    @Getter
    @Setter
    private Identifier musicLocation;

    public PlayingMusicData(int sourceId, int bufferId, Identifier musicLocation){
        this.sourceId = sourceId;
        this.bufferId = bufferId;
        this.musicLocation = musicLocation;
    }

    @Getter
    private TransitionData transition = new TransitionData();
}
