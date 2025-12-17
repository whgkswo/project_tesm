package com.whgkswo.tesm.audio.bgm.manager;

import com.whgkswo.tesm.audio.bgm.loader.MusicContainer;
import com.whgkswo.tesm.audio.bgm.loader.MusicReloader;
import com.whgkswo.tesm.audio.bgm.player.MusicPlayer;
import com.whgkswo.tesm.audio.bgm.selector.MusicSelector;
import com.whgkswo.tesm.audio.core.gamestate.GameState;
import lombok.Getter;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;

import java.util.List;

public class BackgroundMusicManager {

    public static final int NORMAL_INTERVAL = 100;
    public static final int SHORT_INTERVAL = 20;
    public static final int NORMAL_FADE_DURATION = 50;
    public static final int FAST_FADE_DURATION = 35;

    private boolean isResourceLoaded = false;

    @Getter
    private final MusicContainer musicContainer = new MusicContainer();

    private final MusicPlayer musicPlayer = new MusicPlayer();
    private final MusicSelector musicSelector = new MusicSelector();
    private final PlayingContext playingContext = new PlayingContext(0, GameState.UNKNOWN, MusicSituation.NONE);

    private static BackgroundMusicManager instance;

    private static BackgroundMusicManager getInstance(){
        return instance == null ? new BackgroundMusicManager() : instance;
    }

    public static void initialize() {
        BackgroundMusicManager manager = getInstance();

        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES)
                        .registerReloadListener(new MusicReloader(manager));

        ClientTickEvents.END_CLIENT_TICK.register(client -> manager.tick());
    }

    private void tick(){
        if(!isResourceLoaded) return;

        playingContext.countdownTick();

        handleStateChange();

        if(shouldPlayNextTrack()) {
            playNextTrack();
        }else {
            musicPlayer.update();
        }
    }

    // 메뉴 - 인게임 전환 처리
    private void handleStateChange(){
        GameState prevState = playingContext.getGameState();
        GameState newState = GameState.get(playingContext.getGameState());
        playingContext.setGameState(newState);

        if(!prevState.equals(GameState.UNKNOWN)
                && !prevState.equals(GameState.LOADING) // 메뉴 -> 로딩 갈 때만, 로딩 -> 인게임 갈때는 x
                && !prevState.equals(newState)){

            musicPlayer.clearQueue();
            stopTrack(FAST_FADE_DURATION, SHORT_INTERVAL);
        }
    }

    private void stopTrack(int fadeDuration, int delay){
        playingContext.resetTickCounter(fadeDuration + delay);
        musicPlayer.stopMusic(fadeDuration);
    }

    private boolean shouldPlayNextTrack(){
        return isResourceLoaded
                && playingContext.getTickCounter() <= 0
                && !musicPlayer.isMusicPlaying();
    }

    private void playNextTrack(){
        playingContext.resetTickCounter(NORMAL_INTERVAL);

        if(musicPlayer.isQueueEmpty()){
            String path = musicSelector.selectMusic(playingContext.getGameState());

            List<ResourceLocation> trackList = musicContainer.getTrackList(path);

            musicPlayer.addToQueue(trackList);
        }

        musicPlayer.playNext();
    }

    public void onResourceReloadStart(){
        isResourceLoaded = false;

        musicPlayer.stopMusic(0);
        musicPlayer.clearQueue();
    }

    public void onResourceReloadComplete(){
        isResourceLoaded = true;
    }
}
