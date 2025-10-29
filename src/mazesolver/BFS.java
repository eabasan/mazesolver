/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mazesolver;

import java.util.*;

/**
 *
 * @author elena
 */
public class BFS implements SearchAlgorithm {

    @Override
    public List<Node> findPath(Maze maze, Node start, Node goal) {
        Queue<Node> queue = new LinkedList<>();
        Set<Node> visited = new HashSet<>();
        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Node cur = queue.poll();
            if (cur.equals(goal)) {
                return reconstructPath(cur);
            }

            for (int[] d : new int[][]{{-1, 0}, {0, -1}, {1, 0}, {0, 1}}) {
                int nr = cur.row + d[0], nc = cur.col + d[1];
                if (maze.isFree(nr, nc)) {
                    Node neigh = new Node(nr, nc, cur);
                    if (!visited.contains(neigh)) {
                        visited.add(neigh);
                        queue.add(neigh);
                    }
                }
            }
        }
        return Collections.emptyList();
    }

    private List<Node> reconstructPath(Node goal) {
        List<Node> path = new ArrayList<>();
        Node cur = goal;
        while (cur != null) {
            path.add(cur);
            cur = cur.parent;
        }
        Collections.reverse(path);
        return path;
    }

}
