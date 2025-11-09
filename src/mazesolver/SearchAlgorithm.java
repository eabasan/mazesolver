/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
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
