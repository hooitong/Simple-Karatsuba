import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * User: Yeap Hooi Tong
 * Matric: A0111736M
 */
public class Spaceship {

    public static void main(String[] args) {
        /* Main Input Control */
        Scanner sc = new Scanner(System.in);
        PrintWriter pw = new PrintWriter(
            new BufferedWriter(new OutputStreamWriter(System.out)));
        int numLines = sc.nextInt();
        for (int i = 0; i < numLines; i++) {
            int radix = sc.nextInt();
            String velocity = sc.next();
            String mass = sc.next();
            pw.write(calculateMomentum(radix, velocity, mass));
            pw.write("\n");
        }
        pw.close();
    }

    public static String calculateMomentum(int radix, String velocity,
                                           String mass) {
        int[] velocityArray = new int[5005];
        int[] massArray = new int[5005];
        int[] momentumArray = new int[10010];

        int velocityRadixIndex = 0, massRadixIndex = 0, radixOffset = 0;
        char c;

        /* Convert both strings into the array format */
        for (int i = 5004; i >= 5005 - velocity.length(); i--) {
            //character.digit with radix and plug into array
            c = velocity.charAt(i - (5005 - velocity.length()));
            if (c == '.') {
                velocityRadixIndex = 5004 - i;
                radixOffset = 1;
            } else {
                velocityArray[i + radixOffset] = parseDigit(c);
            }
        }

        radixOffset = 0;
        for (int i = 5004; i >= 5005 - mass.length(); i--) {
            //character.digit with radix and plug into array
            c = mass.charAt(i - (5005 - mass.length()));
            if (c == '.') {
                massRadixIndex = 5004 - i;
                radixOffset = 1;
            } else {
                massArray[i + radixOffset] = parseDigit(c);
            }
        }

        /* Perform multiplication */
        for (int i = 5004; i >= 5005 - mass.length(); i--) {
            for (int j = 5004; j >= 5005 - velocity.length(); j--) {
                //multiply [i] with [j] + carryValue. add into j + additionOffset. int / radix to get carry. modulo to get current value. . set carryValue.
                momentumArray[j + i + 1] += massArray[i] * velocityArray[j];
                momentumArray[j + i] += momentumArray[j + i + 1] / radix;
                momentumArray[j + i + 1] %= radix;
            }
        }

        /* Convert momentumArray into String */
        int finalRadix = velocityRadixIndex + massRadixIndex;
        int maxPos = velocity.length() + mass.length();
        String finalAnswer = "";

        for (int i = 10010 - maxPos; i <= 10009; i++) {
            finalAnswer += toDigit(momentumArray[i]);
            if (i == 10009 - finalRadix) {
                finalAnswer += ".";
            }
        }

        return trimZeros(finalAnswer);
    }

    /**
     * Use to trim leading and trailing zeros on a result string.
     */
    private static String trimZeros(String input) {
        int left = 0;
        int right = input.length() - 1;
        int fp = input.indexOf('.');
        if (fp == -1) {
            fp = input.length();
        }

        while (left < fp - 1) {
            if (input.charAt(left) != '0') { break; }
            left++;
        }

        while (right >= fp) {
            if (input.charAt(right) != '0') {
                if (input.charAt(right) == '.') { right--; }
                break;
            }
            right--;
        }

        if (left >= fp) { return "0" + input.substring(left, right + 1); }
        return input.substring(left, right + 1);
    }

    /**
     * Convert digit to int (for reading)
     */
    private static int parseDigit(char c) {
        if (c <= '9') {
            return c - '0';
        }
        return c - 'A' + 10;
    }

    /**
     * Convert int to digit. (for printing)
     */
    private static char toDigit(int digit) {
        if (digit <= 9) {
            return (char) (digit + '0');
        }
        return (char) (digit - 10 + 'A');
    }
}
