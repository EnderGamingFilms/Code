package tv.ender.lib.common;

import java.util.Random;

/**
 * Random String utility similar to Apache Commons RandomStringUtils
 */
public class RandomStringUtils {
    private static final Random RANDOM = new Random();

    private RandomStringUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String random(int count) {
        return random(count, false, false);
    }

    public static String randomAscii(int count) {
        return random(count, 32, 127, false, false);
    }

    public static String randomAlphabetic(int count) {
        return random(count, true, false);
    }

    public static String randomAlphanumeric(int count) {
        return random(count, true, true);
    }

    public static String randomNumeric(int count) {
        return random(count, false, true);
    }

    public static String random(int count, boolean letters, boolean numbers) {
        return random(count, 0, 0, letters, numbers);
    }

    public static String random(int count, int start, int end, boolean letters, boolean numbers) {
        return random(count, start, end, letters, numbers, null, RANDOM);
    }

    public static String random(int count, int start, int end, boolean letters, boolean numbers, char... chars) {
        return random(count, start, end, letters, numbers, chars, RANDOM);
    }

    public static String random(int count, int start, int end, boolean letters, boolean numbers, char[] chars, Random random) {
        if (count == 0) {
            return "";
        } else if (count < 0) {
            throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
        } else if (chars != null && chars.length == 0) {
            throw new IllegalArgumentException("The chars array must not be empty");
        } else {
            if (start == 0 && end == 0) {
                if (chars != null) {
                    end = chars.length;
                } else if (!letters && !numbers) {
                    end = Integer.MAX_VALUE;
                } else {
                    end = 123;
                    start = 32;
                }
            } else if (end <= start) {
                throw new IllegalArgumentException("Parameter end (" + end + ") must be greater than start (" + start + ")");
            }

            char[] buffer = new char[count];
            int gap = end - start;

            while (count-- != 0) {
                char ch;
                if (chars == null) {
                    ch = (char) (random.nextInt(gap) + start);
                } else {
                    ch = chars[random.nextInt(gap) + start];
                }

                if (letters && Character.isLetter(ch) || numbers && Character.isDigit(ch) || !letters && !numbers) {
                    if (ch >= '\udc00' && ch <= '\udfff') {
                        if (count == 0) {
                            ++count;
                        } else {
                            buffer[count] = ch;
                            --count;
                            buffer[count] = (char) ('\ud800' + random.nextInt(128));
                        }
                    } else if (ch >= '\ud800' && ch <= '\udb7f') {
                        if (count == 0) {
                            ++count;
                        } else {
                            buffer[count] = (char) ('\udc00' + random.nextInt(128));
                            --count;
                            buffer[count] = ch;
                        }
                    } else if (ch >= '\udb80' && ch <= '\udbff') {
                        ++count;
                    } else {
                        buffer[count] = ch;
                    }
                } else {
                    ++count;
                }
            }

            return new String(buffer);
        }
    }

    public static String random(int count, String chars) {
        return chars == null ? random(count, 0, 0, false, false, null, RANDOM) : random(count, chars.toCharArray());
    }

    public static String random(int count, char... chars) {
        return chars == null ? random(count, 0, 0, false, false, null, RANDOM) : random(count, 0, chars.length, false, false, chars, RANDOM);
    }
}