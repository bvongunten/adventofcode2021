package ch.nostromo.adventofcode2021.puzzle1302;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * Finish folding the transparent paper according to the instructions. The manual says the code is always eight capital letters.
 *
 * What code do you use to activate the infrared thermal imaging camera system?
 *
 * Your puzzle answer was EPLGRULR.
 */
public class Puzzle1302 {

    public static void main(String... args) throws URISyntaxException, IOException {
        List<String> input = Files.readAllLines(Paths.get(Puzzle1302.class.getClassLoader().getResource("1301/input.txt").toURI()), Charset.defaultCharset());

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

        for (Tuple command : commands) {
            sheet = foldSheet(sheet, (String) command.getX(), (int) command.getY());;
        }

        for (int y = 0; y < sheet.length; y++) {
            for (int x = 0; x < sheet[y].length; x++) {
                System.out.print(sheet[y][x] > 0 ? "#" : " ");
            }
            System.out.print("\n");
        }

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
