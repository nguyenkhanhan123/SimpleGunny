package test;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
public class Gunny extends JFrame {
     private JLayeredPane layeredPane;
    private JLabel backgroundLabel;
    private JLabel road;
    private JLabel shootButton;
    private JLabel wind;
      private JLabel windText;
    private User player1;
     private User player2;
    

    public Gunny() {
        initComponents();
        initGame();
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents

    private void initGame() {
    setTitle("Gunny Game");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(1500, 932);
    setLayout(new BorderLayout());

    // Tạo JLayeredPane để quản lý các lớp
    layeredPane = new JLayeredPane();
    layeredPane.setPreferredSize(new Dimension(1500, 932));
    
    // Bắt sự kiện nhấn chuột trên màn hình
        layeredPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Lấy tọa độ nơi nhấn
                int x = e.getX();
                int y = e.getY();
                
                // Hiển thị tọa độ khi nhấn vào màn hình
                System.out.println("Tọa độ bạn nhấn vào: (" + x + ", " + y + ")");
                
                // Bạn có thể sử dụng tọa độ này cho các mục đích khác
            }
        });

    // Tạo JPanel cho hình nền
    ImageIcon backgroundImageIcon = new ImageIcon("D:\\back.jpg");
    Image backgroundImage = backgroundImageIcon.getImage();
    Image scaledBackgroundImage = backgroundImage.getScaledInstance(1500, 932, Image.SCALE_SMOOTH);
    backgroundLabel = new JLabel(new ImageIcon(scaledBackgroundImage));
    backgroundLabel.setBounds(0, 0, 1500, 932);
    layeredPane.add(backgroundLabel, Integer.valueOf(0));
    
    //Tạo JPanel cho thanh gió 
    wind = new JLabel(new ImageIcon("D:\\BattleOperator.png"));
    wind.setBounds(700, 20, 99, 65);
    layeredPane.add(wind, Integer.valueOf(1));
    String s="Gió: 3";
     windText = new JLabel(s);
        windText.setFont(new Font("Arial", Font.BOLD, 18)); // Kích thước phông chữ lớn hơn
        windText.setForeground(Color.BLACK); // Màu chữ
        windText.setOpaque(true); // Bật nền
        windText.setBackground(Color.WHITE); // Màu nền để tạo hiệu ứng viền
        windText.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // Viền trắng
        windText.setHorizontalAlignment(SwingConstants.CENTER);
        windText.setBounds(650, 80, 185, 30);
        layeredPane.add(windText, Integer.valueOf(3));

    // Thêm thanh đường đi
    road = new JLabel(new ImageIcon("D:\\dead.png"));
    road.setBounds(0, 100, 1500, 720);
    layeredPane.add(road, Integer.valueOf(1));

    // Tạo và thêm nhân vật và các đối tượng liên quan
    player1 = new User(layeredPane, "D:\\living101.png", "D:\\102.png","D:\\331.png","An", 100, Color.GREEN, Color.BLACK, Color.WHITE, 200, 470,true);
    player2 = new User(layeredPane, "D:\\living091.png", "D:\\102.png","D:\\391.png","Huy", 100, Color.RED, Color.BLACK, Color.WHITE, 1100, 470,false);

    // Thêm nút bắn đạn
    ImageIcon shootIcon = new ImageIcon("D:\\button_shoot.png");
    Image shootImage = shootIcon.getImage();
    Image scaledShootImage = shootImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
    shootButton = new JLabel(new ImageIcon(scaledShootImage));
    shootButton.setBounds(900, 732, 100, 100);
     shootButton.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            player1.shoot(layeredPane,player2); // Thực hiện hành động bắn đạn
        }
    });
    layeredPane.add(shootButton, Integer.valueOf(2));


    // Thêm JPanel hình nền vào JFrame
    add(layeredPane);
       setupKeyBindings();
}
    private void setupKeyBindings() {
    // Di chuyển nhân vật player1 sang phải
    layeredPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");
    layeredPane.getActionMap().put("moveRight", new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            player1.setX(player1.getX() + 10); // Di chuyển 10 pixel sang phải
            if(player1.isFlipHorizontally()==false){
                player1.setFlipHorizontally(true);
            }
        }
    });

    // Di chuyển nhân vật player1 sang trái
    layeredPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
    layeredPane.getActionMap().put("moveLeft", new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            player1.setX(player1.getX() - 10); // Di chuyển 10 pixel sang trái
             if(player1.isFlipHorizontally()==true){
                player1.setFlipHorizontally(false);
            }
        }
    });
    
      // Bắn đạn khi nhấn phím Space
    layeredPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("SPACE"), "shoot");
    layeredPane.getActionMap().put("shoot", new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            player1.shoot(layeredPane,player2); // Bắn đạn khi nhấn Space
        }
    });
}

 public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Gunny().setVisible(true);
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
