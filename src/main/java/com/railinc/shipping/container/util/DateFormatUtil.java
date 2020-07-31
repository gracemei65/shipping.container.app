
package com.railinc.shipping.container.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtil {

    public static String formatTime(Long epoch) {
        if (epoch == null)
            return null;
        Date date = new Date(epoch);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return format.format(date);
    }
}
