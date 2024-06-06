package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

// khởi tạo cửa sửa của game
public class GameWindow {
    private final JFrame jFrame;

    public GameWindow(GamePanel gamePanel){
        // khởi tạo màn hình
        jFrame = new JFrame();
        // đặt tiêu đề cho màn hình
        jFrame.setTitle("Game Java");
        // đặt button close cho màn hình
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // thêm nội dung cho màn hình
        jFrame.add(gamePanel);
        // cho phép thay đổi kích thước màn hình : false
        jFrame.setResizable(false);
        // đặt màn hình tại ví trí trung tâm khi được khởi tạo
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        jFrame.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                gamePanel.getGame().windowFocusLost();
            }

            @Override
            public void windowLostFocus(WindowEvent e) {

            }
        });
        // cho phép hiển thị màn hình

        jFrame.setVisible(true);
        jFrame.pack();

    }
}
