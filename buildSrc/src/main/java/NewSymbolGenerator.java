import kotlin.ranges.IntRange;

import java.util.Arrays;
import java.util.List;
import java.util.function.IntPredicate;

import static java.lang.Character.*;

public class NewSymbolGenerator {

    private static StringBuilder firstOneByteBuffer = new StringBuilder();
    static private String firstOneByte = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    static public void setFirstOneByte(String firstOneByte) {
        NewSymbolGenerator.firstOneByte = firstOneByte;
        initFirstOneByte();
    }

    private static StringBuilder oneByteBuffer = new StringBuilder();
    static private String oneByte = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    static public void setOneByte(String oneByte) {
        NewSymbolGenerator.oneByte = oneByte;
        initOneByte();
    }

    private static StringBuilder twoByteBuffer = new StringBuilder();
    static private IntRange twoByteRange = new IntRange(0xa1, 0x7ff); // 161, 2046(0x7ff)

    static public void setTwoByteRange(IntRange twoByteRange) {
        NewSymbolGenerator.twoByteRange = twoByteRange;
        initTwoByte();
    }

    private static StringBuilder threeByteBuffer = new StringBuilder();
    static private IntRange threeByteRange = new IntRange(0x2030, 0xd7ff);

    static public void setThreeByteRange(IntRange threeByteRange) {
        NewSymbolGenerator.threeByteRange = threeByteRange;
        initThreeByte();
    }

    static IntPredicate intPredicate = i -> {
        if (Character.getDirectionality(i) == DIRECTIONALITY_NONSPACING_MARK) {
            return false;
        }
        if (Character.getDirectionality(i) == DIRECTIONALITY_RIGHT_TO_LEFT) {
            return false;
        }
        if (Character.getDirectionality(i) == DIRECTIONALITY_UNDEFINED) {
            return false;
        }
        if (Character.getDirectionality(i) == DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC) {
            return false;
        }
        if (Character.getDirectionality(i) == DIRECTIONALITY_ARABIC_NUMBER) {
            return false;
        }
        return true;
    };

    static void initFirstOneByte() {
        firstOneByteBuffer = new StringBuilder();
        firstOneByte.chars()
                .filter(intPredicate)
                .forEach(i -> firstOneByteBuffer.append(Character.toChars(i)));
        refreshCountTotal();
    }

    static void initOneByte() {
        oneByteBuffer = new StringBuilder();
        oneByte.chars()
                .filter(intPredicate)
                .forEach(i -> oneByteBuffer.append(Character.toChars(i)));
        refreshCountTotal();
    }

    static void initTwoByte() {
        twoByteBuffer = new StringBuilder();
        twoByteRange.forEach(integer -> {
            if (intPredicate.test(integer)) {
                twoByteBuffer.append(Character.toChars(integer));
            }
        });
        refreshCountTotal();
    }

    static void initThreeByte() {
        threeByteBuffer = new StringBuilder();
        threeByteRange.forEach(integer -> {
            if (intPredicate.test(integer)) {
                threeByteBuffer.append(Character.toChars(integer));
            }
        });
        refreshCountTotal();
    }

    public NewSymbolGenerator() {
        initFirstOneByte();
        initOneByte();
        initTwoByte();
        initThreeByte();

        refreshCountTotal();
        System.out.println("All possible classes nubmer " + countTotal);
        System.out.println("f = " + firstOneByte.length());
        System.out.println("f1 = " + firstOneByte.length() * oneByteBuffer.length());
        System.out.println("2 = " + twoByteBuffer.length());
        System.out.println("f11 = " + firstOneByte.length() * oneByteBuffer.length() * oneByteBuffer.length());
        System.out.println("f2 = " + firstOneByte.length() * twoByteBuffer.length());
        System.out.println("21 = " + twoByteBuffer.length() * oneByteBuffer.length());
        System.out.println("3 = " + threeByteBuffer.length());
        System.out.println("22 = " + twoByteBuffer.length() * twoByteBuffer.length());
        System.out.println("211 = " + twoByteBuffer.length() * oneByteBuffer.length() * oneByteBuffer.length());
    }

    static int countTotal = 0;

    static private void refreshCountTotal() {
        countTotal = firstOneByte.length()
                + firstOneByte.length() * oneByteBuffer.length()
                + twoByteBuffer.length()
                + firstOneByte.length() * oneByteBuffer.length() * oneByteBuffer.length()
                + firstOneByte.length() * twoByteBuffer.length()
                + twoByteBuffer.length() * oneByteBuffer.length()
                + threeByteBuffer.length()
                + twoByteBuffer.length() * twoByteBuffer.length()
                + twoByteBuffer.length() * oneByteBuffer.length() * oneByteBuffer.length();
    }

