package com.whgkswo.tesm.audio.core.gamestate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.TitleScreen;

public enum GameState {
    IN_GAME,
    MENU,
    LOADING,
    UNKNOWN
    ;

    public static GameState get(GameState prevState){
        Minecraft client = Minecraft.getInstance();

        if(client.level != null && client.player != null) return GameState.IN_GAME;

        if(client.screen instanceof TitleScreen) return GameState.MENU;

        if(client.screen instanceof LevelLoadingScreen) return GameState.LOADING;

        if(prevState != null) return prevState;

        return GameState.UNKNOWN;
    }
}
