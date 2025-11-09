/*
 * This class loads a maze from a text file.
 * The maze file format is:
 * - Lines of ASCII characters showing walls: | for vertical, - for horizontal
 * - Second to last line: start position (row,col)
 * - Last line: goal position (row,col)
 */
package mazesolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 *
 * @author elena
 */
public class MazeLoaderAscii {
    public static Maze loadFromFile(String filename) throws IOException {
        // Read all lines from the file
        List<String> lines = Files.readAllLines(Paths.get(filename));

        // Check if file has enough lines (maze + start + goal positions)
        if (lines.size() < 3) {
            throw new IllegalArgumentException("File is too short.");
        }

        // Read start and goal coordinates from the last two lines
        String[] startCoords = lines.get(lines.size() - 2).split(",");
        String[] goalCoords = lines.get(lines.size() - 1).split(",");

        // Convert coordinates from strings to numbers
        int startRow = Integer.parseInt(startCoords[0].trim());
        int startCol = Integer.parseInt(startCoords[1].trim());
        int goalRow = Integer.parseInt(goalCoords[0].trim());
        int goalCol = Integer.parseInt(goalCoords[1].trim());

        // Remove the last two lines (coordinates) to keep only the maze
        lines = new ArrayList<>(lines.subList(0, lines.size() - 2));

        // Make sure all lines have the same width by padding with spaces
        int textCols = lines.stream().mapToInt(String::length).max().orElse(0);
        for (int i = 0; i < lines.size(); i++) {
            String l = lines.get(i);
            if (l.length() < textCols) {
                lines.set(i, String.format("%-" + textCols + "s", l));
            }
        }

        // Calculate maze size: each cell is surrounded by walls
        // so we divide the text size by 2 (adding 1 for odd numbers)
        int rows = (lines.size() + 1) / 2;
        int cols = (textCols + 1) / 2;
        Maze maze = new Maze(rows, cols);

        // Initialize all walls to false (no walls)
        // We'll set them to true when we find them in the text
        for (int r = 0; r < maze.horWalls.length; r++)
            Arrays.fill(maze.horWalls[r], false);
        for (int r = 0; r < maze.verWalls.length; r++)
            Arrays.fill(maze.verWalls[r], false);

        // Read the ASCII text and create walls where we find - and |
        for (int r = 0; r < lines.size(); r++) {
            String line = lines.get(r);
            for (int c = 0; c < textCols; c++) {
                char ch = line.charAt(c);

                if (ch == '-') {
                    // Found a horizontal wall between cells
                    // Convert text position to cell position
                    int cellRow = (r + 1) / 2;
                    int cellCol = c / 2;
                    if (cellRow >= 0 && cellRow <= maze.rows && cellCol < maze.cols)
                        maze.horWalls[cellRow][cellCol] = true;

                } else if (ch == '|') {
                    // Found a vertical wall between cells
                    // Convert text position to cell position
                    int cellRow = r / 2;
                    int cellCol = (c + 1) / 2;
                    if (cellRow < maze.rows && cellCol >= 0 && cellCol <= maze.cols)
                        maze.verWalls[cellRow][cellCol] = true;
                }
            }
        }

        // Create start and goal nodes at the positions we read
        maze.start = new Node(startRow, startCol, null);
        maze.goal = new Node(goalRow, goalCol, null);
        return maze;
    }
}
