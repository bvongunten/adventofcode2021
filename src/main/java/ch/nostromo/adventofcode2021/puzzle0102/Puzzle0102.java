package ch.nostromo.adventofcode2021.puzzle0102;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 *--- Part Two ---
 * Considering every single measurement isn't as useful as you expected: there's just too much noise in the data.
 *
 * Instead, consider sums of a three-measurement sliding window. Again considering the above example:
 *
 * 199  A
 * 200  A B
 * 208  A B C
 * 210    B C D
 * 200  E   C D
 * 207  E F   D
 * 240  E F G
 * 269    F G H
 * 260      G H
 * 263        H
 * Start by comparing the first and second three-measurement windows. The measurements in the first window are marked A (199, 200, 208); their sum is 199 + 200 + 208 = 607. The second window is marked B (200, 208, 210); its sum is 618. The sum of measurements in the second window is larger than the sum of the first, so this first comparison increased.
 *
 * Your goal now is to count the number of times the sum of measurements in this sliding window increases from the previous sum. So, compare A with B, then compare B with C, then C with D, and so on. Stop when there aren't enough measurements left to create a new three-measurement sum.
 *
 * In the above example, the sum of each three-measurement window is as follows:
 *
 * A: 607 (N/A - no previous sum)
 * B: 618 (increased)
 * C: 618 (no change)
 * D: 617 (decreased)
 * E: 647 (increased)
 * F: 716 (increased)
 * G: 769 (increased)
 * H: 792 (increased)
 * In this example, there are 5 sums that are larger than the previous sum.
 *
 * Consider sums of a three-measurement sliding window. How many sums are larger than the previous sum?
 *
 * Your puzzle answer was 1822.
 */
public class Puzzle0102 {

    public static void main(String...args) throws URISyntaxException, IOException {
        List<String> input = Files.readAllLines(Paths.get(Puzzle0102.class.getClassLoader().getResource("0101/input.txt").toURI()), Charset.defaultCharset());

        int solutionCounter = 0;

        int lastVal = 0;

        int windowValue1 = 0;
        int windowValue2 = 0;
        int windowValue3 = 0;

        int lineCounter = 0;

        for (String line : input) {

            int lineVal = Integer.parseInt(line);

            if (lineCounter == 0) {
                windowValue1 = lineVal;
            } else if (lineCounter == 1) {
                windowValue2 = lineVal;
            } else if (lineCounter == 2) {
                windowValue3 = lineVal;
                lastVal = windowValue1 + windowValue2 + windowValue3;
            } else {
                // Shift values
                windowValue1 = windowValue2;
                windowValue2 = windowValue3;
                windowValue3 = lineVal;

                int currentWindowSum = windowValue1 + windowValue2 + windowValue3;

                if (currentWindowSum > lastVal) {
                    solutionCounter ++;
                }

                lastVal = currentWindowSum;

            }

            lineCounter ++;
        }

        System.out.println("Solution: " + solutionCounter);

    }

}
