package ch.nostromo.adventofcode2021.puzzle0501;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * --- Day 5: Hydrothermal Venture ---
 * You come across a field of hydrothermal vents on the ocean floor! These vents constantly produce large, opaque clouds, so it would be best to avoid them if possible.
 *
 * They tend to form in lines; the submarine helpfully produces a list of nearby lines of vents (your puzzle input) for you to review. For example:
 *
 * 0,9 -> 5,9
 * 8,0 -> 0,8
 * 9,4 -> 3,4
 * 2,2 -> 2,1
 * 7,0 -> 7,4
 * 6,4 -> 2,0
 * 0,9 -> 2,9
 * 3,4 -> 1,4
 * 0,0 -> 8,8
 * 5,5 -> 8,2
 * Each line of vents is given as a line segment in the format x1,y1 -> x2,y2 where x1,y1 are the coordinates of one end the line segment and x2,y2 are the coordinates of the other end. These line segments include the points at both ends. In other words:
 *
 * An entry like 1,1 -> 1,3 covers points 1,1, 1,2, and 1,3.
 * An entry like 9,7 -> 7,7 covers points 9,7, 8,7, and 7,7.
 * For now, only consider horizontal and vertical lines: lines where either x1 = x2 or y1 = y2.
 *
 * So, the horizontal and vertical lines from the above list would produce the following diagram:
 *
 * .......1..
 * ..1....1..
 * ..1....1..
 * .......1..
 * .112111211
 * ..........
 * ..........
 * ..........
 * ..........
 * 222111....
 * In this diagram, the top left corner is 0,0 and the bottom right corner is 9,9. Each position is shown as the number of lines which cover that point or . if no line covers that point. The top-left pair of 1s, for example, comes from 2,2 -> 2,1; the very bottom row is formed by the overlapping lines 0,9 -> 5,9 and 0,9 -> 2,9.
 *
 * To avoid the most dangerous areas, you need to determine the number of points where at least two lines overlap. In the above example, this is anywhere in the diagram with a 2 or larger - a total of 5 points.
 *
 * Consider only horizontal and vertical lines. At how many points do at least two lines overlap?
 *
 * Your puzzle answer was 8111.
 */
public class Puzzle0501 {

    public static void main(String... args) throws URISyntaxException, IOException {
        List<String> input = Files.readAllLines(Paths.get(Puzzle0501.class.getClassLoader().getResource("0501/input.txt").toURI()), Charset.defaultCharset());

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
            y1= Integer.parseInt(coord1.trim().split(",")[1]);

            x2 = Integer.parseInt(coord2.trim().split(",")[0]);
            y2= Integer.parseInt(coord2.trim().split(",")[1]);
        }

        private boolean isHorizontal() {
            return x1 == x2;
        }

        public void drawOntoMap(int[][] map) {
            if (y1 == y2) {
                int drawx1 = x1;
                int drawx2 = x2;
                if (x1 > x2) {
                    drawx1 = x2;
                    drawx2 = x1;
                }
                for (int i = drawx1; i <=drawx2; i++) {
                    map[y1][i]= map[y1][i]+ 1;
                }
            } else if (x1 == x2){
                int drawy1 = y1;
                int drawy2 = y2;
                if (y1 > y2) {
                    drawy1 = y2;
                    drawy2 = y1;
                }
                for (int i = drawy1; i <=drawy2; i++) {
                    map[i][x1]= map[i][x2]+ 1;
                }
            }

        }

    }



}
