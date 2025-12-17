package com.whgkswo.tesm;

import com.whgkswo.tesm.audio.bgm.manager.BackgroundMusicManager;
import net.fabricmc.api.ClientModInitializer;

public class TesmClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

        BackgroundMusicManager.initialize();
	}
}