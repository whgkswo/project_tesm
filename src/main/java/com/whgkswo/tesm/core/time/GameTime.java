package com.whgkswo.tesm.core.time;

import lombok.Getter;
import net.minecraft.world.World;

import static com.whgkswo.tesm.core.time.TimeConstants.*;

@Getter
public class GameTime {
    private final int hour;
    private final int minute;
    private final long tick;

    private GameTime(int hour, int minute, long tick) {
        if (hour < 0 || hour >= 24) {
            throw new IllegalArgumentException("Invalid hour: " + hour);
        }
        if (minute < 0 || minute >= 60) {
            throw new IllegalArgumentException("Invalid minute: " + minute);
        }

        this.hour = hour;
        this.minute = minute;
        this.tick = tick;
    }



    public static GameTime fromTick(long tick){
        tick %= TICKS_PER_DAY;
        int hour = (int) (tick / TICKS_PER_HOUR + DAY_START_HOUR) % 24;
        int minute = (int) ((tick % 1000) * TICKS_TO_MINUTE_FACTOR);

        return new GameTime(hour, minute, tick);
    }

    public static GameTime fromHourMinute(int hour, int minute){
        return new GameTime(hour, minute, calculateTickOfDay(hour, minute));
    }

    public static GameTime now(World world){
        long tick = world.getTimeOfDay() % 24000;

        int hour = (int) (tick / TICKS_PER_HOUR + 6) % 24;
        int minute = (int) ((tick % 1000) * TICKS_TO_MINUTE_FACTOR);

        return new GameTime(hour, minute, tick);
    }

    private static int calculateTickOfDay(int hour, int minute) {
        int hourTick = ((hour - 6 + 24) % 24) * TICKS_PER_HOUR;
        int minuteTick = (int) (minute * TICKS_PER_HOUR / 60f);
        return hourTick + minuteTick;
    }
}
