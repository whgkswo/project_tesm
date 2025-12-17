package com.whgkswo.tesm.audio.bgm.player;

import com.whgkswo.tesm.audio.bgm.player.transition.TransitionState;
import com.whgkswo.tesm.audio.core.AudioHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import org.lwjgl.openal.AL10;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class MusicPlayer {

    private PlayingMusicData currentMusic;
    private CompletableFuture<?> loadingFuture;

    private Queue<ResourceLocation> musicQueue = new LinkedList<>();

    public void playNext(){
        if(musicQueue.isEmpty()) return;

        ResourceLocation location = musicQueue.poll();

        //currentMusic = AudioHelper.playMusic(location, getBaseVolume());

        loadingFuture = AudioHelper.playMusicAsync(location, getBaseVolume())
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

    public boolean isQueueEmpty(){
        return musicQueue.isEmpty();
    }

    public void addToQueue(List<ResourceLocation> locations){
        List<ResourceLocation> shuffled = new ArrayList<>(locations);
        Collections.shuffle(shuffled);
        musicQueue.addAll(shuffled);
    }

    public void clearQueue(){
        musicQueue.clear();
    }

    public boolean isMusicPlaying(){
        return currentMusic != null;
    }
    
    private float getBaseVolume(){
        Minecraft client = Minecraft.getInstance();
        Options options = client.options;
        
        float musicVolume = options.getSoundSourceVolume(SoundSource.MUSIC);
        float masterVolume = options.getSoundSourceVolume(SoundSource.MASTER);
        
        return musicVolume * masterVolume;
    }

    public void stopMusic(int fadeDuration){
        if(currentMusic != null)
            currentMusic.getTransition().startFade(TransitionState.FADE_OUT, fadeDuration);
    }
}
