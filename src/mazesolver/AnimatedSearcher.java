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

   // Animated version of Breadth-First Search
   public static List<Node> bfsAnimate(Maze maze, Node start, Node goal, MazePanel panel, int delayMs) {
        // Same setup as BFS
        Queue<Node> queue = new LinkedList<>();
        Set<Node> visited = new HashSet<>();
        queue.add(start);
        visited.add(start);
        // Show the start position as frontier (orange)
        SwingUtilities.invokeLater(() -> panel.addFrontier(start));

        while (!queue.isEmpty()) {
            // Get next position to check
            Node cur = queue.poll();

            // Update display: remove from frontier and mark as visited
            SwingUtilities.invokeLater(() -> {
                panel.removeFrontier(cur);  // Remove orange
                panel.addVisited(cur);      // Add blue
            });
            sleep(delayMs);  // Wait so we can see the change

            // If we found the goal, show the path and we're done
            if (cur.equals(goal)) {
                List<Node> path = reconstructPath(cur);
                SwingUtilities.invokeLater(() -> panel.setPath(path));
                return path;
            }

            // Check all four directions (up, left, down, right)
            int[][] dirs = {{-1,0},{0,-1},{1,0},{0,1}};
            for (int[] d : dirs) {
                int nr = cur.row + d[0], nc = cur.col + d[1];
                if (maze.canMove(cur.row, cur.col, nr, nc)) {
                    Node neigh = new Node(nr, nc, cur);
                    if (!visited.contains(neigh)) {
                        visited.add(neigh);
                        queue.add(neigh);
                        // Show new position in frontier (orange)
                        SwingUtilities.invokeLater(() -> panel.addFrontier(neigh));
                    }
                }
            }
            sleep(delayMs);  // Wait so we can see the changes
        }
        // If we checked everywhere and didn't find the goal, there's no path
        return Collections.emptyList();
    }

    // Animated version of Depth-First Search
    public static List<Node> dfsAnimate(Maze maze, Node start, Node goal, MazePanel panel, int delayMs) {
        // Same setup as DFS
        Stack<Node> stack = new Stack<>();
        Set<Node> visited = new HashSet<>();
        stack.push(start);
        visited.add(start);
        // Show the start position as frontier (orange)
        SwingUtilities.invokeLater(() -> panel.addFrontier(start));

        while (!stack.isEmpty()) {
            // Get next position to check
            Node cur = stack.pop();

            // Update display: remove from frontier and mark as visited
            SwingUtilities.invokeLater(() -> {
                panel.removeFrontier(cur);  // Remove orange
                panel.addVisited(cur);      // Add blue
            });
            sleep(delayMs);  // Wait so we can see the change

            // If we found the goal, show the path and we're done
            if (cur.equals(goal)) {
                List<Node> path = reconstructPath(cur);
                SwingUtilities.invokeLater(() -> panel.setPath(path));
                return path;
            }

            // Check all four directions (up, left, down, right)
            int[][] dirs = {{-1,0},{0,-1},{1,0},{0,1}};
            for (int[] d : dirs) {
                int nr = cur.row + d[0], nc = cur.col + d[1];
                if (maze.canMove(cur.row, cur.col, nr, nc)) {
                    Node neigh = new Node(nr, nc, cur);
                    if (!visited.contains(neigh)) {
                        visited.add(neigh);
                        stack.push(neigh);
                        // Show new position in frontier (orange)
                        SwingUtilities.invokeLater(() -> panel.addFrontier(neigh));
                    }
                }
            }
            sleep(delayMs);  // Wait so we can see the changes
        }
        // If we checked everywhere and didn't find the goal, there's no path
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
