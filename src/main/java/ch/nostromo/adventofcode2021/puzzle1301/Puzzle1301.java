package ch.nostromo.adventofcode2021.puzzle1301;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


/**
 * --- Day 13: Transparent Origami ---
 * You reach another volcanically active part of the cave. It would be nice if you could do some kind of thermal imaging so you could tell ahead of time which caves are too hot to safely enter.
 *
 * Fortunately, the submarine seems to be equipped with a thermal camera! When you activate it, you are greeted with:
 *
 * Congratulations on your purchase! To activate this infrared thermal imaging
 * camera system, please enter the code found on page 1 of the manual.
 * Apparently, the Elves have never used this feature. To your surprise, you manage to find the manual; as you go to open it, page 1 falls out. It's a large sheet of transparent paper! The transparent paper is marked with random dots and includes instructions on how to fold it up (your puzzle input). For example:
 *
 * 6,10
 * 0,14
 * 9,10
 * 0,3
 * 10,4
 * 4,11
 * 6,0
 * 6,12
 * 4,1
 * 0,13
 * 10,12
 * 3,4
 * 3,0
 * 8,4
 * 1,10
 * 2,14
 * 8,10
 * 9,0
 *
 * fold along y=7
 * fold along x=5
 * The first section is a list of dots on the transparent paper. 0,0 represents the top-left coordinate. The first value, x, increases to the right. The second value, y, increases downward. So, the coordinate 3,0 is to the right of 0,0, and the coordinate 0,7 is below 0,0. The coordinates in this example form the following pattern, where # is a dot on the paper and . is an empty, unmarked position:
 *
 * ...#..#..#.
 * ....#......
 * ...........
 * #..........
 * ...#....#.#
 * ...........
 * ...........
 * ...........
 * ...........
 * ...........
 * .#....#.##.
 * ....#......
 * ......#...#
 * #..........
 * #.#........
 * Then, there is a list of fold instructions. Each instruction indicates a line on the transparent paper and wants you to fold the paper up (for horizontal y=... lines) or left (for vertical x=... lines). In this example, the first fold instruction is fold along y=7, which designates the line formed by all of the positions where y is 7 (marked here with -):
 *
 * ...#..#..#.
 * ....#......
 * ...........
 * #..........
 * ...#....#.#
 * ...........
 * ...........
 * -----------
 * ...........
 * ...........
 * .#....#.##.
 * ....#......
 * ......#...#
 * #..........
 * #.#........
 * Because this is a horizontal line, fold the bottom half up. Some of the dots might end up overlapping after the fold is complete, but dots will never appear exactly on a fold line. The result of doing this fold looks like this:
 *
 * #.##..#..#.
 * #...#......
 * ......#...#
 * #...#......
 * .#.#..#.###
 * ...........
 * ...........
 * Now, only 17 dots are visible.
 *
 * Notice, for example, the two dots in the bottom left corner before the transparent paper is folded; after the fold is complete, those dots appear in the top left corner (at 0,0 and 0,1). Because the paper is transparent, the dot just below them in the result (at 0,3) remains visible, as it can be seen through the transparent paper.
 *
 * Also notice that some dots can end up overlapping; in this case, the dots merge together and become a single dot.
 *
 * The second fold instruction is fold along x=5, which indicates this line:
 *
 * #.##.|#..#.
 * #...#|.....
 * .....|#...#
 * #...#|.....
 * .#.#.|#.###
 * .....|.....
 * .....|.....
 * Because this is a vertical line, fold left:
 *
 * #####
 * #...#
 * #...#
 * #...#
 * #####
 * .....
 * .....
 * The instructions made a square!
 *
 * The transparent paper is pretty big, so for now, focus on just completing the first fold. After the first fold in the example above, 17 dots are visible - dots that end up overlapping after the fold is completed count as a single dot.
 *
 * How many dots are visible after completing just the first fold instruction on your transparent paper?
 *
 * Your puzzle answer was 710.
 */
public class Puzzle1301 {

    public static void main(String... args) throws URISyntaxException, IOException {
        List<String> input = Files.readAllLines(Paths.get(Puzzle1301.class.getClassLoader().getResource("1301/input.txt").toURI()), Charset.defaultCharset());

        List<Tuple<Integer, Integer>> coordinates = new ArrayList<>();
        List<Tuple<String, Integer>> commands = new ArrayList<>();

        for (String line : input) {
            if (line.startsWith("fold along ")) {
                commands.add(new Tuple(line.substring(11, 12), Integer.parseInt(line.substring(13))));
            } else if (!line.isEmpty()) {
                coordinates.add(new Tuple(Integer.parseInt(line.split(",")[0]), Integer.parseInt(line.split(",")[1])));
            }
        }

        int maxX = coordinates.stream().max(Comparator.comparing(Tuple::getX)).get().getX();
        int maxY = coordinates.stream().max(Comparator.comparing(Tuple::getY)).get().getY();

        int[][] sheet = new int[maxY + 1][maxX + 1];
        for (Tuple tuple : coordinates) {
            sheet[(int) tuple.y][(int) tuple.x] = 1;
        }

        sheet = foldSheet(sheet, commands.get(0).x, commands.get(0).y);;

        int solution = 0;
        for (int y = 0; y < sheet.length; y++) {
            for (int x = 0; x < sheet[y].length; x++) {
                solution += sheet[y][x] > 0 ? 1 : 0;
            }
        }

        System.out.println("Solution: " + solution);
    }

    private static int[][] foldSheet(int[][] sheet, String orientation, int size) {
        int[][] newSheet = null;

        if (orientation.equals("y")) {
            int length = size;
            int width = sheet[0].length;
            newSheet = createSubSheet(sheet, length, width);

            for (int i = sheet.length - 1; i > length; i--) {
                for (int n = 0; n < sheet[0].length; n++) {
                    newSheet[sheet.length - 1 - i][n] += sheet[i][n];
                }
            }
        } else if (orientation.equals("x")) {
            int length = sheet.length;
            int width = size;
            newSheet = createSubSheet(sheet, length, width);

            for (int i = 0; i < newSheet.length; i++) {
                for (int n = sheet[0].length - 1; n > width; n--) {
                    newSheet[i][sheet[0].length - 1 - n] += sheet[i][n];
                }
            }
        }

        return newSheet;

    }

    private static int[][] createSubSheet(int[][] sheet, int length, int width) {
        int[][] result = new int[length][width];

        for (int i = 0; i < length; i++) {
            for (int n = 0; n < width; n++) {
                result[i][n] = sheet[i][n];
            }
        }

        return result;
    }

    private static class Tuple<X, Y> {
        public final X x;
        public final Y y;

        public Tuple(X x, Y y) {
            this.x = x;
            this.y = y;
        }

        public X getX() {
            return x;
        }

        public Y getY() {
            return y;
        }

    }


}
