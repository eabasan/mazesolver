/*
 * This class implements the Breadth-First Search algorithm.
 * BFS explores the maze by checking all positions at the current distance
 * before moving to positions that are further away.
 * This ensures we find the shortest path to the goal.
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
        // Queue to store positions we need to check
        Queue<Node> queue = new LinkedList<>();
        // Set to remember positions we already checked
        Set<Node> visited = new HashSet<>();
        // Start from the beginning position
        queue.add(start);
        visited.add(start);

        // Keep looking until we check all possible positions
        while (!queue.isEmpty()) {
            // Get the next position to check
            Node cur = queue.poll();
            // If we found the goal, we're done!
            if (cur.equals(goal)) {
                return reconstructPath(cur);
            }

            // Check all four directions: up, left, down, right
            int[][] dirs = {{-1, 0}, {0, -1}, {1, 0}, {0, 1}};
            for (int[] d : dirs) {
                // Calculate new position
                int nr = cur.row + d[0], nc = cur.col + d[1];
                // Check if we can move to this new position
                if (maze.canMove(cur.row, cur.col, nr, nc)) {
                    // Create a new node for this position
                    Node neigh = new Node(nr, nc, cur);
                    // If we haven't been here before, add it to check later
                    if (!visited.contains(neigh)) {
                        visited.add(neigh);
                        queue.add(neigh);
                    }
                }
            }
        }
        // If we checked everywhere and didn't find the goal, there's no path
        return Collections.emptyList();
    }

    // Follow the parent links from goal back to start to get the path
    private List<Node> reconstructPath(Node goal) {
        List<Node> path = new ArrayList<>();
        // Start at the goal
        Node cur = goal;
        // Keep going until we reach the start (which has no parent)
        while (cur != null) {
            path.add(cur);
            cur = cur.parent;
        }
        // Since we built the path backwards, reverse it
        Collections.reverse(path);
        return path;
    }

}
