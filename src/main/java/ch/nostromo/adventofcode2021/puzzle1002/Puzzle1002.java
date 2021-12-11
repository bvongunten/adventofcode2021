package ch.nostromo.adventofcode2021.puzzle1002;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

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
