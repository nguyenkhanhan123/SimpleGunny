package test;

import javax.swing.*;
import java.awt.*;

public class HealthBar extends JComponent {
    private int health; // Giá trị máu hiện tại
    private final int maxHealth; // Giá trị máu tối đa
    private final Color foregroundColor; // Màu thanh máu
    private final Color backgroundColor; // Màu nền thanh máu
    private final Color borderColor; // Màu viền thanh máu

    public HealthBar(int maxHealth, Color foregroundColor, Color backgroundColor, Color borderColor) {
        this.maxHealth = maxHealth;
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
        this.health = maxHealth;
    }

    public void setHealth(int health) {
        this.health = health;
        repaint(); // Vẽ lại khi giá trị máu thay đổi
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();
        int healthWidth = (int) ((health / (double) maxHealth) * width);

        Graphics2D g2d = (Graphics2D) g;
        // Vẽ nền thanh máu
        g2d.setColor(backgroundColor);
        g2d.fillRect(0, 0, width, height);

        // Vẽ thanh máu
        g2d.setColor(foregroundColor);
        g2d.fillRect(0, 0, healthWidth, height);

        // Vẽ viền thanh máu
        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(3)); // Đặt độ dày của viền
        g2d.drawRect(0, 0, width - 1, height - 1); // Vẽ viền bao quanh thanh máu
    }
}
