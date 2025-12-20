package com.whgkswo.tesm.audio.bgm.loader;

import lombok.Getter;
import net.minecraft.util.Identifier;

import java.util.*;

@Getter
public class Playlist {
    private final String path;
    private final List<Identifier> tracks = new ArrayList<>();
    private final Queue<Identifier> queue = new LinkedList<>();

    public Playlist(String path) {
        this.path = path;
    }

    public void fillQueue(String path){
        List<Identifier> shuffled = new ArrayList<>(tracks);
        Collections.shuffle(shuffled);

        queue.addAll(shuffled);
    }
}
