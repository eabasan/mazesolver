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
        // tamaño preferido: ajustar si quieres más grandes o pequeños
        int preferSize = Math.min(800, Math.max(400, 40 * Math.max(maze.rows, maze.cols)));
        setPreferredSize(new Dimension(preferSize, preferSize));
    }

    public void addVisited(Node n) { visited.add(n); repaint(); }
    public void addFrontier(Node n) { frontier.add(n); repaint(); }
    public void removeFrontier(Node n) { frontier.remove(n); repaint(); }
    public void setPath(List<Node> p) { path = new ArrayList<>(p); repaint(); }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int w = getWidth();
        int h = getHeight();
        int cellSize = Math.min(w / Math.max(maze.cols,1), h / Math.max(maze.rows,1));
        if (cellSize <= 0) cellSize = 20;

        int offsetX = (w - cellSize * maze.cols) / 2;
        int offsetY = (h - cellSize * maze.rows) / 2;

        // pintar celdas (libres = blanco, fondo = blanco)
        for (int r = 0; r < maze.rows; r++) {
            for (int c = 0; c < maze.cols; c++) {
                int x = offsetX + c * cellSize;
                int y = offsetY + r * cellSize;

                g.setColor(Color.WHITE);
                g.fillRect(x, y, cellSize, cellSize);

                g.setColor(Color.LIGHT_GRAY);
                g.drawRect(x, y, cellSize, cellSize);
            }
        }

        // pintar paredes horizontales
        g.setColor(Color.BLACK);
        for (int r = 0; r < maze.horWalls.length; r++) {
            for (int c = 0; c < maze.cols; c++) {
                if (maze.horWalls[r][c]) {
                    int x1 = offsetX + c * cellSize;
                    int y = offsetY + (r) * cellSize; // r==0 => línea superior
                    g.fillRect(x1, y - 2, cellSize, 4); // pequeño grosor para ver bien
                }
            }
        }

        // pintar paredes verticales
        for (int r = 0; r < maze.rows; r++) {
            for (int c = 0; c < maze.verWalls[0].length; c++) {
                if (maze.verWalls[r][c]) {
                    int x = offsetX + c * cellSize;
                    int y1 = offsetY + r * cellSize;
                    g.fillRect(x - 2, y1, 4, cellSize); // pequeño grosor
                }
            }
        }

        // pintar frontera
        synchronized (frontier) {
            g.setColor(new Color(173, 216, 230)); // light blue
            for (Node n : frontier) {
                if (!maze.inBounds(n.row, n.col)) continue;
                int x = offsetX + n.col * cellSize;
                int y = offsetY + n.row * cellSize;
                g.fillRect(x + 2, y + 2, cellSize - 4, cellSize - 4);
            }
        }

        // pintar visitados
        synchronized (visited) {
            g.setColor(new Color(135, 206, 235)); // sky blue
            for (Node n : visited) {
                if (!maze.inBounds(n.row, n.col)) continue;
                int x = offsetX + n.col * cellSize;
                int y = offsetY + n.row * cellSize;
                g.fillRect(x + 3, y + 3, cellSize - 6, cellSize - 6);
            }
        }

        // ruta final
        if (path != null && !path.isEmpty()) {
            g.setColor(Color.BLUE);
            for (Node n : path) {
                if (!maze.inBounds(n.row, n.col)) continue;
                int x = offsetX + n.col * cellSize;
                int y = offsetY + n.row * cellSize;
                g.fillRect(x + cellSize/4, y + cellSize/4, cellSize/2, cellSize/2);
            }
        }

        // inicio y fin encima de todo
        if (maze.start != null && maze.inBounds(maze.start.row, maze.start.col)) {
            g.setColor(Color.GREEN);
            int x = offsetX + maze.start.col * cellSize;
            int y = offsetY + maze.start.row * cellSize;
            g.fillRect(x + 2, y + 2, cellSize - 4, cellSize - 4);
        }
        if (maze.goal != null && maze.inBounds(maze.goal.row, maze.goal.col)) {
            g.setColor(Color.RED);
            int x = offsetX + maze.goal.col * cellSize;
            int y = offsetY + maze.goal.row * cellSize;
            g.fillRect(x + 2, y + 2, cellSize - 4, cellSize - 4);
        }
    }
}
