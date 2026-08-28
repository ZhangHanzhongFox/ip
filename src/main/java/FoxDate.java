import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

/** Provides the single date format used by Fox's Level-8 deadline support. */
public final class FoxDate {
    /** Format accepted in commands and used for typed values in storage. */
    public static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern(
            "uuuu-MM-dd", Locale.ROOT).withResolverStyle(ResolverStyle.STRICT);
    /** Format shown to users for typed deadline dates. */
    public static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern(
            "MMM dd uuuu", Locale.ENGLISH);

    private FoxDate() {
        // Utility class.
    }

    /** Parses a strict ISO-style date and turns malformed input into a Fox error. */
    public static LocalDate parse(String value) throws FoxException {
        try {
            return LocalDate.parse(value, INPUT_FORMAT);
        } catch (DateTimeParseException exception) {
            throw new FoxException("☹ OOPS!!! Please enter a valid date in yyyy-MM-dd format.");
        }
    }

    /** Formats a typed date for display. */
    public static String format(LocalDate date) {
        return DISPLAY_FORMAT.format(date);
    }
}
