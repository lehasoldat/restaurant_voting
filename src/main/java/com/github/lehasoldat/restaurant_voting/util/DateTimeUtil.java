package com.github.lehasoldat.restaurant_voting.util;

import lombok.experimental.UtilityClass;

import java.time.LocalTime;

@UtilityClass
public class DateTimeUtil {

    public static LocalTime getCurrentTime() {
        return LocalTime.now();
    }
}
