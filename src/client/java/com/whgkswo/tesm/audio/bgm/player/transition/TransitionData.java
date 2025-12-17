package com.whgkswo.tesm.audio.bgm.player.transition;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransitionData {
    private TransitionState state = TransitionState.NONE;
    private int fadeCounter;
    private int fadeDuration;
    private FadingVolumeProvider volumeProvider = new EaseOutCubicProvider();

    public void startFade(TransitionState state, int duration){
        this.state = state;
        fadeCounter = 0;
        fadeDuration = duration;
    }

    public float getFadeVolume(){
        float progress = (float) fadeCounter / fadeDuration;

        return switch (state) {
            case FADE_IN -> volumeProvider.getVolume(progress);
            case FADE_OUT -> volumeProvider.getVolume(1 - progress);
            default -> 1;
        };
    }

    public boolean isFinished(){
        return state != TransitionState.NONE && fadeCounter >= fadeDuration;
    }

    public void tick(){
        fadeCounter++;
    }

    public TransitionState getState(){
        return state;
    }
}
