package com.whgkswo.tesm.audio.bgm.loader;

import com.whgkswo.tesm.Tesm;
import com.whgkswo.tesm.audio.bgm.manager.BackgroundMusicManager;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

public class MusicReloader implements SimpleSynchronousResourceReloadListener {
    private final BackgroundMusicManager manager;

    public MusicReloader(BackgroundMusicManager manager) {
        this.manager = manager;
    }


    @Override
    public Identifier getFabricId() {
        return new Identifier(Tesm.MOD_ID, "music_reloader");
    }

    @Override
    public void reload(ResourceManager resourceManager) {
        manager.onResourceReloadStart();
        manager.getMusicLibrary().scanMusicFiles(resourceManager);
        manager.onResourceReloadComplete();
    }
}
