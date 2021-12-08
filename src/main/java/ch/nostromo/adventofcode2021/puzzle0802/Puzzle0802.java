package ch.nostromo.adventofcode2021.puzzle0802;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Through a little deduction, you should now be able to determine the remaining digits. Consider again the first example above:
 * <p>
 * acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab |
 * cdfeb fcadb cdfeb cdbaf
 * After some careful analysis, the mapping between signal wires and segments only make sense in the following configuration:
 * <p>
 * dddd
 * e    a
 * e    a
 * ffff
 * g    b
 * g    b
 * cccc
 * So, the unique signal patterns would correspond to the following digits:
 * <p>
 * acedgfb: 8
 * cdfbe: 5
 * gcdfa: 2
 * fbcad: 3
 * dab: 7
 * cefabd: 9
 * cdfgeb: 6
 * eafb: 4
 * cagedb: 0
 * ab: 1
 * Then, the four digits of the output value can be decoded:
 * <p>
 * cdfeb: 5
 * fcadb: 3
 * cdfeb: 5
 * cdbaf: 3
 * Therefore, the output value for this entry is 5353.
 * <p>
 * Following this same process for each entry in the second, larger example above, the output value of each entry can be determined:
 * <p>
 * fdgacbe cefdb cefbgd gcbe: 8394
 * fcgedb cgb dgebacf gc: 9781
 * cg cg fdcagb cbg: 1197
 * efabcd cedba gadfec cb: 9361
 * gecf egdcabf bgf bfgea: 4873
 * gebdcfa ecba ca fadegcb: 8418
 * cefg dcbef fcge gbcadfe: 4548
 * ed bcgafe cdgba cbgef: 1625
 * gbdfcae bgc cg cgb: 8717
 * fgae cfgab fg bagce: 4315
 * Adding all of the output values in this larger example produces 61229.
 * <p>
 * For each entry, determine all of the wire/segment connections and decode the four-digit output values. What do you get if you add up all of the output values?
 * <p>
 * Your puzzle answer was 1027483.
 */
public class Puzzle0802 {

    public static void main(String... args) throws URISyntaxException, IOException {
        List<String> input = Files.readAllLines(Paths.get(Puzzle0802.class.getClassLoader().getResource("0801/input.txt").toURI()), Charset.defaultCharset());

        int result = 0;

        for (String line : input) {
            String firstPart = line.split("\\|")[0];
            String secondPart = line.split("\\|")[1];

            String[] firstPartTokens = firstPart.split(" ");
            String[] secondPArtTokens = secondPart.split(" ");

            // Known numbers by len
            String numberString1 = findNumberByLength(firstPartTokens, 2);
            String numberString4 = findNumberByLength(firstPartTokens, 4);
            String numberString7 = findNumberByLength(firstPartTokens, 3);
            String numberString8 = findNumberByLength(firstPartTokens, 7);

            // Find numbers by knowing number 1
            String numberString3 = findNumberByFilter(firstPartTokens, 5, numberString1, true);
            String numberString6 = findNumberByFilter(firstPartTokens, 6, numberString1, false);

            // Find some leds to help further search
            String ledTopRight = checkStringForOccuringChar(numberString6, numberString1, false);
            String ledBottomRight = checkStringForOccuringChar(numberString6, numberString1, true);
            String ledBottomLeft = checkStringForOccuringChar(numberString4 + numberString3, numberString8, false);

            // Find numbers by knowing leds
            String numberString0 = findNumberByFilter(firstPartTokens, 6, ledBottomLeft + ledTopRight, true);
            String numberString2 = findNumberByFilter(firstPartTokens, 5, ledBottomRight, false);
            String numberString5 = findNumberByFilter(firstPartTokens, 5, ledTopRight, false);
            String numberString9 = findNumberByFilter(firstPartTokens, 6, ledBottomLeft, false);

            Map<String, String> numberMap = new HashMap<>();
            numberMap.put(numberString0, "0");
            numberMap.put(numberString1, "1");
            numberMap.put(numberString2, "2");
            numberMap.put(numberString3, "3");
            numberMap.put(numberString4, "4");
            numberMap.put(numberString5, "5");
            numberMap.put(numberString6, "6");
            numberMap.put(numberString7, "7");
            numberMap.put(numberString8, "8");
            numberMap.put(numberString9, "9");

            String partialResultString = "";
            for (String token : secondPArtTokens) {
                if (!token.isEmpty()) {
                    for (String anagram : createAnagrams(token)) {
                        if (numberMap.containsKey(anagram)) {
                            partialResultString += numberMap.get(anagram);
                        }
                    }
                }
            }

            result += Integer.parseInt(partialResultString);

        }


        System.out.println("Solution " + result);
    }


    private static String findNumberByLength(String[] tokens, int length) {
        for (String token : tokens) {
            if (token.length() == length) {
                return token;
            }
        }
        throw new IllegalArgumentException("length not found: " + length);
    }

    private static String findNumberByFilter(String[] tokens, int len, String toFind, boolean mustContain) {
        for (String token : tokens) {
            if (token.length() == len && checkStringForOccurences(token, toFind, mustContain)) {
                return token;
            }
        }
        throw new IllegalArgumentException("No number found with len:" + len + " to find: " + toFind + " must contain: " + mustContain);
    }

    private static boolean checkStringForOccurences(String longer, String smaller, boolean mustContain) {
        for (int i = 0; i < smaller.length(); i++) {
            if (!longer.contains(smaller.substring(i, i + 1))) {
                return !mustContain;
            }
        }
        return mustContain;
    }


    private static String checkStringForOccuringChar(String full, String candidates, boolean mustContain) {
        for (int i = 0; i < candidates.length(); i++) {
            String toFind = candidates.substring(i, i + 1);
            boolean found = full.contains(toFind);
            if ((mustContain && found) || (!mustContain && !found)) {
                return toFind;
            }
        }
        throw new IllegalArgumentException("No char found in string: " + full + " filter: " + candidates + " must contain: " + mustContain);
    }


    private static List<String> createAnagrams(String input) {
        List<String> anagrams = new ArrayList<>();
        recursiveAnagram(anagrams, "", input);

        return anagrams;
    }

    private static void recursiveAnagram(List<String> result, String candidate, String remaining) {
        if (remaining.length() == 0) {
            result.add(candidate);
        }

        for (int i = 0; i < remaining.length(); i++) {
            String newCandidate = candidate + remaining.charAt(i);
            String newRemaining = remaining.substring(0, i) + remaining.substring(i + 1);
            recursiveAnagram(result, newCandidate, newRemaining);
        }
    }
    
}
