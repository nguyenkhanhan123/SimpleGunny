package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class User {
    private JLabel characterIcon;
    private JLabel bulletIcon;
    private JLabel ammoContainerIcon;
    private JLabel nameLabel; // JLabel cho tên nhân vật
    private HealthBar healthBar;
    private int x, y;
    private int health;
    private boolean flipHorizontally;
     private Timer bulletTimer;
    private boolean isShooting = false;

    public User(JLayeredPane layeredPane, String characterImagePath, String bulletImagePath, String ammoContainerImagePath, String name, int maxHealth, Color healthBarColor, Color healthBarBackgroundColor, Color healthBarBorderColor, int startX, int startY, boolean flipHorizontally) {
        this.flipHorizontally = flipHorizontally;
        this.health = maxHealth;
        this.x = startX;
        this.y = startY;

        // Khởi tạo biểu tượng nhân vật
        Image characterImage = loadImage(characterImagePath);
        if (flipHorizontally) {
            characterImage = flipImageHorizontally(characterImage);
        }
        characterIcon = new JLabel(new ImageIcon(characterImage));
        characterIcon.setBounds(startX, startY, 185, 175);
        layeredPane.add(characterIcon, Integer.valueOf(2));

        // Khởi tạo biểu tượng đạn
        bulletIcon = new JLabel(new ImageIcon(loadImage(bulletImagePath)));
        bulletIcon.setBounds(startX+50, startY+75, 80, 80);
        bulletIcon.setVisible(false); // Ẩn đạn khi chưa bắn
        layeredPane.add(bulletIcon, Integer.valueOf(3));

        // Khởi tạo biểu tượng đồ đựng đạn
        Image ammoContainerImage = loadImage(ammoContainerImagePath);
        if (flipHorizontally) {
            ammoContainerImage = flipImageHorizontally(ammoContainerImage);
        }
        ammoContainerIcon = new JLabel(new ImageIcon(ammoContainerImage));
        ammoContainerIcon.setBounds(startX + (flipHorizontally ? -20 : 120), startY + 60, 80, 80);
        layeredPane.add(ammoContainerIcon, Integer.valueOf(3));

        // Khởi tạo thanh máu
        healthBar = new HealthBar(maxHealth, healthBarColor, healthBarBackgroundColor, healthBarBorderColor);
        healthBar.setBounds(startX, startY + 160, 185, 10);
        layeredPane.add(healthBar, Integer.valueOf(3));

        // Khởi tạo nhãn tên
        nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Kích thước phông chữ lớn hơn
        nameLabel.setForeground(Color.BLACK); // Màu chữ
        nameLabel.setOpaque(true); // Bật nền
        nameLabel.setBackground(Color.WHITE); // Màu nền để tạo hiệu ứng viền
        nameLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // Viền trắng
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setBounds(startX, startY + 170 + 10, 185, 30); // Vị trí dưới thanh máu
        layeredPane.add(nameLabel, Integer.valueOf(3));
    }

    private void updatePosition() {
        characterIcon.setBounds(x, y, 185, 175);
        bulletIcon.setBounds(x+50, y+75, 80, 80);
        ammoContainerIcon.setBounds(x + (flipHorizontally ? -20 : 120), y + 60, 80, 80);
        healthBar.setBounds(x, y + 160, 185, 10);
        nameLabel.setBounds(x, y + 170 + 10, 185, 30); // Cập nhật vị trí nhãn tên
    }

    private Image loadImage(String path) {
        try {
            return new ImageIcon(path).getImage();
        } catch (Exception e) {
            System.err.println("Lỗi khi tải hình ảnh từ đường dẫn: " + path);
            e.printStackTrace();
            return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB); // Trả về hình ảnh trống
        }
    }

    private Image flipImageHorizontally(Image img) {
        int width = img.getWidth(null);
        int height = img.getHeight(null);

        BufferedImage flippedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = flippedImage.createGraphics();
        g2d.drawImage(img, 0, 0, width, height, width, 0, 0, height, null);
        g2d.dispose();

        return flippedImage;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = Math.max(0, Math.min(1350, x));
        updatePosition();
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
        updatePosition();
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
        healthBar.setHealth(health);
    }

    public boolean isFlipHorizontally() {
        return flipHorizontally;
    }
    
    public void setFlipHorizontally(boolean flipHorizontally) {
        this.flipHorizontally = flipHorizontally;
        ImageIcon icon = (ImageIcon) characterIcon.getIcon();
        Image flippedCharacterImage = flipImageHorizontally(icon.getImage());
        characterIcon.setIcon(new ImageIcon(flippedCharacterImage));
        
        ImageIcon ammoIcon = (ImageIcon) ammoContainerIcon.getIcon();
        Image flippedAmmoImage = flipImageHorizontally(ammoIcon.getImage());
        ammoContainerIcon.setIcon(new ImageIcon(flippedAmmoImage));
        
        updatePosition();
    }

    public JLabel getCharacterIcon() {
        return characterIcon;
    }

    public JLabel getBulletIcon() {
        return bulletIcon;
    }

    public JLabel getAmmoContainerIcon() {
        return ammoContainerIcon;
    }

    public JLabel getNameLabel() {
        return nameLabel;
    }

    public HealthBar getHealthBar() {
        return healthBar;
    }
  // Phương thức bắn đạn
    public void shoot(JLayeredPane layeredPane, User target) {
        double angleDegrees=60,power=300;
         double angle = Math.toRadians(angleDegrees); // Chuyển góc sang radian
        double vX = power * Math.cos(angle); // Vận tốc ngang
        double vY = power * Math.sin(angle); // Vận tốc dọc
        final double gravity = 100; // Gia tốc trọng trường
        if (isShooting) {
            return; // Nếu đạn đã bắn, không cho phép bắn thêm
        }

        isShooting = true; // Đánh dấu trạng thái đang bắn
        bulletIcon.setVisible(true); // Hiển thị đạn khi bắn

        if (flipHorizontally) {
               bulletTimer = new Timer(10, new ActionListener() {
            double time = 0.0; // Thời gian ban đầu
            int bulletX = x+50; // Vị trí khởi đầu của đạn
            int bulletY = y+75; 

            @Override
            public void actionPerformed(ActionEvent e) {
               int newX = (int) (bulletX + vX * time); // Tọa độ ngang
               int newY = (int) (bulletY - (vY * time - 0.5 * gravity * time * time)); // Tọa độ dọc

                // Kiểm tra xem đạn có ra khỏi màn hình không
                if (newX < 0 || newX > 1500) {
                    bulletTimer.stop(); // Dừng timer khi đạn ra ngoài màn hình
                    bulletIcon.setVisible(false); // Ẩn đạn khi nó rời khỏi màn hình
                    isShooting = false; // Cho phép bắn đạn mới
                }
                    // Nếu viên đạn chạm đất, dừng lại
                if (newY > 550) { // Giả sử y=550 là mặt đất
                     if (bulletIcon.getBounds().intersects(target.getCharacterIcon().getBounds())) {
                    System.out.println("Đạn của User1 đã trúng User2!");
                    // Giảm máu của User2 khi trúng đạn
                    target.setHealth(target.getHealth() - 10); // Ví dụ giảm 10 máu
                }
                    bulletTimer.stop();
                    bulletIcon.setVisible(false); // Ẩn đạn khi chạm đất
                    isShooting = false; // Cho phép bắn đạn mới
                } else {
                    // Cập nhật vị trí viên đạn
                    bulletIcon.setLocation(newX, newY);
                }

                time += 35 / 1000.0;
            }
        });
        }
        else{
               bulletTimer = new Timer(10, new ActionListener() {
            double time = 0.0; // Thời gian ban đầu
            int bulletX = x+50; // Vị trí khởi đầu của đạn
            int bulletY = y+75; 

            @Override
            public void actionPerformed(ActionEvent e) {
               int newX = (int) (bulletX - vX * time); // Tọa độ ngang
               int newY = (int) (bulletY - (vY * time - 0.5 * gravity * time * time)); // Tọa độ dọc

                // Kiểm tra xem đạn có ra khỏi màn hình không
                if (newX < 0 || newX > 1500) {
                    bulletTimer.stop(); // Dừng timer khi đạn ra ngoài màn hình
                    bulletIcon.setVisible(false); // Ẩn đạn khi nó rời khỏi màn hình
                    isShooting = false; // Cho phép bắn đạn mới
                }
                    // Nếu viên đạn chạm đất, dừng lại
                if (newY > 550) { // Giả sử y=550 là mặt đất
                    bulletTimer.stop();
                    bulletIcon.setVisible(false); // Ẩn đạn khi chạm đất
                    isShooting = false; // Cho phép bắn đạn mới
                } else {
                    // Cập nhật vị trí viên đạn
                    bulletIcon.setLocation(newX, newY);
                }

                time += 35 / 1000.0;
            }
        });
        }
     

        bulletTimer.start(); // Bắt đầu timer để bắn đạn
    }
    
    // Phương thức dừng bắn đạn (khi cần thiết)
    public void stopShooting() {
        if (bulletTimer != null) {
            bulletTimer.stop();
            isShooting = false;
            bulletIcon.setVisible(false); // Ẩn đạn
        }
    }
}
