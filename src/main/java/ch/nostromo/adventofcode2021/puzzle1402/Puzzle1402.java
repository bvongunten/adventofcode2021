package ch.nostromo.adventofcode2021.puzzle1402;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The resulting polymer isn't nearly strong enough to reinforce the submarine. You'll need to run more steps of the pair insertion process; a total of 40 steps should do it.
 * <p>
 * In the above example, the most common element is B (occurring 2192039569602 times) and the least common element is H (occurring 3849876073 times); subtracting these produces 2188189693529.
 * <p>
 * Apply 40 steps of pair insertion to the polymer template and find the most and least common elements in the result. What do you get if you take the quantity of the most common element and subtract the quantity of the least common element?
 * <p>
 * Your puzzle answer was 1976896901756.
 */
public class Puzzle1402 {

    public static void main(String... args) throws URISyntaxException, IOException {
        List<String> input = Files.readAllLines(Paths.get(Puzzle1402.class.getClassLoader().getResource("1401/input.txt").toURI()), Charset.defaultCharset());

        String template = input.get(0);

        Map<String, String> instructions = new HashMap<>();

        for (int i = 2; i < input.size(); i++) {
            String line = input.get(i);
            instructions.put(line.substring(0, 2), line.substring(6));
        }

        Map<String, Long> knownCharacterPairs = new HashMap<>();
        for (int i = 0; i < template.length() - 1; i++) {
            knownCharacterPairs.merge(template.substring(i, i + 2), 1L, Long::sum);
        }


        for (int i = 0; i < 40; i++) {
            Map<String, Long> newKnownCharacterPairs = new HashMap<>();

            for (Map.Entry<String, Long> entry : knownCharacterPairs.entrySet()) {
                String charPair = entry.getKey();
                Long count = entry.getValue();
                if (instructions.containsKey(charPair)) {
                    String toInsert = instructions.get(charPair);
                    newKnownCharacterPairs.merge(charPair.substring(0, 1) + toInsert, count, Long::sum);
                    newKnownCharacterPairs.merge(toInsert + charPair.substring(1), count, Long::sum);
                }
            }

            knownCharacterPairs = newKnownCharacterPairs;
        }

        Map<String, Long> characterCount = new HashMap<>();

        // Add last char at least once, as it never had the chance to be at first pos :)
        characterCount.put(template.substring(template.length() - 1), 1L);

        for (Map.Entry<String, Long> entry : knownCharacterPairs.entrySet()) {
            String character = entry.getKey().substring(0, 1);
            characterCount.put(character, characterCount.getOrDefault(character, 0L) + entry.getValue());
        }

        long mostKnown = Long.MIN_VALUE;
        long leastKnown = Long.MAX_VALUE;

        for (Map.Entry<String, Long> entry : characterCount.entrySet()) {
            if (entry.getValue() > mostKnown) {
                mostKnown = entry.getValue();
            }

            if (entry.getValue() < leastKnown) {
                leastKnown = entry.getValue();
            }
        }

        System.out.println("Solution: " + (mostKnown - leastKnown));

    }

}
