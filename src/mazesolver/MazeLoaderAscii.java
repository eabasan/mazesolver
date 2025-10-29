/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
        List<String> lines = Files.readAllLines(Paths.get(filename));

        if (lines.size() < 3) {
            throw new IllegalArgumentException("Archivo demasiado corto.");
        }

        // Leer coordenadas (últimas dos líneas)
        String[] startCoords = lines.get(lines.size() - 2).split(",");
        String[] goalCoords = lines.get(lines.size() - 1).split(",");

        int startRow = Integer.parseInt(startCoords[0].trim());
        int startCol = Integer.parseInt(startCoords[1].trim());
        int goalRow = Integer.parseInt(goalCoords[0].trim());
        int goalCol = Integer.parseInt(goalCoords[1].trim());

        // El resto son las líneas del laberinto
        lines = new ArrayList<>(lines.subList(0, lines.size() - 2));

        // Asegurar ancho uniforme
        int textCols = lines.stream().mapToInt(String::length).max().orElse(0);
        for (int i = 0; i < lines.size(); i++) {
            String l = lines.get(i);
            if (l.length() < textCols) {
                lines.set(i, String.format("%-" + textCols + "s", l));
            }
        }

        // Dimensiones: celdas ≈ (numLineas+1)/2 x (numColumnas+1)/2
        int rows = (lines.size() + 1) / 2;
        int cols = (textCols + 1) / 2;
        Maze maze = new Maze(rows, cols);

        // Inicializar todas las paredes
        for (int r = 0; r < maze.horWalls.length; r++)
            Arrays.fill(maze.horWalls[r], false);
        for (int r = 0; r < maze.verWalls.length; r++)
            Arrays.fill(maze.verWalls[r], false);

        // Leer el ASCII y asignar muros
        for (int r = 0; r < lines.size(); r++) {
            String line = lines.get(r);
            for (int c = 0; c < textCols; c++) {
                char ch = line.charAt(c);

                if (ch == '-') {
                    // Pared horizontal entre celdas
                    int cellRow = (r + 1) / 2;
                    int cellCol = c / 2;
                    if (cellRow >= 0 && cellRow <= maze.rows && cellCol < maze.cols)
                        maze.horWalls[cellRow][cellCol] = true;

                } else if (ch == '|') {
                    // Pared vertical entre celdas
                    int cellRow = r / 2;
                    int cellCol = (c + 1) / 2;
                    if (cellRow < maze.rows && cellCol >= 0 && cellCol <= maze.cols)
                        maze.verWalls[cellRow][cellCol] = true;
                }
            }
        }

        maze.start = new Node(startRow, startCol, null);
        maze.goal = new Node(goalRow, goalCol, null);
        return maze;
    }
}
