package com.whgkswo.tesm.audio.bgm.loader;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MusicContainer {

    private final Map<String, List<ResourceLocation>> tracks = new HashMap<>();

    public void scanMusicFiles(ResourceManager resourceManager) {
        Map<ResourceLocation, Resource> resources = resourceManager.listResources(
                "musics", id -> id.getPath().endsWith(".ogg")
        );

        for (ResourceLocation location : resources.keySet()) {
            String path = location.getPath(); // "musics/cyrodiil/ambient/day/track1.ogg"

            // 파일명 제외한 디렉토리 경로
            int lastSlash = path.lastIndexOf('/');
            String dirPath = path.substring(0, lastSlash); // "musics/cyrodiil/ambient/day"

            tracks.computeIfAbsent(dirPath, k -> new ArrayList<>())
                    .add(location);
        }
    }

    public List<ResourceLocation> getTrackList(String path){
        return tracks.get(path);
    }
}
