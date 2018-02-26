package util;

import java.time.LocalDate;
import java.time.Month;

/**
 * Created by Marisha on 25/02/2018.
 */
public class DateUtil {
    public static LocalDate NOW = LocalDate.of(3000, 1, 1);
    public static LocalDate of(int year, Month month) {
        return LocalDate.of(year, month, 1);
    }

}