    public String resolve(int numberCount) {
        return resolve(numberCount, firstOneByte, oneByte, twoByteBuffer.toString(), threeByteBuffer.toString());
    }

    private String resolve(int numberCount, String firstOneByteSymbolMass, String oneByteSymbolMass, String twoByteSymbolMass, String threeByteSymbolMass) {
        int value = numberCount;

        // oneByte
        int oneByte = firstOneByteSymbolMass.length();
        if (value < oneByte) {
            return firstOneByteSymbolMass.charAt(value) + "";
        }
        value -= oneByte;
        assert value >= 0;

        // twoByte
        int twoByteFirstOne = firstOneByteSymbolMass.length() * oneByteSymbolMass.length();
        if (value < twoByteFirstOne) {
            List<Character> characters = two(value, firstOneByteSymbolMass, oneByteSymbolMass);
            return new String(new char[]{characters.get(0), characters.get(1)});
        }
        value -= twoByteFirstOne;
        assert value >= 0;

        int twoByteTwo = twoByteSymbolMass.length();
        if (value < twoByteTwo) {
            return twoByteSymbolMass.charAt(value) + "";
        }
        value -= twoByteTwo;
        assert value >= 0;

        // three byte
        int threeFirstOneOne = firstOneByteSymbolMass.length() * oneByteSymbolMass.length() * oneByteSymbolMass.length();
        if (value < threeFirstOneOne) {
            List<Character> characters = three(value, firstOneByteSymbolMass, oneByteSymbolMass, oneByteSymbolMass);
            return new String(new char[]{characters.get(0), characters.get(1), characters.get(2)});
        }
        value -= threeFirstOneOne;
        assert value >= 0;

        int threeByteFirstTwo = firstOneByteSymbolMass.length() * twoByteSymbolMass.length();
        if (value < threeByteFirstTwo) {
            List<Character> characters = two(value, firstOneByteSymbolMass, twoByteSymbolMass);
            return new String(new char[]{characters.get(0), characters.get(1)});
        }
        value -= threeByteFirstTwo;
        assert value >= 0;

        int threeByteTwoOne = twoByteSymbolMass.length() * oneByteSymbolMass.length();
        if (value < threeByteTwoOne) {
            List<Character> characters = two(value, twoByteSymbolMass, oneByteSymbolMass);
            return new String(new char[]{characters.get(0), characters.get(1)});
        }
        value -= threeByteTwoOne;
        assert value >= 0;

        int threeByteThree = threeByteSymbolMass.length();
        if (value < threeByteThree) {
            return threeByteSymbolMass.charAt(value) + "";
        }
        value -= threeByteThree;
        assert value >= 0;

        // four byte
        int fourTwoTwo = twoByteSymbolMass.length() * twoByteSymbolMass.length();
        if (value < fourTwoTwo) {
            List<Character> characters = two(value, twoByteSymbolMass, twoByteSymbolMass);
            return new String(new char[]{characters.get(0), characters.get(1)});
        }
        value -= fourTwoTwo;
        assert value >= 0;

        int fourTwoOneOne = twoByteSymbolMass.length() * oneByteSymbolMass.length() * oneByteSymbolMass.length();
        if (value < fourTwoOneOne) {
            List<Character> characters = three(value, twoByteSymbolMass, oneByteSymbolMass, oneByteSymbolMass);
            return new String(new char[]{characters.get(0), characters.get(1), characters.get(2)});
        }
        value -= fourTwoOneOne;
        assert value >= 0;

        throw new RuntimeException("I was too lazy to write any more");
    }

    private List<Character> three(int value, String firstSymbolMass, String secondSymbolMass, String thirdSymbolMass) {
        int tmp = value;
        int firstCountSize = secondSymbolMass.length() * thirdSymbolMass.length();
        int first = tmp / firstCountSize;
        tmp -= first * firstCountSize;
        List<Character> twoOneByte = two(tmp, secondSymbolMass, thirdSymbolMass);
        return Arrays.asList(firstSymbolMass.charAt(first), twoOneByte.get(0), twoOneByte.get(1));
    }

    private List<Character> two(int value, String firstSymbolMass, String secondSymbolMass) {
        int first = value / secondSymbolMass.length();
        int second = value - first * secondSymbolMass.length();
        return Arrays.asList(firstSymbolMass.charAt(first), secondSymbolMass.charAt(second));
    }
}