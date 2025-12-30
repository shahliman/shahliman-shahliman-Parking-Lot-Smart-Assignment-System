package parking.smart.assignment.util;

import java.util.regex.Pattern;

public class PlateValidator {
    public static final String Plate_Pattern = "^[0-9]{2}-[A-Z]{2,3}-[0-9]{3,4}$";

    public static boolean isValid(String plate) {
        if (plate == null)
            return false;
        return Pattern.matches(Plate_Pattern, plate.toUpperCase());
    }

}
