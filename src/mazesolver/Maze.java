/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

package mazesolver;
public class Maze {
    // Size of the maze
    public final int rows, cols;
    // horWalls[r][c] = true if there is a horizontal wall between row r-1 and r in column c
    // dimensions: (rows+1) x cols
    public final boolean[][] horWalls;
    // verWalls[r][c] = true if there is a vertical wall between column c-1 and c in row r
    // dimensions: rows x (cols+1)
    public final boolean[][] verWalls;

    // Start and end positions in the maze
    public Node start, goal;

    // Create a new empty maze of given size
    public Maze(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        // Create arrays to store the walls
        this.horWalls = new boolean[rows + 1][cols];
        this.verWalls = new boolean[rows][cols + 1];
    }

    // Check if a position (r,c) is inside the maze
    public boolean inBounds(int r, int c) {
        return r >= 0 && c >= 0 && r < rows && c < cols;
    }

    /**
     * Check if we can move from position (r,c) to position (nr,nc).
     * Returns false if:
     * - The new position is outside the maze
     * - There is a wall between the positions
     * - The positions are not next to each other
     */
    public boolean canMove(int r, int c, int nr, int nc) {
        if (!inBounds(nr, nc)) return false;
        if (nr == r - 1 && nc == c) {
            // Move up: check horizontal wall between r-1 and r
            return !horWalls[r][c];
        } else if (nr == r + 1 && nc == c) {
            // Move down: check horizontal wall between r and r+1
            return !horWalls[r + 1][c];
        } else if (nr == r && nc == c - 1) {
            // Move left: check vertical wall between c-1 and c
            return !verWalls[r][c];
        } else if (nr == r && nc == c + 1) {
            // Move right: check vertical wall between c and c+1
            return !verWalls[r][c + 1];
        }
        return false; // Positions are not next to each other
    }
}
