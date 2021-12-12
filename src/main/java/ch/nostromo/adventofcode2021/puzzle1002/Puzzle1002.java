package ch.nostromo.adventofcode2021.puzzle1002;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Now, discard the corrupted lines. The remaining lines are incomplete.
 *
 * Incomplete lines don't have any incorrect characters - instead, they're missing some closing characters at the end of the line. To repair the navigation subsystem, you just need to figure out the sequence of closing characters that complete all open chunks in the line.
 *
 * You can only use closing characters (), ], }, or >), and you must add them in the correct order so that only legal pairs are formed and all chunks end up closed.
 *
 * In the example above, there are five incomplete lines:
 *
 * [({(<(())[]>[[{[]{<()<>> - Complete by adding }}]])})].
 * [(()[<>])]({[<{<<[]>>( - Complete by adding )}>]}).
 * (((({<>}<{<{<>}{[]{[]{} - Complete by adding }}>}>)))).
 * {<[[]]>}<{[{[{[]{()[[[] - Complete by adding ]]}}]}]}>.
 * <{([{{}}[<[[[<>{}]]]>[]] - Complete by adding ])}>.
 * Did you know that autocomplete tools also have contests? It's true! The score is determined by considering the completion string character-by-character. Start with a total score of 0. Then, for each character, multiply the total score by 5 and then increase the total score by the point value given for the character in the following table:
 *
 * ): 1 point.
 * ]: 2 points.
 * }: 3 points.
 * >: 4 points.
 * So, the last completion string above - ])}> - would be scored as follows:
 *
 * Start with a total score of 0.
 * Multiply the total score by 5 to get 0, then add the value of ] (2) to get a new total score of 2.
 * Multiply the total score by 5 to get 10, then add the value of ) (1) to get a new total score of 11.
 * Multiply the total score by 5 to get 55, then add the value of } (3) to get a new total score of 58.
 * Multiply the total score by 5 to get 290, then add the value of > (4) to get a new total score of 294.
 * The five lines' completion strings have total scores as follows:
 *
 * }}]])})] - 288957 total points.
 * )}>]}) - 5566 total points.
 * }}>}>)))) - 1480781 total points.
 * ]]}}]}]}> - 995444 total points.
 * ])}> - 294 total points.
 * Autocomplete tools are an odd bunch: the winner is found by sorting all of the scores and then taking the middle score. (There will always be an odd number of scores to consider.) In this example, the middle score is 288957 because there are the same number of scores smaller and larger than it.
 *
 * Find the completion string for each incomplete line, score the completion strings, and sort the scores. What is the middle score?
 *
 * Your puzzle answer was 2292863731.
 */
public class Puzzle1002 {

    public static void main(String... args) throws URISyntaxException, IOException {
        List<String> input = Files.readAllLines(Paths.get(Puzzle1002.class.getClassLoader().getResource("1001/input.txt").toURI()), Charset.defaultCharset());


        List<Long> scores = new ArrayList<>();
        for (String line : input) {
            Queue<Character> arrayLifoQueue = Collections.asLifoQueue(new ArrayDeque<Character>());

            String currentLine = "";

            boolean corrupt = false;

            for (char c : line.toCharArray()) {
                currentLine = currentLine + c;
                if (isOpeningChar(c)) {
                    arrayLifoQueue.add(c);
                } else if (isClosingChar(c)) {
                    char open = arrayLifoQueue.poll();
                    if (!matchesChar(open, c)) {
                        corrupt = true;
                        break;
                    }
                }

            }

            if (!corrupt) {
                long score = 0;

                while (!arrayLifoQueue.isEmpty()) {
                    char closing = arrayLifoQueue.poll();
                    score = score * 5 + scoreOpeningChar(closing);
                }
                scores.add(score);
            }

        }

        Collections.sort(scores);

        System.out.println("Solution: " + scores.get(scores.size() / 2));
    }

    private static int scoreOpeningChar(char c) {
        switch (c) {
            case '(':
                return 1;
            case '[':
                return 2;
            case '{':
                return 3;
            case '<':
                return 4;
        }
        throw new IllegalArgumentException("Unexpected char: " + c);
    }

    private static boolean isOpeningChar(char c) {
        return c == '(' || c == '[' || c == '{' || c == '<';
    }

    private static boolean isClosingChar(char c) {
        return c == ')' || c == ']' || c == '}' || c == '>';
    }

    private static boolean matchesChar(char open, char close) {
        if (open == '(' && close == ')') {
            return true;
        }
        if (open == '[' && close == ']') {
            return true;
        }
        if (open == '{' && close == '}') {
            return true;
        }
        if (open == '<' && close == '>') {
            return true;
        }

        return false;

    }

}
