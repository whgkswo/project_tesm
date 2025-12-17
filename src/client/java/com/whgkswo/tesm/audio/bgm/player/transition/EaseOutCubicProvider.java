package com.whgkswo.tesm.audio.bgm.player.transition;

public class EaseOutCubicProvider implements FadingVolumeProvider{

    @Override
    public float getVolume(float x) {
        return (float) Math.pow(x, 2);
    }
}
