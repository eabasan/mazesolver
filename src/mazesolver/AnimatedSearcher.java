/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mazesolver;

import java.util.*;
import javax.swing.*;

/**
 *
 * @author elena
 */
public class AnimatedSearcher {
    // Ejecuta BFS animado; devuelve la ruta final (o lista vacía).
    public static List<Node> bfsAnimate(Maze maze, Node start, Node goal, MazePanel panel, int delayMs) {
        Queue<Node> queue = new LinkedList<>();
        Set<Node> visited = new HashSet<>();
        queue.add(start);
        visited.add(start);
        panel.addFrontier(start);

        while (!queue.isEmpty()) {
            Node cur = queue.poll();

            // marcar visitado
            SwingUtilities.invokeLater(() -> {
                panel.removeFrontier(cur);
                panel.addVisited(cur);
            });
            sleep(delayMs);

            if (cur.equals(goal)) {
                List<Node> path = reconstructPath(cur);
                SwingUtilities.invokeLater(() -> panel.setPath(path));
                return path;
            }

            for (int[] d : new int[][]{{-1,0},{0,-1},{1,0},{0,1}}) {
                int nr = cur.row + d[0], nc = cur.col + d[1];
                if (maze.isFree(nr, nc)) {
                    Node neigh = new Node(nr, nc, cur);
                    if (!visited.contains(neigh)) {
                        visited.add(neigh);
                        queue.add(neigh);
                        SwingUtilities.invokeLater(() -> panel.addFrontier(neigh));
                    }
                }
            }
            sleep(delayMs);
        }

        return Collections.emptyList();
    }

    public static List<Node> dfsAnimate(Maze maze, Node start, Node goal, MazePanel panel, int delayMs) {
        Stack<Node> stack = new Stack<>();
        Set<Node> visited = new HashSet<>();
        stack.push(start);
        visited.add(start);
        panel.addFrontier(start);

        while (!stack.isEmpty()) {
            Node cur = stack.pop();

            SwingUtilities.invokeLater(() -> {
                panel.removeFrontier(cur);
                panel.addVisited(cur);
            });
            sleep(delayMs);

            if (cur.equals(goal)) {
                List<Node> path = reconstructPath(cur);
                SwingUtilities.invokeLater(() -> panel.setPath(path));
                return path;
            }

            for (int[] d : new int[][]{{-1,0},{0,-1},{1,0},{0,1}}) {
                int nr = cur.row + d[0], nc = cur.col + d[1];
                if (maze.isFree(nr, nc)) {
                    Node neigh = new Node(nr, nc, cur);
                    if (!visited.contains(neigh)) {
                        visited.add(neigh);
                        stack.push(neigh);
                        SwingUtilities.invokeLater(() -> panel.addFrontier(neigh));
                    }
                }
            }
            sleep(delayMs);
        }
        return Collections.emptyList();
    }

    private static List<Node> reconstructPath(Node goal) {
        List<Node> path = new ArrayList<>();
        Node cur = goal;
        while (cur != null) {
            path.add(cur);
            cur = cur.parent;
        }
        Collections.reverse(path);
        return path;
    }

    private static void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}
