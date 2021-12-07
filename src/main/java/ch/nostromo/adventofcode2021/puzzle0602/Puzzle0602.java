package ch.nostromo.adventofcode2021.puzzle0602;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Suppose the lanternfish live forever and have unlimited food and space. Would they take over the entire ocean?
 *
 * After 256 days in the example above, there would be a total of 26984457539 lanternfish!
 *
 * How many lanternfish would there be after 256 days?
 *
 * Your puzzle answer was 1629570219571.
 */
public class Puzzle0602 {

    public static void main(String... args) throws URISyntaxException, IOException {
        List<String> input = Files.readAllLines(Paths.get(Puzzle0602.class.getClassLoader().getResource("0601/input.txt").toURI()), Charset.defaultCharset());

        // Get and remove numbers
        List<Integer> numbers = Stream.of(input.get(0).split(",")).map(Integer::parseInt).collect(Collectors.toList());


        BigInteger[] fishPool = new BigInteger[10];
        for (int i = 0; i < 10; i++) {
            fishPool[i] = new BigInteger("0");
        }

        numbers.forEach((timer) -> fishPool[timer +1] = fishPool[timer + 1].add(new BigInteger("1")));

        for (int i = 0; i < 257; i++) {
            // reduze timers
            for (int x = 0; x < 9; x++) {
                fishPool[x] = fishPool[x + 1];
            }
            fishPool[9] = new BigInteger("0");

            // add zero timers to 6
            fishPool[7] = fishPool[7].add(fishPool[0]);
            fishPool[9] = fishPool[0];
            fishPool[0] = new BigInteger("0");
        }

        BigInteger sum = new BigInteger("0");
        for (int i = 0; i < 9; i ++) {
          sum = sum.add(fishPool[i]);
        }

        System.out.println("Solution " + sum);
    }

}
