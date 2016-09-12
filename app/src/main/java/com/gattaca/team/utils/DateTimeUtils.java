package com.gattaca.team.utils;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.Date;

/**
 * Created by Robert on 18.08.2016.
 */
public final class DateTimeUtils {

    public static Date toDate(LocalDateTime localDateTime){
       return org.threeten.bp.DateTimeUtils.toDate(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

    }

    public static Date startOfToday(){
        return toDate(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS));

    }

    public static Date endOfToday(){
        return toDate(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS).plus(1, ChronoUnit.DAYS).minus(1, ChronoUnit.NANOS));

    }
}
