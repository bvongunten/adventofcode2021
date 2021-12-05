package ch.nostromo.adventofcode2021.puzzle0502;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * --- Part Two ---
 * Unfortunately, considering only horizontal and vertical lines doesn't give you the full picture; you need to also consider diagonal lines.
 * <p>
 * Because of the limits of the hydrothermal vent mapping system, the lines in your list will only ever be horizontal, vertical, or a diagonal line at exactly 45 degrees. In other words:
 * <p>
 * An entry like 1,1 -> 3,3 covers points 1,1, 2,2, and 3,3.
 * An entry like 9,7 -> 7,9 covers points 9,7, 8,8, and 7,9.
 * Considering all lines from the above example would now produce the following diagram:
 * <p>
 * 1.1....11.
 * .111...2..
 * ..2.1.111.
 * ...1.2.2..
 * .112313211
 * ...1.2....
 * ..1...1...
 * .1.....1..
 * 1.......1.
 * 222111....
 * You still need to determine the number of points where at least two lines overlap. In the above example, this is still anywhere in the diagram with a 2 or larger - now a total of 12 points.
 * <p>
 * Consider all of the lines. At how many points do at least two lines overlap?
 * <p>
 * Your puzzle answer was 22088.
 */
public class Puzzle0502 {

    public static void main(String... args) throws URISyntaxException, IOException {
        List<String> input = Files.readAllLines(Paths.get(Puzzle0502.class.getClassLoader().getResource("0501/input.txt").toURI()), Charset.defaultCharset());

        // Get and remove numbers
        ArrayList<Line> lines = new ArrayList<>();

        input.forEach((l) -> lines.add(new Line(l)));

        int[][] map = new int[1000][1000];
        lines.forEach((l) -> l.drawOntoMap(map));

        int result = 0;

        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map.length; y++) {
                if (map[x][y] > 1) {
                    result++;
                }
            }
        }

        System.out.println("Solution: " + result);

    }

    public static class Line {
        int x1;
        int y1;

        int x2;
        int y2;

        public Line(String line) {
            String coord1 = line.split("->")[0];
            String coord2 = line.split("->")[1];

            x1 = Integer.parseInt(coord1.trim().split(",")[0]);
            y1 = Integer.parseInt(coord1.trim().split(",")[1]);

            x2 = Integer.parseInt(coord2.trim().split(",")[0]);
            y2 = Integer.parseInt(coord2.trim().split(",")[1]);
        }

        public void drawOntoMap(int[][] map) {
            int xInc = 0;
            if (x1 > x2) {
                xInc = -1;
            } else if (x2 > x1) {
                xInc = 1;
            }

            int yInc = 0;
            if (y1 > y2) {
                yInc = -1;
            } else if (y2 > y1) {
                yInc = 1;
            }

            int lineDistance = Math.max(Math.max(x1, x2) - Math.min(x1, x2), Math.max(y1, y2) - Math.min(y1, y2));

            int curX = x1;
            int curY = y1;

            for (int i = 0; i <= lineDistance; i++) {
                map[curY][curX] = map[curY][curX] + 1;
                curX = curX + xInc;
                curY = curY + yInc;
            }

        }


    }


}
