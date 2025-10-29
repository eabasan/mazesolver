/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mazesolver;

/**
 *
 * @author elena
 */
public class Maze {
    public int rows, cols;
    public boolean[][] walls; // true = wall, false = free
    public Node start, goal;

    public Maze(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.walls = new boolean[rows][cols];
    }

    public boolean isFree(int r, int c) {
        return r >= 0 && c >= 0 && r < rows && c < cols && !walls[r][c];
    }
}
