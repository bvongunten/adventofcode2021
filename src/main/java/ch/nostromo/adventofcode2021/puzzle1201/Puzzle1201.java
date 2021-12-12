package ch.nostromo.adventofcode2021.puzzle1201;

import ch.nostromo.adventofcode2021.puzzle1202.Puzzle1202;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * --- Day 12: Passage Pathing ---
 * With your submarine's subterranean subsystems subsisting suboptimally, the only way you're getting out of this cave anytime soon is by finding a path yourself. Not just a path - the only way to know if you've found the best path is to find all of them.
 * <p>
 * Fortunately, the sensors are still mostly working, and so you build a rough map of the remaining caves (your puzzle input). For example:
 * <p>
 * start-A
 * start-b
 * A-c
 * A-b
 * b-d
 * A-end
 * b-end
 * This is a list of how all of the caves are connected. You start in the cave named start, and your destination is the cave named end. An entry like b-d means that cave b is connected to cave d - that is, you can move between them.
 * <p>
 * So, the above cave system looks roughly like this:
 * <p>
 * start
 * /   \
 * c--A-----b--d
 * \   /
 * end
 * Your goal is to find the number of distinct paths that start at start, end at end, and don't visit small caves more than once. There are two types of caves: big caves (written in uppercase, like A) and small caves (written in lowercase, like b). It would be a waste of time to visit any small cave more than once, but big caves are large enough that it might be worth visiting them multiple times. So, all paths you find should visit small caves at most once, and can visit big caves any number of times.
 * <p>
 * Given these rules, there are 10 paths through this example cave system:
 * <p>
 * start,A,b,A,c,A,end
 * start,A,b,A,end
 * start,A,b,end
 * start,A,c,A,b,A,end
 * start,A,c,A,b,end
 * start,A,c,A,end
 * start,A,end
 * start,b,A,c,A,end
 * start,b,A,end
 * start,b,end
 * (Each line in the above list corresponds to a single path; the caves visited by that path are listed in the order they are visited and separated by commas.)
 * <p>
 * Note that in this cave system, cave d is never visited by any path: to do so, cave b would need to be visited twice (once on the way to cave d and a second time when returning from cave d), and since cave b is small, this is not allowed.
 * <p>
 * Here is a slightly larger example:
 * <p>
 * dc-end
 * HN-start
 * start-kj
 * dc-start
 * dc-HN
 * LN-dc
 * HN-end
 * kj-sa
 * kj-HN
 * kj-dc
 * The 19 paths through it are as follows:
 * <p>
 * start,HN,dc,HN,end
 * start,HN,dc,HN,kj,HN,end
 * start,HN,dc,end
 * start,HN,dc,kj,HN,end
 * start,HN,end
 * start,HN,kj,HN,dc,HN,end
 * start,HN,kj,HN,dc,end
 * start,HN,kj,HN,end
 * start,HN,kj,dc,HN,end
 * start,HN,kj,dc,end
 * start,dc,HN,end
 * start,dc,HN,kj,HN,end
 * start,dc,end
 * start,dc,kj,HN,end
 * start,kj,HN,dc,HN,end
 * start,kj,HN,dc,end
 * start,kj,HN,end
 * start,kj,dc,HN,end
 * start,kj,dc,end
 * Finally, this even larger example has 226 paths through it:
 * <p>
 * fs-end
 * he-DX
 * fs-he
 * start-DX
 * pj-DX
 * end-zg
 * zg-sl
 * zg-pj
 * pj-he
 * RW-he
 * fs-DX
 * pj-RW
 * zg-RW
 * start-pj
 * he-WI
 * zg-he
 * pj-fs
 * start-RW
 * How many paths through this cave system are there that visit small caves at most once?
 * <p>
 * Your puzzle answer was 4691.
 */
public class Puzzle1201 {

    public static void main(String... args) throws URISyntaxException, IOException {
        List<String> input = Files.readAllLines(Paths.get(Puzzle1201.class.getClassLoader().getResource("1201/input.txt").toURI()), Charset.defaultCharset());

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

        pathFinder(fullPaths, new ArrayList<>(), startNode);

        System.out.println("Solution: " + fullPaths.size());
    }

    private static void pathFinder(List<List<Node>> fullPaths, List<Node> currentPath, Node currentNode) {
        if (!currentNode.isBigCave() && currentPath.contains(currentNode)) {
            return;
        }

        if (currentNode.getName().equals("end")) {
            List<Node> finishedPath = new ArrayList<>(currentPath);
            finishedPath.add(currentNode);
            fullPaths.add(finishedPath);
            return;
        }

        for (Node node : currentNode.getConnections()) {
            currentPath.add(currentNode);
            pathFinder(fullPaths, currentPath, node);
            currentPath.remove(currentPath.size() -1);
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
