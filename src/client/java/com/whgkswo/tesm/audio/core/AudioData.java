package com.whgkswo.tesm.audio.core;

import java.nio.ShortBuffer;

public record AudioData(
        ShortBuffer buffer,
        int format,
        int sampleRate
) {
}
