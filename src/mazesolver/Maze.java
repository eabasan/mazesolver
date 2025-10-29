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
     public final int rows, cols;
    // horWalls[r][c] = true si hay pared horizontal entre fila r-1 y r en la columna c
    // dimensiones: (rows+1) x cols
    public final boolean[][] horWalls;
    // verWalls[r][c] = true si hay pared vertical entre columna c-1 y c en la fila r
    // dimensiones: rows x (cols+1)
    public final boolean[][] verWalls;

    public Node start, goal;

    public Maze(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.horWalls = new boolean[rows + 1][cols];
        this.verWalls = new boolean[rows][cols + 1];
    }

    public boolean inBounds(int r, int c) {
        return r >= 0 && c >= 0 && r < rows && c < cols;
    }

    /**
     * Comprueba si se puede mover de (r,c) a (nr,nc) (adyacente).
     * Devuelve false si fuera de límites o si hay pared entre celdas.
     */
    public boolean canMove(int r, int c, int nr, int nc) {
        if (!inBounds(nr, nc)) return false;
        if (nr == r - 1 && nc == c) {
            // mover arriba: comprobar pared horizontal entre r-1 y r => horWalls[r][c]
            return !horWalls[r][c];
        } else if (nr == r + 1 && nc == c) {
            // mover abajo: pared entre r y r+1 => horWalls[r+1][c]
            return !horWalls[r + 1][c];
        } else if (nr == r && nc == c - 1) {
            // mover izquierda: pared vertical entre c-1 y c => verWalls[r][c]
            return !verWalls[r][c];
        } else if (nr == r && nc == c + 1) {
            // mover derecha: pared vertical entre c and c+1 => verWalls[r][c+1]
            return !verWalls[r][c + 1];
        }
        return false; // no adyacente
    }
}
