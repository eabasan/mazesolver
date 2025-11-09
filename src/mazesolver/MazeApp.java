/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package mazesolver;

import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author elena
 */
public class MazeApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       // Start the program in the special GUI thread
       SwingUtilities.invokeLater(() -> {
            try {
                // Load the maze from a text file
                String file = "src/mazesolver/maze.txt"; 
                Maze maze = MazeLoaderAscii.loadFromFile(file);


                // Let the user choose which search algorithm to use (BFS or DFS)
                String[] algorithms = {"BFS", "DFS"};
                int algChoice = JOptionPane.showOptionDialog(null, "Choose algorithm:",
                        "Search Strategy", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                        null, algorithms, algorithms[0]);
                if (algChoice < 0) return;  // User clicked cancel

                // Ask if user wants to see the solving process step by step
                int animateOption = JOptionPane.showConfirmDialog(null, "Animate step-by-step?", "Animation", JOptionPane.YES_NO_OPTION);
                boolean animate = (animateOption == JOptionPane.YES_OPTION);

                // Set up the animation speed (how fast each step is shown)
                int delayMs = 100;  // Default delay is 100 milliseconds
                if (animate) {
                    String input = (String) JOptionPane.showInputDialog(null, "Delay between steps (ms):", "100");
                    try {
                        if (input != null && !input.trim().isEmpty()) delayMs = Integer.parseInt(input.trim());
                    } catch (NumberFormatException ignored) {}
                }

                // Create a final copy of delay to use inside the thread
                final int delayMsFinal = delayMs;

                // Create and setup the window that will show the maze
                JFrame frame = new JFrame("Maze Pathfinding (" + algorithms[algChoice] + ")");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                // Create the panel that will draw the maze
                MazePanel panel = new MazePanel(maze);
                frame.add(panel, BorderLayout.CENTER);
                frame.pack();
                frame.setLocationRelativeTo(null);  // Center the window on screen
                frame.setVisible(true);

                // Start searching for the solution:
                if (animate) {
                    // Run in a separate thread to not freeze the window
                    new Thread(() -> {
                        // Measure the time it takes to solve
                        long t0 = System.nanoTime();
                        List<Node> path;
                        // Choose between BFS and DFS algorithms
                        if (algChoice == 0) {
                            path = AnimatedSearcher.bfsAnimate(maze, maze.start, maze.goal, panel, delayMsFinal);
                        } else {
                            path = AnimatedSearcher.dfsAnimate(maze, maze.start, maze.goal, panel, delayMsFinal);
                        }
                        long t1 = System.nanoTime();
                        double elapsed = (t1 - t0) / 1_000_000.0;  // Convert to milliseconds
                        int length = path == null ? 0 : path.size();
                        // Show the results
                        SwingUtilities.invokeLater(() ->
                                JOptionPane.showMessageDialog(frame, "Path length: " + length + "\nTime: " + elapsed + " ms")
                        );
                    }, "SearchThread").start();
                } else {
                    // No animation: solve the maze directly without showing steps
                    SearchAlgorithm alg = (algChoice == 0) ? new BFS() : new DFS();
                    long t0 = System.nanoTime();
                    List<Node> path = alg.findPath(maze, maze.start, maze.goal);
                    long t1 = System.nanoTime();
                    double elapsed = (t1 - t0) / 1_000_000.0;  // Convert to milliseconds
                    panel.setPath(path);  // Show the final path
                    JOptionPane.showMessageDialog(frame, "Path length: " + (path==null?0:path.size()) + "\nTime: " + elapsed + " ms");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        });
    }
}
