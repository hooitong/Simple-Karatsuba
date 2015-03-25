import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

/**
 * User: Yeap Hooi Tong Matric: A0111736M
 */
public class Karatsuba {

    public static void main(String[] args) {
        /* Main Input Control */
        Scanner sc = new Scanner(System.in);
        PrintWriter pw = new PrintWriter(
            new BufferedWriter(new OutputStreamWriter(System.out)));
        int numLines = sc.nextInt();
        for (int i = 0; i < numLines; i++) {
            sc.nextInt();
            String velocity = sc.next();
            String mass = sc.next();

            int[] velocityArr = new int[velocity.length()];
            for (int j = 0; j < velocity.length(); j++) {
                velocityArr[j] = parseDigit(velocity.charAt(j));
            }

            int[] massArr = new int[mass.length()];
            for (int j = 0; j < mass.length(); j++) {
                massArr[j] = parseDigit(mass.charAt(j));
            }

            int[] result = multiply(velocityArr, massArr);
            String str = "";
            boolean leading = true;
            for (int j : result) {
                if (j != 0 || !leading) {
                    str += j;
                    leading = false;
                }
            }
            if (str.equals("")) { str = "0"; }
            pw.write(str);
            pw.write("\n");
        }
        pw.close();
    }

    public static int[] minus(int[] leftArr, int[] rightArr) {
        int j = leftArr.length - 1;
        for (int i = rightArr.length - 1; i >= 0; i--, j--) {
            int resultInt = leftArr[j] - rightArr[i];
            if (resultInt < 0) {
                leftArr[j - 1] -= 1;
                resultInt += 10;
            }
            leftArr[j] = resultInt;
        }
        return leftArr;
    }

    public static int[] add(int[] A, int[] B, int offset) {
        int lenA = A.length, lenB = B.length;
        int newLen = lenA > lenB + offset ? lenA + 1 : lenB + offset + 1;

        int[] result = new int[newLen];
        System.arraycopy(B, 0, result, (newLen - lenB - offset), lenB);

        int carry = 0;

        int i = lenA - 1;
        int j = newLen - 1;
        while (i >= 0) {
            carry += A[i] + result[j];
            result[j] = carry % 10;
            carry = carry >= 10 ? 1 : 0;
            i--;
            j--;
        }

        while (carry > 0) {
            carry += result[j];
            result[j] = carry % 10;
            carry = carry >= 10 ? 1 : 0;
            j--;
        }

        return result;
    }

    public static int[] multiply(int[] x, int[] y) {
        int xLen = x.length;
        int yLen = y.length;

        // base case
        if (xLen <= 16 || yLen <= 16) {
            return multiplyArr(x, y);
        }

        int n = xLen > yLen ? xLen / 2 + xLen % 2 : yLen / 2 + yLen % 2;

        int highXLen = n < xLen ? n : xLen;
        int highYLen = n < yLen ? n : yLen;

        int[] highX =
            highXLen == xLen ? new int[1] : Arrays.copyOf(x, xLen - highXLen);
        int[] lowX = Arrays.copyOfRange(x, xLen - highXLen, xLen);

        int[] highY =
            highYLen == yLen ? new int[1] : Arrays.copyOf(y, yLen - highYLen);
        int[] lowY = Arrays.copyOfRange(y, yLen - highYLen, yLen);

        int[] xCombi = add(lowX, highX, 0);
        int[] yCombi = add(lowY, highY, 0);

        int[] z0 = multiply(lowX, lowY);
        int[] z2 = multiply(highX, highY);
        int[] z1 = multiply(xCombi, yCombi);

        int[] zminus = minus(minus(z1, z2), z0);

        return add(add(z0, zminus, n), z2, 2 * n);
    }

    public static int[] multiplyArr(int[] A, int[] B) {
        int[] res = new int[A.length + B.length];

        /* Perform multiplication */
        for (int i = A.length - 1; i >= 0; i--) {
            for (int j = B.length - 1; j >= 0; j--) {
                res[j + i + 1] += A[i] * B[j];
                res[j + i] += res[j + i + 1] / 10;
                res[j + i + 1] %= 10;
            }
        }

        return res;
    }

    private static int parseDigit(char c) {
        if (c <= '9') {
            return c - '0';
        }
        return c - 'A' + 10;
    }
}
