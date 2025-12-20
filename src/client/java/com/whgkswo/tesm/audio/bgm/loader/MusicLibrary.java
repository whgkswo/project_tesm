package com.whgkswo.tesm.audio.bgm.loader;

import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.util.*;

public class MusicLibrary {

    private final Map<String, Playlist> playlists = new HashMap<>();

    public void scanMusicFiles(ResourceManager resourceManager) {
        Map<Identifier, Resource> resources = resourceManager.findResources(
                "musics", id -> id.getPath().endsWith(".ogg")
        );

        for (Identifier track : resources.keySet()) {
            String path = track.getPath();

            // 파일명 제외한 디렉토리 경로
            int lastSlash = path.lastIndexOf('/');
            String dirPath = path.substring(0, lastSlash);

            Playlist playlist = playlists.computeIfAbsent(dirPath, k -> new Playlist(dirPath));
            playlist.getTracks().add(track);
        }
    }

    public Optional<Identifier> getNextTrack(String path) {

        if(!playlists.containsKey(path)) return Optional.empty();

        Playlist pl = playlists.get(path);
        Queue<Identifier> queue = pl.getQueue();

        if(queue.isEmpty()) pl.fillQueue(path);
        return Optional.ofNullable(queue.poll());
    }

    public void clear(){
        playlists.clear();
    }
}
