/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mazesolver;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 *
 * @author elena
 */
public class MazeLoader {

     public static Maze loadFromFile(String filename) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filename));

        // --- 1️⃣ Leer coordenadas de inicio y fin ---
        String[] startCoords = lines.get(lines.size() - 2).split(",");
        String[] goalCoords = lines.get(lines.size() - 1).split(",");
        int startRow = Integer.parseInt(startCoords[0].trim());
        int startCol = Integer.parseInt(startCoords[1].trim());
        int goalRow = Integer.parseInt(goalCoords[0].trim());
        int goalCol = Integer.parseInt(goalCoords[1].trim());

        // --- 2️⃣ Quitar esas dos líneas del texto ---
        lines = lines.subList(0, lines.size() - 2);

        // --- 3️⃣ Convertir el dibujo del laberinto en una cuadrícula de celdas ---
        // Cada celda está entre los muros, así que el número de filas/columnas de celdas
        // es (num_lineas + 1)/2  y (ancho + 1)/2  respectivamente.
        int textRows = lines.size();
        int textCols = lines.get(0).length();
        int cellRows = (textRows + 1) / 2;
        int cellCols = (textCols + 1) / 2;

        Maze maze = new Maze(cellRows, cellCols);

        // Inicializamos todo libre (sin muros)
        for (int r = 0; r < cellRows; r++)
            Arrays.fill(maze.walls[r], false);

        // --- 4️⃣ Analizar el texto ---
        // Un muro horizontal "-" bloquea el paso entre (r, c) y (r-1, c)
        // Un muro vertical "|" bloquea el paso entre (r, c) y (r, c-1)
        boolean[][] blocked = new boolean[cellRows][cellCols];

        for (int r = 0; r < textRows; r++) {
            String line = lines.get(r);
            for (int c = 0; c < Math.min(line.length(), textCols); c++) {
                char ch = line.charAt(c);

                if (ch == '-') {
                    // Es una pared horizontal
                    int cellRow = r / 2;
                    int cellCol = c / 2;
                    if (cellRow < cellRows && cellRow > 0)
                        blocked[cellRow][cellCol] = true;
                } else if (ch == '|') {
                    // Es una pared vertical
                    int cellRow = r / 2;
                    int cellCol = c / 2;
                    if (cellCol < cellCols && cellCol > 0)
                        blocked[cellRow][cellCol] = true;
                }
            }
        }

        // --- 5️⃣ Transferir a la estructura de muros del Maze ---
        for (int r = 0; r < cellRows; r++) {
            for (int c = 0; c < cellCols; c++) {
                maze.walls[r][c] = blocked[r][c];
            }
        }

        // --- 6️⃣ Asignar puntos de entrada y salida ---
        maze.start = new Node(startRow, startCol, null);
        maze.goal = new Node(goalRow, goalCol, null);

        return maze;
    }
}
