public class Validator {

    public static boolean isValidObjectName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    public static boolean isValidDescription(String description) {
        return description != null && description.length() <= 255;
    }

    public static boolean isValidDate(String date) {
        // Updated date validation (format: DD-MM-YYYY)
        return date != null && date.matches("\\d{2}-\\d{2}-\\d{4}");
    }

    public static boolean isValidId(int id) {
        return id > 0;
    }
}