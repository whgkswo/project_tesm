package com.whgkswo.tesm.core.time;

import lombok.Getter;

@Getter
public class TimePeriod {
    private final long startTickOfDay;
    private final long endTickOfDay;

    private TimePeriod(long startTickOfDay, long endTickOfDay) {
        this.startTickOfDay = startTickOfDay;
        this.endTickOfDay = endTickOfDay;
    }

    public static TimePeriod fromHourMinute(int startH, int startM, int endH, int endM){
        GameTime startTime = GameTime.fromHourMinute(startH, startM);
        GameTime endTime = GameTime.fromHourMinute(endH, endM);
        return fromTime(startTime, endTime);
    }

    public static TimePeriod fromTime(GameTime startTime, GameTime endTime){
        return new TimePeriod(startTime.getTick(), endTime.getTick());
    }

    public boolean contains(long tick){
        tick %= TimeConstants.TICKS_PER_DAY;

        if (startTickOfDay <= endTickOfDay) {
            return tick >= startTickOfDay && tick < endTickOfDay;
        } else {
            return tick >= startTickOfDay || tick < endTickOfDay;
        }
    }
}
