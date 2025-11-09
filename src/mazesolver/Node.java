/*
 * This class represents a position in the maze.
 * Each Node has a row and column position, and keeps track of which Node we came from (parent).
 * This helps us remember the path we took through the maze.
 */
package mazesolver;

import java.util.Objects;

/**
 *
 * @author elena
 */
public class Node {
    // Position in the maze (row and column)
    int row, col;
    // The node we came from to reach this position
    Node parent;

    // Create a new node with its position and parent node
    public Node(int row, int col, Node parent) {
        this.row = row;
        this.col = col;
        this.parent = parent;
    }

    // Check if two nodes are in the same position
    // We only check row and col, not the parent
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Node)) {
            return false;
        }
        Node node = (Node) o;
        return row == node.row && col == node.col;
    }

    // Generate a unique number for this position
    // This helps when using nodes in HashSets or HashMaps
    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return "(" + row + "," + col + ")";
    }

}
