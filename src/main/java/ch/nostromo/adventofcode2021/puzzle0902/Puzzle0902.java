package ch.nostromo.adventofcode2021.puzzle0902;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Next, you need to find the largest basins so you know what areas are most important to avoid.
 *
 * A basin is all locations that eventually flow downward to a single low point. Therefore, every low point has a basin, although some basins are very small. Locations of height 9 do not count as being in any basin, and all other locations will always be part of exactly one basin.
 *
 * The size of a basin is the number of locations within the basin, including the low point. The example above has four basins.
 *
 * The top-left basin, size 3:
 *
 * 2199943210
 * 3987894921
 * 9856789892
 * 8767896789
 * 9899965678
 * The top-right basin, size 9:
 *
 * 2199943210
 * 3987894921
 * 9856789892
 * 8767896789
 * 9899965678
 * The middle basin, size 14:
 *
 * 2199943210
 * 3987894921
 * 9856789892
 * 8767896789
 * 9899965678
 * The bottom-right basin, size 9:
 *
 * 2199943210
 * 3987894921
 * 9856789892
 * 8767896789
 * 9899965678
 * Find the three largest basins and multiply their sizes together. In the above example, this is 9 * 14 * 9 = 1134.
 *
 * What do you get if you multiply together the sizes of the three largest basins?
 *
 * Your puzzle answer was 1280496.
 */
public class Puzzle0902 {

    public static void main(String... args) throws URISyntaxException, IOException {
        List<String> input = Files.readAllLines(Paths.get(Puzzle0902.class.getClassLoader().getResource("0901/input.txt").toURI()), Charset.defaultCharset());

        int[][] numbers = new int[input.size()][input.get(0).length()];
        boolean[][] lowestNumbers = new boolean[input.size()][input.get(0).length()];


        // Fill board
        for (int y = 0; y < input.size(); y++) {
            char[] chars = input.get(y).toCharArray();
            for (int x = 0; x < chars.length; x++) {
                numbers[y][x] = Character.getNumericValue(chars[x]);
            }
        }


        // Check for lowest numbers
        for (int y = 0; y < numbers.length; y++) {
            for (int x = 0; x < numbers[y].length; x++) {
                if (isLowestValue(numbers, y, x)) {
                    lowestNumbers[y][x] = true;
                }
            }
        }

        // Find basin sizes
        List<Integer> basins = new ArrayList<>();
        for (int y = 0; y < numbers.length; y++) {
            for (int x = 0; x < numbers[y].length; x++) {
                if (lowestNumbers[y][x]) {
                    basins.add(getRecursiveBasin(numbers, new boolean[input.size()][input.get(0).length()], y, x, 0));
                }
            }
        }

        Collections.sort(basins, Collections.reverseOrder());

        System.out.println("Solution: " + basins.get(0) * basins.get(1) * basins.get(2));
    }


    private static int getRecursiveBasin(int[][] numbers, boolean[][] visitedPlaces, int y, int x, int currentSum) {
        if (y < 0 || y == numbers.length || x < 0 || x == numbers[y].length) {
            return currentSum;
        }

        if (numbers[y][x] == 9) {
            return currentSum;
        }

        if (visitedPlaces[y][x]) {
            return currentSum;
        }

        visitedPlaces[y][x] = true;
        currentSum += 1;

        currentSum = getRecursiveBasin(numbers, visitedPlaces, y - 1, x, currentSum);
        currentSum = getRecursiveBasin(numbers, visitedPlaces, y + 1, x, currentSum);
        currentSum = getRecursiveBasin(numbers, visitedPlaces, y, x - 1, currentSum);
        currentSum = getRecursiveBasin(numbers, visitedPlaces, y, x + 1, currentSum);


        return currentSum;
    }


    private static boolean isLowestValue(int[][] numbers, int y, int x) {
        boolean lowerThanNorth = false;
        boolean lowerThanSouth = false;
        boolean lowerThanWest = false;
        boolean lowerThanEast = false;

        if (y == 0 || numbers[y][x] < numbers[y - 1][x]) {
            lowerThanNorth = true;
        }

        if (y == numbers.length - 1 || numbers[y][x] < numbers[y + 1][x]) {
            lowerThanSouth = true;
        }

        if (x == 0 || numbers[y][x] < numbers[y][x - 1]) {
            lowerThanWest = true;
        }

        if (x == numbers[y].length - 1 || numbers[y][x] < numbers[y][x + 1]) {
            lowerThanEast = true;
        }

        return (lowerThanEast && lowerThanNorth && lowerThanWest && lowerThanSouth);
    }



}
