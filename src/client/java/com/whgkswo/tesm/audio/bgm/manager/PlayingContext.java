package com.whgkswo.tesm.audio.bgm.manager;

import com.whgkswo.tesm.audio.core.gamestate.GameState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PlayingContext {

    private int tickCounter;

    private GameState gameState;

    private MusicSituation situation;


    public void countdownTick(){
        tickCounter--;
    }

    public void resetTickCounter(int tickCounter){
        this.tickCounter = tickCounter;
    }
}
