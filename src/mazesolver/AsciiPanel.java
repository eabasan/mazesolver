/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mazesolver;

import java.awt.Font;
import java.awt.Graphics;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author elena
 */
public class AsciiPanel extends JPanel {

    private final List<String> lines;

    public AsciiPanel(List<String> lines) {
        this.lines = lines;
        setFont(new Font("Monospaced", Font.PLAIN, 14));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int y = 20;
        for (String line : lines) {
            g.drawString(line, 20, y);
            y += 15;
        }
    }
}
