package utility;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeFunctions {

    public static String getTimeScreenShot() {
        return new SimpleDateFormat("HH.mm.ss.SSS").format(new Date().getTime());
    }

    public static String getCurrentDate() {
        return new SimpleDateFormat("yyyy.MM.dd").format(new Date().getTime());
    }

    public static String getCurrentDateBrFormat() {
        return new SimpleDateFormat("dd/MM/yyyy").format(new Date().getTime());
    }
}
