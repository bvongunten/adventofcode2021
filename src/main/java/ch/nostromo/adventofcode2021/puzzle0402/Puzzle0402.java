package ch.nostromo.adventofcode2021.puzzle0402;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Puzzle0402 {

    public static void main(String... args) throws URISyntaxException, IOException {
        List<String> input = Files.readAllLines(Paths.get(Puzzle0402.class.getClassLoader().getResource("0401/input.txt").toURI()), Charset.defaultCharset());

        // Get and remove numbers
        List<Integer> numbers = Stream.of(input.get(0).split(",")).map(Integer::parseInt).collect(Collectors.toList());
        input.remove(0);


        List<Tableau> tableaus = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            List<String> tableauLines = input.subList(i * 6 + 1, i * 6 + 6);
            tableaus.add(new Tableau(tableauLines));
        }

        Tableau winner = null;
        int numberCount = 0;
        int luckyNumber = 0;
        int winnerCount = 0;
        while (winnerCount <= 99) {
            int currentNumber = numbers.get(numberCount);
            tableaus.forEach((t) -> t.putNumber(currentNumber));

            for (Tableau tableau : tableaus) {
                if (tableau.callBing()) {
                    winner = tableau;
                    luckyNumber = currentNumber;
                    if (winner.winnerCount == -1) {
                        winner.winnerCount = winnerCount;
                        winnerCount++;
                    }
                    if (winnerCount == 100) {
                        break;
                    }
                }
            }

            numberCount++;
        }


        System.out.println("Solution " + (luckyNumber * winner.getNonMarkedSum()));
    }

    public static class Tableau {
        int[][] tableau = new int[5][5];
        boolean[][] hits = new boolean[5][5];

        int winnerCount = -1;

        public Tableau(List<String> lines) {

            for (int i = 0; i <= 4; i++) {
                String[] lineS = lines.get(i).trim().split("\\s+");
                for (int x = 0; x <= 4; x++) {
                    tableau[i][x] = Integer.parseInt(lineS[x]);
                }
            }
        }

        public void putNumber(int number) {
            for (int i = 0; i <= 4; i++) {
                for (int x = 0; x <= 4; x++) {
                    if (tableau[i][x] == number) {
                        hits[i][x] = true;
                    }
                }
            }
        }

        public boolean callBing() {
            // Horizontal
            for (int i = 0; i <= 4; i++) {
                int hitCount = 0;
                for (int x = 0; x <= 4; x++) {
                    if (hits[i][x]) {
                        hitCount++;
                    }
                }
                if (hitCount == 5) return true;
            }

            for (int i = 0; i <= 4; i++) {
                int hitCount = 0;
                for (int x = 0; x <= 4; x++) {
                    if (hits[x][i]) {
                        hitCount++;
                    }
                }
                if (hitCount == 5) return true;
            }

            return false;

        }

        public int getNonMarkedSum() {
            int result = 0;
            for (int i = 0; i <= 4; i++) {
                for (int x = 0; x <= 4; x++) {
                    if (!hits[i][x]) {
                        result += tableau[i][x];
                    }
                }
            }

            return result;
        }

    }


}
