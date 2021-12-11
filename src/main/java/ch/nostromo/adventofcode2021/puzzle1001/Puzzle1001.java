package ch.nostromo.adventofcode2021.puzzle1001;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Puzzle1001 {

    public static void main(String... args) throws URISyntaxException, IOException {
        List<String> input = Files.readAllLines(Paths.get(Puzzle1001.class.getClassLoader().getResource("1001/input.txt").toURI()), Charset.defaultCharset());

        int solution = 0;

        for (String line : input) {
            Queue<Character> queue = Collections.asLifoQueue(new ArrayDeque<Character>());

            String currentLine = "";
            for (char c : line.toCharArray()) {
                currentLine = currentLine + c;
                if (isOpeningChar(c)) {
                    queue.add(c);
                } else if (isClosingChar(c)) {
                    char open = queue.poll();

                    if (!matchesChar(open, c)) {
                        System.out.println("Failed on " + c + " opened by : " + open + " on line: " + currentLine);

                        solution += scoreClosingChar(c);
                        break;
                    }
                }
            }

        }


        System.out.println("Solution: " + solution);
    }

    private static int scoreClosingChar(char c) {
        switch (c) {
            case ')' : return 3;
            case ']' : return 57;
            case '}' : return 1197;
            case '>' : return 25137;
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
        if (open == '<' && close == '>' ) {
            return true;
        }

        return false;

    }

}
