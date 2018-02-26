package util;

import java.time.LocalDate;
import java.time.Month;
import java.util.Objects;

/**
 * Created by Marisha on 25/02/2018.
 */
public class DateUtil {
    public static LocalDate of(int year, Month month) {
        return LocalDate.of(year, month, 1);
    }

}
