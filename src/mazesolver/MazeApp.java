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
       SwingUtilities.invokeLater(() -> {
            try {
                String file = "src/mazesolver/maze.txt"; // coloca aquí tu fichero (o pásalo como arg)
                //Maze maze = MazeLoader.loadFromFile(file);
                Maze maze = MazeLoaderAscii.loadFromFile(file);


                String[] algorithms = {"BFS", "DFS"};
                int algChoice = JOptionPane.showOptionDialog(null, "Choose algorithm:",
                        "Search Strategy", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                        null, algorithms, algorithms[0]);
                if (algChoice < 0) return;

                int animateOption = JOptionPane.showConfirmDialog(null, "Animate step-by-step?", "Animation", JOptionPane.YES_NO_OPTION);
                boolean animate = (animateOption == JOptionPane.YES_OPTION);

                int delayMs = 100;
                if (animate) {
                    String input = (String) JOptionPane.showInputDialog(null, "Delay between steps (ms):", "100");
                    try {
                        if (input != null && !input.trim().isEmpty()) delayMs = Integer.parseInt(input.trim());
                    } catch (NumberFormatException ignored) {}
                }

                // Creamos una copia final para usarla dentro del hilo/lambda
                final int delayMsFinal = delayMs;

                JFrame frame = new JFrame("Maze Pathfinding (" + algorithms[algChoice] + ")");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                MazePanel panel = new MazePanel(maze);
                frame.add(panel, BorderLayout.CENTER);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                // Ejecutar búsqueda:
                if (animate) {
                    // En hilo aparte para no bloquear la EDT
                    new Thread(() -> {
                        long t0 = System.nanoTime();
                        List<Node> path;
                        if (algChoice == 0) {
                            path = AnimatedSearcher.bfsAnimate(maze, maze.start, maze.goal, panel, delayMsFinal);
                        } else {
                            path = AnimatedSearcher.dfsAnimate(maze, maze.start, maze.goal, panel, delayMsFinal);
                        }
                        long t1 = System.nanoTime();
                        double elapsed = (t1 - t0) / 1_000_000.0;
                        int length = path == null ? 0 : path.size();
                        SwingUtilities.invokeLater(() ->
                                JOptionPane.showMessageDialog(frame, "Path length: " + length + "\nTime: " + elapsed + " ms")
                        );
                    }, "SearchThread").start();
                } else {
                    // no animación: usar implementaciones directas
                    SearchAlgorithm alg = (algChoice == 0) ? new BFS() : new DFS();
                    long t0 = System.nanoTime();
                    List<Node> path = alg.findPath(maze, maze.start, maze.goal);
                    long t1 = System.nanoTime();
                    double elapsed = (t1 - t0) / 1_000_000.0;
                    panel.setPath(path);
                    JOptionPane.showMessageDialog(frame, "Path length: " + (path==null?0:path.size()) + "\nTime: " + elapsed + " ms");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        });
    }
}
