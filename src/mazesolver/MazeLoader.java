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
/**
 * Lee el formato del enunciado:
 * - Líneas con '|' y '-' que representan muros entre celdas.
 * - Las dos últimas líneas son "startRow,startCol" y "goalRow,goalCol".
 *
 * Construye Maze con horWalls y verWalls correctamente.
 */
public class MazeLoader {

     public static Maze loadFromFile(String filename) throws IOException {
        List<String> raw = Files.readAllLines(Paths.get(filename));
        if (raw.size() < 3) throw new IllegalArgumentException("Archivo demasiado corto");

        // coordenadas (últimas dos líneas)
        String[] startCoords = raw.get(raw.size() - 2).split(",");
        String[] goalCoords = raw.get(raw.size() - 1).split(",");
        int startR = Integer.parseInt(startCoords[0].trim());
        int startC = Integer.parseInt(startCoords[1].trim());
        int goalR = Integer.parseInt(goalCoords[0].trim());
        int goalC = Integer.parseInt(goalCoords[1].trim());

        // líneas del dibujo
        List<String> lines = new ArrayList<>(raw.subList(0, raw.size() - 2));

        // ancho máximo para normalizar
        int textRows = lines.size();
        int textCols = 0;
        for (String s : lines) if (s.length() > textCols) textCols = s.length();
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).length() < textCols) {
                StringBuilder sb = new StringBuilder(lines.get(i));
                while (sb.length() < textCols) sb.append(' ');
                lines.set(i, sb.toString());
            }
        }

        // celdas lógicas
        int cellRows = (textRows + 1) / 2;
        int cellCols = (textCols + 1) / 2;
        Maze maze = new Maze(cellRows, cellCols);

        // parsear caracteres y rellenar horWalls / verWalls
        for (int tr = 0; tr < textRows; tr++) {
            String line = lines.get(tr);
            for (int tc = 0; tc < textCols; tc++) {
                char ch = line.charAt(tc);
                if (ch == '|') {
                    // pared vertical: ubicación entre celdas
                    // columna de celda aproximadamente tc/2
                    int cellRow = tr / 2;
                    int cellCol = tc / 2;
                    // verWalls tiene cols+1 columnas; ubicamos la pared en indice cellCol (a la izquierda de la celda)
                    int idxRow = Math.max(0, Math.min(cellRow, cellRows - 1));
                    int idxCol = Math.max(0, Math.min(cellCol + 0, cellCols)); // puede mapear a col 0..cols
                    // si tc corresponde al primer carácter (borde izquierdo) cellCol==0 -> verWalls[*][0]
                    if (idxRow >= 0 && idxRow < maze.rows && idxCol >= 0 && idxCol <= maze.cols) {
                        maze.verWalls[idxRow][idxCol] = true;
                    }
                } else if (ch == '-') {
                    // pared horizontal
                    int cellRowBetween = tr / 2 + 1; // pared entre filas cellRowBetween-1 y cellRowBetween
                    int cellCol = tc / 2;
                    int idxR = Math.max(0, Math.min(cellRowBetween, cellRows)); // 0..rows
                    int idxC = Math.max(0, Math.min(cellCol, cellCols - 1));
                    if (idxR >= 0 && idxR <= maze.rows && idxC >= 0 && idxC < maze.cols) {
                        maze.horWalls[idxR][idxC] = true;
                    }
                }
            }
        }

        maze.start = new Node(startR, startC, null);
        maze.goal = new Node(goalR, goalC, null);
        return maze;
    }
}
