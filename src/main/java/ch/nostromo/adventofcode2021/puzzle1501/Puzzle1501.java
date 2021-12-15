package ch.nostromo.adventofcode2021.puzzle1501;

import ch.nostromo.adventofcode2021.puzzle1101.Puzzle1101;
import lombok.Data;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * --- Day 15: Chiton ---
 * You've almost reached the exit of the cave, but the walls are getting closer together. Your submarine can barely still fit, though; the main problem is that the walls of the cave are covered in chitons, and it would be best not to bump any of them.
 *
 * The cavern is large, but has a very low ceiling, restricting your motion to two dimensions. The shape of the cavern resembles a square; a quick scan of chiton density produces a map of risk level throughout the cave (your puzzle input). For example:
 *
 * 1163751742
 * 1381373672
 * 2136511328
 * 3694931569
 * 7463417111
 * 1319128137
 * 1359912421
 * 3125421639
 * 1293138521
 * 2311944581
 * You start in the top left position, your destination is the bottom right position, and you cannot move diagonally. The number at each position is its risk level; to determine the total risk of an entire path, add up the risk levels of each position you enter (that is, don't count the risk level of your starting position unless you enter it; leaving it adds no risk to your total).
 *
 * Your goal is to find a path with the lowest total risk. In this example, a path with the lowest total risk is highlighted here:
 *
 * 1163751742
 * 1381373672
 * 2136511328
 * 3694931569
 * 7463417111
 * 1319128137
 * 1359912421
 * 3125421639
 * 1293138521
 * 2311944581
 * The total risk of this path is 40 (the starting position is never entered, so its risk is not counted).
 *
 * What is the lowest total risk of any path from the top left to the bottom right?
 *
 * Your puzzle answer was 656.
 */
public class Puzzle1501 {

    public static void main(String... args) throws URISyntaxException, IOException {
        List<String> input = Files.readAllLines(Paths.get(Puzzle1101.class.getClassLoader().getResource("1501/input.txt").toURI()), Charset.defaultCharset());

        int[][] numbers = new int[input.size()][input.get(0).length()];
        Node[][] nodes = new Node[numbers.length][numbers[0].length];

        // Fill board
        for (int y = 0; y < input.size(); y++) {
            char[] chars = input.get(y).toCharArray();
            for (int x = 0; x < chars.length; x++) {
                numbers[y][x] = Character.getNumericValue(chars[x]);
                nodes[y][x] = new Node();
            }
        }

        // Link nodes
        for (int y = 0; y < numbers.length; y++) {
            for (int x = 0; x < numbers[y].length; x++) {

                if (y > 0) {
                    nodes[y][x].addDestination(nodes[y - 1][x], numbers[y - 1][x]);
                }
                if (y < numbers.length - 1) {
                    nodes[y][x].addDestination(nodes[y + 1][x], numbers[y + 1][x]);
                }
                if (x > 0) {
                    nodes[y][x].addDestination(nodes[y][x - 1], numbers[y][x - 1]);
                }
                if (x < numbers[y].length - 1) {
                    nodes[y][x].addDestination(nodes[y][x + 1], numbers[y][x + 1]);
                }
            }
        }

        calculateShortestPathFromSource(nodes[0][0]);

        System.out.println("Solution: " + nodes[nodes.length - 1][nodes[0].length - 1].distance);
    }

    // Dijkstra from Baeldung
    public static class Node {

        private List<Node> shortestPath = new LinkedList<>();
        private Integer distance = Integer.MAX_VALUE;
        Map<Node, Integer> adjacentNodes = new HashMap<>();

        public void addDestination(Node destination, int distance) {
            adjacentNodes.put(destination, distance);
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

        public List<Node> getShortestPath() {
            return shortestPath;
        }

        public void setShortestPath(List<Node> shortestPath) {
            this.shortestPath = shortestPath;
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
