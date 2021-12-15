package ch.nostromo.adventofcode2021.puzzle1502;

import ch.nostromo.adventofcode2021.puzzle1101.Puzzle1101;
import ch.nostromo.adventofcode2021.puzzle1501.Puzzle1501;
import lombok.Data;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * --- Part Two ---
 * Now that you know how to find low-risk paths in the cave, you can try to find your way out.
 *
 * The entire cave is actually five times larger in both dimensions than you thought; the area you originally scanned is just one tile in a 5x5 tile area that forms the full map. Your original map tile repeats to the right and downward; each time the tile repeats to the right or downward, all of its risk levels are 1 higher than the tile immediately up or left of it. However, risk levels above 9 wrap back around to 1. So, if your original map had some position with a risk level of 8, then that same position on each of the 25 total tiles would be as follows:
 *
 * 8 9 1 2 3
 * 9 1 2 3 4
 * 1 2 3 4 5
 * 2 3 4 5 6
 * 3 4 5 6 7
 * Each single digit above corresponds to the example position with a value of 8 on the top-left tile. Because the full map is actually five times larger in both dimensions, that position appears a total of 25 times, once in each duplicated tile, with the values shown above.
 *
 *
 * ....
 *
 *
 * The total risk of this path is 315 (the starting position is still never entered, so its risk is not counted).
 *
 * Using the full map, what is the lowest total risk of any path from the top left to the bottom right?
 *
 * Your puzzle answer was 2979.
 *
 */
public class Puzzle1502 {

    public static void main(String... args) throws URISyntaxException, IOException {
        List<String> input = Files.readAllLines(Paths.get(Puzzle1101.class.getClassLoader().getResource("1501/input.txt").toURI()), Charset.defaultCharset());


        int initialLength = input.size();
        int initialWidth = input.get(0).length();

        int length = initialLength * 5;
        int width = initialWidth * 5;

        Node[][] nodes = new Node[length][width];
        for (int y = 0; y < length; y++) {
            for (int x = 0; x < width; x++) {
                nodes[y][x] = new Node();
            }
        }


        // Fill board & horizontal expansion
        int[][] numbers = new int[length][width];
        for (int y = 0; y < initialLength; y++) {
            char[] chars = input.get(y).toCharArray();
            for (int x = 0; x < chars.length; x++) {
                int value = Character.getNumericValue(chars[x]);

                // prepare horizontal expansion
                int horiziontalValue = value;
                for (int i = 0; i < 5; i++) {
                    int newX = x + (i * initialWidth);
                    if (horiziontalValue > 9) {
                        horiziontalValue -= 9;
                    }
                    numbers[y][newX] = horiziontalValue;
                    horiziontalValue++;
                }
            }
        }

        // Vertical expansion
        for (int i = 1; i < 5; i++) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < initialLength; y++) {
                    int value = numbers[y][x] + i;
                    if (value > 9) {
                        value -= 9;
                    }
                    numbers[y + (i*initialLength)][x] =value;
                }
            }

        }

        // Link nodes
        for (int y = 0; y < length; y++) {
            for (int x = 0; x < width; x++) {
                if (y > 0) {
                    nodes[y][x].addDestination(nodes[y - 1][x], numbers[y - 1][x]);
                }
                if (y < length - 1) {
                    nodes[y][x].addDestination(nodes[y + 1][x], numbers[y + 1][x]);
                }
                if (x > 0) {
                    nodes[y][x].addDestination(nodes[y][x - 1], numbers[y][x - 1]);
                }
                if (x < width - 1) {
                    nodes[y][x].addDestination(nodes[y][x + 1], numbers[y][x + 1]);
                }
            }
        }

        calculateShortestPathFromSource(nodes[0][0]);

        System.out.println("Solution: " + nodes[length - 1][width -1].distance);
    }

    // Dijkstra from Baeldung
    public static class Node {

        private List<Node> shortestPath = new LinkedList<>();
        private Integer distance = Integer.MAX_VALUE;
        Map<Node, Integer> adjacentNodes = new HashMap<>();

        public void addDestination(Node destination, int distance) {
            adjacentNodes.put(destination, distance);
        }

        public List<Node> getShortestPath() {
            return shortestPath;
        }

        public void setShortestPath(List<Node> shortestPath) {
            this.shortestPath = shortestPath;
        }

        public Integer getDistance() {
            return distance;
        }

        public void setDistance(Integer distance) {
            this.distance = distance;
        }

        public Map<Node, Integer> getAdjacentNodes() {
            return adjacentNodes;
        }

    }

    public static void calculateShortestPathFromSource(Node source) {
        source.setDistance(0);

        Set<Node> settledNodes = new HashSet<>();
        Set<Node> unsettledNodes = new HashSet<>();

        unsettledNodes.add(source);

        while (unsettledNodes.size() != 0) {
            Node currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            for (Map.Entry<Node, Integer> adjacencyPair :
                    currentNode.getAdjacentNodes().entrySet()) {
                Node adjacentNode = adjacencyPair.getKey();
                Integer edgeWeight = adjacencyPair.getValue();
                if (!settledNodes.contains(adjacentNode)) {
                    CalculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    unsettledNodes.add(adjacentNode);
                }
            }
            settledNodes.add(currentNode);
        }
    }

    private static Node getLowestDistanceNode(Set<Node> unsettledNodes) {
        Node lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (Node node : unsettledNodes) {
            int nodeDistance = node.getDistance();
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }

    private static void CalculateMinimumDistance(Node evaluationNode, Integer edgeWeigh, Node sourceNode) {
        Integer sourceDistance = sourceNode.getDistance();
        if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeigh);
            LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }
}
