package ch.nostromo.adventofcode2021.puzzle0201;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * --- Day 2: Dive! ---
 * Now, you need to figure out how to pilot this thing.
 *
 * It seems like the submarine can take a series of commands like forward 1, down 2, or up 3:
 *
 * forward X increases the horizontal position by X units.
 * down X increases the depth by X units.
 * up X decreases the depth by X units.
 * Note that since you're on a submarine, down and up affect your depth, and so they have the opposite result of what you might expect.
 *
 * The submarine seems to already have a planned course (your puzzle input). You should probably figure out where it's going. For example:
 *
 * forward 5
 * down 5
 * forward 8
 * up 3
 * down 8
 * forward 2
 * Your horizontal position and depth both start at 0. The steps above would then modify them as follows:
 *
 * forward 5 adds 5 to your horizontal position, a total of 5.
 * down 5 adds 5 to your depth, resulting in a value of 5.
 * forward 8 adds 8 to your horizontal position, a total of 13.
 * up 3 decreases your depth by 3, resulting in a value of 2.
 * down 8 adds 8 to your depth, resulting in a value of 10.
 * forward 2 adds 2 to your horizontal position, a total of 15.
 * After following these instructions, you would have a horizontal position of 15 and a depth of 10. (Multiplying these together produces 150.)
 *
 * Calculate the horizontal position and depth you would have after following the planned course. What do you get if you multiply your final horizontal position by your final depth?
 *
 * Your puzzle answer was 2117664.
 *
 */
public class Puzzle0201 {

    public static void main(String... args) throws URISyntaxException, IOException {
        List<String> input = Files.readAllLines(Paths.get(Puzzle0201.class.getClassLoader().getResource("0201/input.txt").toURI()), Charset.defaultCharset());

        int currentHorizontal = 0;
        int currentDepth = 0;

        for (String line : input) {
            String[] tokens = line.split(" ");
            String direction = tokens[0];
            int change = Integer.parseInt(tokens[1]);

            switch (direction) {
                case "forward": {
                    currentHorizontal += change;
                    break;
                }
                case "up": {
                    currentDepth -= change;
                    break;
                }
                case "down": {
                    currentDepth += change;
                    break;
                }

            }


        }


        System.out.println("Solution: " + (currentHorizontal * currentDepth));

    }

}
