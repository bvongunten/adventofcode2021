package ch.nostromo.adventofcode2021.puzzle0401;

import java.io.IOException;
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
 * --- Day 4: Giant Squid ---
 * You're already almost 1.5km (almost a mile) below the surface of the ocean, already so deep that you can't see any sunlight. What you can see, however, is a giant squid that has attached itself to the outside of your submarine.
 *
 * Maybe it wants to play bingo?
 *
 * Bingo is played on a set of boards each consisting of a 5x5 grid of numbers. Numbers are chosen at random, and the chosen number is marked on all boards on which it appears. (Numbers may not appear on all boards.) If all numbers in any row or any column of a board are marked, that board wins. (Diagonals don't count.)
 *
 * The submarine has a bingo subsystem to help passengers (currently, you and the giant squid) pass the time. It automatically generates a random order in which to draw numbers and a random set of boards (your puzzle input). For example:
 *
 * 7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1
 *
 * 22 13 17 11  0
 *  8  2 23  4 24
 * 21  9 14 16  7
 *  6 10  3 18  5
 *  1 12 20 15 19
 *
 *  3 15  0  2 22
 *  9 18 13 17  5
 * 19  8  7 25 23
 * 20 11 10 24  4
 * 14 21 16 12  6
 *
 * 14 21 17 24  4
 * 10 16 15  9 19
 * 18  8 23 26 20
 * 22 11 13  6  5
 *  2  0 12  3  7
 * After the first five numbers are drawn (7, 4, 9, 5, and 11), there are no winners, but the boards are marked as follows (shown here adjacent to each other to save space):
 *
 * 22 13 17 11  0         3 15  0  2 22        14 21 17 24  4
 *  8  2 23  4 24         9 18 13 17  5        10 16 15  9 19
 * 21  9 14 16  7        19  8  7 25 23        18  8 23 26 20
 *  6 10  3 18  5        20 11 10 24  4        22 11 13  6  5
 *  1 12 20 15 19        14 21 16 12  6         2  0 12  3  7
 * After the next six numbers are drawn (17, 23, 2, 0, 14, and 21), there are still no winners:
 *
 * 22 13 17 11  0         3 15  0  2 22        14 21 17 24  4
 *  8  2 23  4 24         9 18 13 17  5        10 16 15  9 19
 * 21  9 14 16  7        19  8  7 25 23        18  8 23 26 20
 *  6 10  3 18  5        20 11 10 24  4        22 11 13  6  5
 *  1 12 20 15 19        14 21 16 12  6         2  0 12  3  7
 * Finally, 24 is drawn:
 *
 * 22 13 17 11  0         3 15  0  2 22        14 21 17 24  4
 *  8  2 23  4 24         9 18 13 17  5        10 16 15  9 19
 * 21  9 14 16  7        19  8  7 25 23        18  8 23 26 20
 *  6 10  3 18  5        20 11 10 24  4        22 11 13  6  5
 *  1 12 20 15 19        14 21 16 12  6         2  0 12  3  7
 * At this point, the third board wins because it has at least one complete row or column of marked numbers (in this case, the entire top row is marked: 14 21 17 24 4).
 *
 * The score of the winning board can now be calculated. Start by finding the sum of all unmarked numbers on that board; in this case, the sum is 188. Then, multiply that sum by the number that was just called when the board won, 24, to get the final score, 188 * 24 = 4512.
 *
 * To guarantee victory against the giant squid, figure out which board will win first. What will your final score be if you choose that board?
 *
 * Your puzzle answer was 54275.
 */
public class Puzzle0401 {

    public static void main(String... args) throws URISyntaxException, IOException {
        List<String> input = Files.readAllLines(Paths.get(Puzzle0401.class.getClassLoader().getResource("0401/input.txt").toURI()), Charset.defaultCharset());

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
        while (winner == null) {
            int currentNumber = numbers.get(numberCount);
            tableaus.forEach((t) -> t.putNumber(currentNumber));

            for (Tableau tableau : tableaus) {
                if (tableau.callBing()) {
                    winner = tableau;
                    luckyNumber = currentNumber;
                    break;
                }
            }

            numberCount++;
        }


        System.out.println("Solution " + (luckyNumber * winner.getNonMarkedSum()));
    }

    public static class Tableau {
        int[][] tableau = new int[5][5];
        boolean[][] hits = new boolean[5][5];

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
