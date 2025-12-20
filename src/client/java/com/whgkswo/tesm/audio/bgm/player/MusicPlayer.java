package com.whgkswo.tesm.audio.bgm.player;

import com.whgkswo.tesm.audio.bgm.player.transition.TransitionState;
import com.whgkswo.tesm.audio.core.AudioHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import org.lwjgl.openal.AL10;

import java.util.concurrent.CompletableFuture;

public class MusicPlayer {

    private PlayingMusicData currentMusic;
    private CompletableFuture<?> loadingFuture;


    public void play(Identifier track){

        loadingFuture = AudioHelper.playMusicAsync(track, getBaseVolume())
                .thenAccept(musicData -> {
                    currentMusic = musicData;
                    loadingFuture = null;
                })
                .exceptionally(e -> {
                    System.err.println("Failed to play music: " + e.getMessage());
                    e.printStackTrace();
                    loadingFuture = null;
                    return null;
                });
    }

    public void update(){
        if(currentMusic != null){
            int state = AL10.alGetSourcei(currentMusic.getSourceId(), AL10.AL_SOURCE_STATE);

            if(state == AL10.AL_STOPPED){
                currentMusic = null;

            } else if(state == AL10.AL_PLAYING){
                currentMusic.getTransition().tick();
                float volume = getBaseVolume();
                float fadeVolume = currentMusic.getTransition().getFadeVolume();
                AL10.alSourcef(currentMusic.getSourceId(), AL10.AL_GAIN, volume * fadeVolume);

                if(currentMusic.getTransition().isFinished()){
                    AudioHelper.stopMusic(currentMusic);
                    currentMusic = null;
                }
            }
        }
    }

    public boolean isMusicPlaying(){
        return currentMusic != null;
    }
    
    private float getBaseVolume(){
        MinecraftClient client = MinecraftClient.getInstance();
        GameOptions options = client.options;
        
        float musicVolume = options.getSoundVolume(SoundCategory.MUSIC);
        float masterVolume = options.getSoundVolume(SoundCategory.MASTER);
        
        return musicVolume * masterVolume;
    }

    public void stopMusic(int fadeDuration){
        if(currentMusic != null)
            currentMusic.getTransition().startFade(TransitionState.FADE_OUT, fadeDuration);
    }
}
