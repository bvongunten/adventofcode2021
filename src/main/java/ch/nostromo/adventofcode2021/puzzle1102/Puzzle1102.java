package ch.nostromo.adventofcode2021.puzzle1102;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * It seems like the individual flashes aren't bright enough to navigate. However, you might have a better option: the flashes seem to be synchronizing!
 *
 * In the example above, the first time all octopuses flash simultaneously is step 195:
 *
 * After step 193:
 * 5877777777
 * 8877777777
 * 7777777777
 * 7777777777
 * 7777777777
 * 7777777777
 * 7777777777
 * 7777777777
 * 7777777777
 * 7777777777
 *
 * After step 194:
 * 6988888888
 * 9988888888
 * 8888888888
 * 8888888888
 * 8888888888
 * 8888888888
 * 8888888888
 * 8888888888
 * 8888888888
 * 8888888888
 *
 * After step 195:
 * 0000000000
 * 0000000000
 * 0000000000
 * 0000000000
 * 0000000000
 * 0000000000
 * 0000000000
 * 0000000000
 * 0000000000
 * 0000000000
 * If you can calculate the exact moments when the octopuses will all flash simultaneously, you should be able to navigate through the cavern. What is the first step during which all octopuses flash?
 *
 * Your puzzle answer was 290.
 */
public class Puzzle1102 {

    public static void main(String... args) throws URISyntaxException, IOException {
        List<String> input = Files.readAllLines(Paths.get(Puzzle1102.class.getClassLoader().getResource("1101/input.txt").toURI()), Charset.defaultCharset());

        int[][] numbers = new int[input.size()][input.get(0).length()];

        // Fill board
        for (int y = 0; y < input.size(); y++) {
            char[] chars = input.get(y).toCharArray();
            for (int x = 0; x < chars.length; x++) {
                numbers[y][x] = Character.getNumericValue(chars[x]);
            }
        }

        int solution = 0;

        boolean allFlashed = false;
        while(!allFlashed) {

            boolean[][] flashedAlready = new boolean[10][10];

            // Increase all by 1
            for (int y = 0; y < numbers.length; y++) {
                for (int x = 0; x < numbers[y].length; x++) {
                    increaseIfPossible(numbers, y, x);
                }
            }

            // Flash :)
            boolean keepFlashing = true;
            while (keepFlashing) {
                keepFlashing = false;
                for (int y = 0; y < numbers.length; y++) {
                    for (int x = 0; x < numbers[y].length; x++) {
                        if (numbers[y][x] > 9 && !flashedAlready[y][x]) {
                            flashMeAndNeighbours(numbers, y, x);
                            flashedAlready[y][x] = true;
                            keepFlashing = true;
                        }

                    }
                }
            }


            // Reset flashed to zero
            for (int y = 0; y < numbers.length; y++) {
                for (int x = 0; x < numbers[y].length; x++) {
                    if (numbers[y][x] > 9) {
                        numbers[y][x] = 0;
                    }
                }
            }

            // Check if all have flashed in this turn (equals 0)
            allFlashed = true;
            for (int y = 0; y < numbers.length; y++) {
                for (int x = 0; x < numbers[y].length; x++) {
                    if (numbers[y][x] != 0) {
                        allFlashed = false;
                    }
                }
            }

            solution ++;

         }


        System.out.println("Solution: " + solution);
    }

    private static void flashMeAndNeighbours(int[][] numbers, int y, int x) {
        increaseIfPossible(numbers, y, x);

        increaseIfPossible(numbers, y - 1, x);
        increaseIfPossible(numbers, y + 1, x);
        increaseIfPossible(numbers, y, x - 1);
        increaseIfPossible(numbers, y, x + 1);


        increaseIfPossible(numbers, y - 1, x + 1);
        increaseIfPossible(numbers, y + 1, x - 1);
        increaseIfPossible(numbers, y - 1, x - 1);
        increaseIfPossible(numbers, y + 1, x + 1);

    }

    private static void increaseIfPossible(int[][] numbers, int y, int x) {
        if (x < 0 || y < 0 || x > numbers.length - 1 || y > numbers.length - 1) {
            return;
        }

        numbers[y][x] = numbers[y][x] + 1;
    }

}
