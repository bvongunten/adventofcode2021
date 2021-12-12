package ch.nostromo.adventofcode2021.puzzle1101;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * --- Day 11: Dumbo Octopus ---
 * You enter a large cavern full of rare bioluminescent dumbo octopuses! They seem to not like the Christmas lights on your submarine, so you turn them off for now.
 *
 * There are 100 octopuses arranged neatly in a 10 by 10 grid. Each octopus slowly gains energy over time and flashes brightly for a moment when its energy is full. Although your lights are off, maybe you could navigate through the cave without disturbing the octopuses if you could predict when the flashes of light will happen.
 *
 * Each octopus has an energy level - your submarine can remotely measure the energy level of each octopus (your puzzle input). For example:
 *
 * 5483143223
 * 2745854711
 * 5264556173
 * 6141336146
 * 6357385478
 * 4167524645
 * 2176841721
 * 6882881134
 * 4846848554
 * 5283751526
 * The energy level of each octopus is a value between 0 and 9. Here, the top-left octopus has an energy level of 5, the bottom-right one has an energy level of 6, and so on.
 *
 * You can model the energy levels and flashes of light in steps. During a single step, the following occurs:
 *
 * First, the energy level of each octopus increases by 1.
 * Then, any octopus with an energy level greater than 9 flashes. This increases the energy level of all adjacent octopuses by 1, including octopuses that are diagonally adjacent. If this causes an octopus to have an energy level greater than 9, it also flashes. This process continues as long as new octopuses keep having their energy level increased beyond 9. (An octopus can only flash at most once per step.)
 * Finally, any octopus that flashed during this step has its energy level set to 0, as it used all of its energy to flash.
 * Adjacent flashes can cause an octopus to flash on a step even if it begins that step with very little energy. Consider the middle octopus with 1 energy in this situation:
 *
 * Before any steps:
 * 11111
 * 19991
 * 19191
 * 19991
 * 11111
 *
 * After step 1:
 * 34543
 * 40004
 * 50005
 * 40004
 * 34543
 *
 * After step 2:
 * 45654
 * 51115
 * 61116
 * 51115
 * 45654
 * An octopus is highlighted when it flashed during the given step.
 *
 * After 100 steps, there have been a total of 1656 flashes.
 *
 * Given the starting energy levels of the dumbo octopuses in your cavern, simulate 100 steps. How many total flashes are there after 100 steps?
 *
 * Your puzzle answer was 1732.
 */
public class Puzzle1101 {

    public static void main(String... args) throws URISyntaxException, IOException {
        List<String> input = Files.readAllLines(Paths.get(Puzzle1101.class.getClassLoader().getResource("1101/input.txt").toURI()), Charset.defaultCharset());

        int[][] numbers = new int[input.size()][input.get(0).length()];

        // Fill board
        for (int y = 0; y < input.size(); y++) {
            char[] chars = input.get(y).toCharArray();
            for (int x = 0; x < chars.length; x++) {
                numbers[y][x] = Character.getNumericValue(chars[x]);
            }
        }

        int solution = 0;

        for (int i = 0; i < 100; i++) {

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
                            solution ++;
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
