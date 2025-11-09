/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
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
    // The maze to display
    private final Maze maze;
    // Keep track of cells we've already checked (in blue)
    private final Set<Node> visited = Collections.synchronizedSet(new HashSet<>());
    // Keep track of cells we need to check next (in orange)
    private final Set<Node> frontier = Collections.synchronizedSet(new HashSet<>());
    // The path from start to goal when we find it (in dark blue)
    private List<Node> path = Collections.synchronizedList(new ArrayList<>());

    public MazePanel(Maze maze) {
        this.maze = maze;
        // Set preferred size (between 400 and 800 pixels)
        int preferSize = Math.min(800, Math.max(400, 40 * Math.max(maze.rows, maze.cols)));
        setPreferredSize(new Dimension(preferSize, preferSize));
    }

    // Methods to update what we show on screen
    // Each method redraws the screen after updating
    
    // Mark a cell as visited (blue)
    public void addVisited(Node n) { visited.add(n); repaint(); }
    // Add a cell to check next (orange)
    public void addFrontier(Node n) { frontier.add(n); repaint(); }
    // Remove a cell from the frontier after checking it
    public void removeFrontier(Node n) { frontier.remove(n); repaint(); }
    // Show the final path when we find it (dark blue)
    public void setPath(List<Node> p) { path = new ArrayList<>(p); repaint(); }

    // This method is called automatically to draw the maze
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Calculate how big each cell should be
        int w = getWidth();
        int h = getHeight();
        int cellSize = Math.min(w / Math.max(maze.cols,1), h / Math.max(maze.rows,1));
        if (cellSize <= 0) cellSize = 20;

        // Center the maze in the window
        int offsetX = (w - cellSize * maze.cols) / 2;
        int offsetY = (h - cellSize * maze.rows) / 2;

        // Draw all cells as WHITE squares with light gray borders
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

        // Draw horizontal WALLS as thick black lines
        g.setColor(Color.BLACK);
        for (int r = 0; r < maze.horWalls.length; r++) {
            for (int c = 0; c < maze.cols; c++) {
                if (maze.horWalls[r][c]) {
                    int x1 = offsetX + c * cellSize;
                    int y = offsetY + (r) * cellSize;
                    // Make walls 4 pixels thick so they're easy to see
                    g.fillRect(x1, y - 2, cellSize, 4);
                }
            }
        }

        // Draw vertical WALLS as thick black lines
        for (int r = 0; r < maze.rows; r++) {
            for (int c = 0; c < maze.verWalls[0].length; c++) {
                if (maze.verWalls[r][c]) {
                    int x = offsetX + c * cellSize;
                    int y1 = offsetY + r * cellSize;
                    // Make walls 4 pixels thick
                    g.fillRect(x - 2, y1, 4, cellSize);
                }
            }
        }

        // Draw cells we need to check next (frontier) in ORANGE
        synchronized (frontier) {
            g.setColor(new Color(255, 165, 0)); // orange
            for (Node n : frontier) {
                if (!maze.inBounds(n.row, n.col)) continue;
                int x = offsetX + n.col * cellSize;
                int y = offsetY + n.row * cellSize;
                g.fillRect(x + 2, y + 2, cellSize - 4, cellSize - 4);
            }
        }

        // Draw cells we've already checked (visited) in BLUE
        synchronized (visited) {
            g.setColor(new Color(100, 149, 237)); // blue
            for (Node n : visited) {
                if (!maze.inBounds(n.row, n.col)) continue;
                int x = offsetX + n.col * cellSize;
                int y = offsetY + n.row * cellSize;
                g.fillRect(x + 3, y + 3, cellSize - 6, cellSize - 6);
            }
        }

        // Draw the FINAL path in dark BLUE (smaller squares)
        if (path != null && !path.isEmpty()) {
            g.setColor(Color.BLUE);
            for (Node n : path) {
                if (!maze.inBounds(n.row, n.col)) continue;
                int x = offsetX + n.col * cellSize;
                int y = offsetY + n.row * cellSize;
                // Make path squares smaller than the cell
                g.fillRect(x + cellSize/4, y + cellSize/4, cellSize/2, cellSize/2);
            }
        }

        // Draw START (green) and GOAL (red) positions on top of everything
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
