package com.whgkswo.tesm.audio.core.gamestate;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.LevelLoadingScreen;
import net.minecraft.client.gui.screen.ProgressScreen;
import net.minecraft.client.gui.screen.TitleScreen;

public enum GameState {
    IN_GAME,
    MENU,
    LOADING,
    UNKNOWN
    ;

    public static GameState get(GameState prevState){
        MinecraftClient client = MinecraftClient.getInstance();

        if(client.world != null && client.player != null) return GameState.IN_GAME;

        if(client.currentScreen instanceof TitleScreen) return GameState.MENU;

        if(client.currentScreen instanceof ProgressScreen
        || client.currentScreen instanceof LevelLoadingScreen) return GameState.LOADING;

        if(prevState != null) return prevState;

        return GameState.UNKNOWN;
    }
}
