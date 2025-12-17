package com.whgkswo.tesm.audio.bgm.loader;

import com.whgkswo.tesm.Tesm;
import com.whgkswo.tesm.audio.bgm.manager.BackgroundMusicManager;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

public class MusicReloader implements SimpleSynchronousResourceReloadListener {
    private final BackgroundMusicManager manager;

    public MusicReloader(BackgroundMusicManager manager) {
        this.manager = manager;
    }


    @Override
    public ResourceLocation getFabricId() {
        return new ResourceLocation(Tesm.MOD_ID, "music_reloader");
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        manager.onResourceReloadStart();
        manager.getMusicContainer().scanMusicFiles(resourceManager);
        manager.onResourceReloadComplete();
    }
}
