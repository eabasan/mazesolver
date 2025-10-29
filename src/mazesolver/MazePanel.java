/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mazesolver;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.*;
import javax.swing.*;

/**
 *
 * @author elena
 */
public class MazePanel extends JPanel{
    private final Maze maze;
    private final Set<Node> visited = Collections.synchronizedSet(new HashSet<>());
    private final Set<Node> frontier = Collections.synchronizedSet(new HashSet<>());
    private List<Node> path = Collections.synchronizedList(new ArrayList<>());

    public MazePanel(Maze maze) {
        this.maze = maze;
        setPreferredSize(new Dimension(600, 800));
    }

    // llamadas desde threads de búsqueda: usar invokeLater al actualizar UI
    public void addVisited(Node n) { visited.add(n); repaint(); }
    public void addFrontier(Node n) { frontier.add(n); repaint(); }
    public void removeFrontier(Node n) { frontier.remove(n); repaint(); }
    public void setPath(List<Node> p) { path = new ArrayList<>(p); repaint(); }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int cellSize = Math.min(getWidth() / Math.max(maze.cols,1), getHeight() / Math.max(maze.rows,1));
        if (cellSize <= 0) cellSize = 20;

        // dibujar celdas
        for (int r = 0; r < maze.rows; r++) {
            for (int c = 0; c < maze.cols; c++) {
                int x = c * cellSize, y = r * cellSize;
                if (maze.walls[r][c]) {
                    g.setColor(Color.BLACK);
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(x, y, cellSize, cellSize);
                g.setColor(Color.GRAY);
                g.drawRect(x, y, cellSize, cellSize);
            }
        }

        // dibujar frontera (nodos pendientes)
        synchronized (frontier) {
            g.setColor(new Color(173, 216, 230)); // light blue
            for (Node n : frontier) {
                int x = n.col * cellSize, y = n.row * cellSize;
                g.fillRect(x+1, y+1, cellSize-2, cellSize-2);
            }
        }

        // dibujar visitados
        synchronized (visited) {
            g.setColor(new Color(135, 206, 235)); // sky blue
            for (Node n : visited) {
                int x = n.col * cellSize, y = n.row * cellSize;
                g.fillRect(x+2, y+2, cellSize-4, cellSize-4);
            }
        }

        // dibujar ruta final (si existe)
        if (path != null && !path.isEmpty()) {
            g.setColor(Color.BLUE);
            for (Node n : path) {
                int x = n.col * cellSize, y = n.row * cellSize;
                g.fillRect(x + cellSize/4, y + cellSize/4, cellSize/2, cellSize/2);
            }
        }

        // inicio y fin
        if (maze.start != null) {
            g.setColor(Color.GREEN);
            int x = maze.start.col * cellSize, y = maze.start.row * cellSize;
            g.fillRect(x+1, y+1, cellSize-2, cellSize-2);
        }
        if (maze.goal != null) {
            g.setColor(Color.RED);
            int x = maze.goal.col * cellSize, y = maze.goal.row * cellSize;
            g.fillRect(x+1, y+1, cellSize-2, cellSize-2);
        }
    }
}
