package ch.nostromo.adventofcode2021.puzzle1202;

import ch.nostromo.adventofcode2021.puzzle1201.Puzzle1201;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
*After reviewing the available paths, you realize you might have time to visit a single small cave twice. Specifically, big caves can be visited any number of times, a single small cave can be visited at most twice, and the remaining small caves can be visited at most once. However, the caves named start and end can only be visited exactly once each: once you leave the start cave, you may not return to it, and once you reach the end cave, the path must end immediately.
 *
 * Now, the 36 possible paths through the first example above are:
 *
 * start,A,b,A,b,A,c,A,end
 * start,A,b,A,b,A,end
 * start,A,b,A,b,end
 * start,A,b,A,c,A,b,A,end
 * start,A,b,A,c,A,b,end
 * start,A,b,A,c,A,c,A,end
 * start,A,b,A,c,A,end
 * start,A,b,A,end
 * start,A,b,d,b,A,c,A,end
 * start,A,b,d,b,A,end
 * start,A,b,d,b,end
 * start,A,b,end
 * start,A,c,A,b,A,b,A,end
 * start,A,c,A,b,A,b,end
 * start,A,c,A,b,A,c,A,end
 * start,A,c,A,b,A,end
 * start,A,c,A,b,d,b,A,end
 * start,A,c,A,b,d,b,end
 * start,A,c,A,b,end
 * start,A,c,A,c,A,b,A,end
 * start,A,c,A,c,A,b,end
 * start,A,c,A,c,A,end
 * start,A,c,A,end
 * start,A,end
 * start,b,A,b,A,c,A,end
 * start,b,A,b,A,end
 * start,b,A,b,end
 * start,b,A,c,A,b,A,end
 * start,b,A,c,A,b,end
 * start,b,A,c,A,c,A,end
 * start,b,A,c,A,end
 * start,b,A,end
 * start,b,d,b,A,c,A,end
 * start,b,d,b,A,end
 * start,b,d,b,end
 * start,b,end
 * The slightly larger example above now has 103 paths through it, and the even larger example now has 3509 paths through it.
 *
 * Given these new rules, how many paths through this cave system are there?
 *
 * Your puzzle answer was 140718.
 * */
public class Puzzle1202 {

    public static void main(String... args) throws URISyntaxException, IOException {
        List<String> input = Files.readAllLines(Paths.get(Puzzle1202.class.getClassLoader().getResource("1201/input.txt").toURI()), Charset.defaultCharset());

        Map<String, Node> allNodes = new HashMap<>();


        for (String line : input) {
            String firstNodeName = line.split("-")[0];
            String secondNodeName = line.split("-")[1];

            Node firstNode = getOrCreateNode(allNodes, firstNodeName);
            Node secondNode = getOrCreateNode(allNodes, secondNodeName);

            firstNode.getConnections().add(secondNode);
            secondNode.getConnections().add(firstNode);
        }

        Node startNode = allNodes.get("start");
        List<List<Node>> fullPaths = new ArrayList<>();

        List<Node> smallCaves = new ArrayList<>();
        for (Node node : allNodes.values()) {
            if (!node.isBigCave() && !node.getName().equals("start") && !node.getName().equals("end")) {
                smallCaves.add(node);
            }
        }

        for (Node smallCaveToVisitTwice : smallCaves) {
            pathFinder(fullPaths, new ArrayList<>(), startNode, smallCaveToVisitTwice);
        }

        // Poor man's filter ;)
        Set<String> routes = new HashSet<>();
        for (List<Node> route : fullPaths) {
            routes.add(route.toString());
        }


        System.out.println("Solution: " + routes.size());
    }

    private static void pathFinder(List<List<Node>> fullPaths, List<Node> currentPath, Node currentNode, Node smallCaveToVisitTwice) {
        if (!currentNode.isBigCave() && currentPath.contains(currentNode)) {
            if (currentNode.equals(smallCaveToVisitTwice)) {
                if (currentPath.stream().filter(e -> e.equals(currentNode)).count() > 1) {
                    return;
                }
            } else {
                return;
            }
        }

        if (currentNode.getName().equals("end")) {
            List<Node> finishedPath = new ArrayList<>(currentPath);
            finishedPath.add(currentNode);
            fullPaths.add(finishedPath);
            return;
        }

        for (Node node : currentNode.getConnections()) {
            currentPath.add(currentNode);
            pathFinder(fullPaths, currentPath, node, smallCaveToVisitTwice);
            currentPath.remove(currentPath.size() - 1);
        }

    }

    private static Node getOrCreateNode(Map<String, Node> nodes, String name) {
        if (nodes.containsKey(name)) {
            return nodes.get(name);
        } else {
            Node result = new Node(name);
            nodes.put(name, result);
            return result;
        }
    }

    private static class Node {

        String name;
        Set<Node> connections = new HashSet<>();


        public Node(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public Set<Node> getConnections() {
            return connections;
        }

        public boolean isBigCave() {
            return Character.isUpperCase(name.charAt(0));
        }

        public String toString() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return Objects.equals(name, node.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }

}
