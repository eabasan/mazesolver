/*
 * This interface defines what every search algorithm must do.
 * Any class that wants to solve the maze must implement this interface.
 * Examples are BFS (Breadth-First Search) and DFS (Depth-First Search).
 */
package mazesolver;

import java.util.List;

/**
 *
 * @author elena
 */
public interface SearchAlgorithm {
    // Find a path from start to goal in the given maze
    // Returns a list of nodes that make up the path
    // If no path is found, returns null
    List<Node> findPath(Maze maze, Node start, Node goal);
}
