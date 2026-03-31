package mid_project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwingApp {
    public static void main(String[] args) {
        // 確保 GUI 在事件派發執行緒 (Event Dispatch Thread) 中啟動
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    private static void createAndShowGUI() {
        // 1. 建立主視窗容器 (JFrame)
        JFrame frame = new JFrame("VS Code Swing 測試");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 關閉視窗時結束程式
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout()); // 設定佈局管理器

        // 2. 建立組件
        JLabel label = new JLabel("點擊下方按鈕試試看！", SwingConstants.CENTER);
        label.setFont(new Font("Microsoft JhengHei", Font.BOLD, 18)); // 設定中文字體

        JButton button = new JButton("按我！");
        
        // 3. 建立簡單的 OOP 事件監聽器 (Listener)
        button.addActionListener(new ActionListener() {
            private int count = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                count++;
                label.setText("按鈕已被點擊 " + count + " 次");
                System.out.println("按鈕被按下了！目前的次數：" + count);
            }
        });

        // 4. 將組件加入視窗
        frame.add(label, BorderLayout.CENTER);
        frame.add(button, BorderLayout.SOUTH);

        // 5. 讓視窗顯示在螢幕中間並啟動
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}