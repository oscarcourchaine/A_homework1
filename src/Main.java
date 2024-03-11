import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        testEnrollmentParser();
        testLISPChecker();
    }

    private static void testEnrollmentParser() throws IOException {
        CSVEnrollmentParser.generateTestFile("", 25);
        CSVEnrollmentParser.parseCSVEnrollment("test1.csv", "");
    }

    private static void testLISPChecker() {
        String[] valid_strings = {"USER(1): (* 2 (cos 0) (+ 4 6))", " (defmethod rewind-state ((rewindable rewindable))\n" +
                "   (invariant (not (zerop (rewind-count rewindable))))\n" +
                "   (setf (rewind-index rewindable) \n" +
                "         (mod (1+ (rewind-index rewindable)) (rewind-count rewindable)))\n" +
                "   (aref (rewind-store rewindable) \n" +
                "         (- (rewind-count rewindable) (rewind-index rewindable) 1)))"};

        String[] invalid_strings = {"", "USER(1): (* 2 (cos 0) (+ 4 6)))", "USER(1): )(* 2 (cos 0) (+ 4 6))"};

        for (String s : valid_strings) {
            if (!LISPChecker.isLISPValid(s)) {
                System.out.println("test failure for: " + s);
            }
        }
        for (String s : invalid_strings) {
            if (LISPChecker.isLISPValid(s)) {
                System.out.println("test failure for: " + s);
            }
        }

    }
}