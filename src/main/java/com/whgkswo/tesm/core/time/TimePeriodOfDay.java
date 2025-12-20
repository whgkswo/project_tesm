package com.whgkswo.tesm.core.time;

import java.util.Arrays;

public enum TimePeriodOfDay {
    MORNING(TimePeriod.fromHourMinute(4, 30, 6, 20)),
    DAY(TimePeriod.fromHourMinute(6, 40, 14, 40)),
    EVENING(TimePeriod.fromHourMinute(16, 40, 18, 40)),
    NIGHT(TimePeriod.fromHourMinute(18, 0, 6, 0))
    ;

    private final TimePeriod period;

    TimePeriodOfDay(TimePeriod period){
        this.period = period;
    }

    public static TimePeriodOfDay of(long tick) {
        return Arrays.stream(values())
                .filter(p -> p.period.contains(tick))
                .findFirst()
                .orElse(null);
    }
}
