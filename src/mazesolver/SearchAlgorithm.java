/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mazesolver;

import java.util.List;

/**
 *
 * @author elena
 */
public interface SearchAlgorithm {
    java.util.List<Node> findPath(Maze maze, Node start, Node goal);
}
