package com.whgkswo.tesm.audio.core;

import com.whgkswo.tesm.audio.bgm.player.PlayingMusicData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.lwjgl.stb.STBVorbis;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.concurrent.CompletableFuture;

public class AudioHelper {

    public static AudioData loadOgg(InputStream stream) throws Exception {
        // ByteBuffer로 변환
        byte[] bytes = stream.readAllBytes();
        ByteBuffer buffer = ByteBuffer.allocateDirect(bytes.length);
        buffer.put(bytes);
        buffer.flip();

        // STB Vorbis로 디코딩
        IntBuffer channelsBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer sampleRateBuffer = BufferUtils.createIntBuffer(1);

        ShortBuffer pcm = STBVorbis.stb_vorbis_decode_memory(
                buffer,
                channelsBuffer,
                sampleRateBuffer
        );

        if (pcm == null) {
            throw new RuntimeException("Failed to decode OGG file");
        }

        int channels = channelsBuffer.get(0);
        int sampleRate = sampleRateBuffer.get(0);
        int format = channels == 1 ? AL10.AL_FORMAT_MONO16 : AL10.AL_FORMAT_STEREO16;

        return new AudioData(pcm, format, sampleRate);
    }

    private static int createBuffer(AudioData audioData){
        int bufferId = AL10.alGenBuffers();
        AL10.alBufferData(bufferId, audioData.format(), audioData.buffer(), audioData.sampleRate());

        return bufferId;
    }

    private static int createSource(float volume, int bufferId){
        int sourceId = AL10.alGenSources();
        AL10.alSourcei(sourceId, AL10.AL_BUFFER, bufferId);
        AL10.alSourcef(sourceId, AL10.AL_GAIN, volume);
        AL10.alSourcei(sourceId, AL10.AL_LOOPING, AL10.AL_FALSE);

        return sourceId;
    }

    public static PlayingMusicData playMusic(Identifier location, float volume){
        try{
            ResourceManager resourceManager = MinecraftClient.getInstance().getResourceManager();
            Resource resource = resourceManager.getResource(location).orElseThrow(() -> new RuntimeException("음악파일 로드 실패: " + location));
            InputStream stream = resource.getInputStream();

            AudioData audioData = loadOgg(stream);
            stream.close();

            int bufferId = AudioHelper.createBuffer(audioData);
            int sourceId = AudioHelper.createSource(volume, bufferId);

            AL10.alSourcePlay(sourceId);

            return new PlayingMusicData(sourceId, bufferId, location);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    // ogg 디코딩이 무거워서 비동기로 전환
    public static CompletableFuture<PlayingMusicData> playMusicAsync(Identifier location, float volume) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                ResourceManager resourceManager = MinecraftClient.getInstance().getResourceManager();
                Resource resource = resourceManager.getResource(location).orElseThrow(() -> new RuntimeException("음악파일 로드 실패: " + location));
                InputStream stream = resource.getInputStream();

                AudioData audioData = loadOgg(stream);
                stream.close();

                return audioData;

            } catch (Exception e) {
                throw new RuntimeException("Failed to load music: " + location, e);
            }
        }).thenApplyAsync(audioData -> {
            // OpenAL 작업은 메인 스레드에서
            int bufferId = AudioHelper.createBuffer(audioData);
            int sourceId = AudioHelper.createSource(volume, bufferId);
            AL10.alSourcePlay(sourceId);

            return new PlayingMusicData(sourceId, bufferId, location);

        }, runnable -> MinecraftClient.getInstance().execute(runnable));
    }

    public static void stopMusic(PlayingMusicData musicData){
        AL10.alSourceStop(musicData.getSourceId());
        AL10.alDeleteSources(musicData.getSourceId());
        AL10.alDeleteBuffers(musicData.getBufferId());
    }
}
