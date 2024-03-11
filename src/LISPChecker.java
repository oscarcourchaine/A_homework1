public class LISPChecker {

    /**
     * Checks a string of LISP code for valid parentheses nesting and closure.
     * @param code .
     * @return true if code is correct, false if not
     */
    public static boolean isLISPValid(String code) {
        String[] chars = code.split("");
        if (chars.length < 2) {
            return false;
        }
        int counter = 0;

        for (String c : chars) {
            if ("(".equals(c)) {
                counter--;
            } else if (")".equals(c)) {
                counter++;
            }

            if (counter > 0) {
                return false;
            }
        }
        return counter == 0;
    }
}
