import javax.swing.*;
import java.awt.*;

public class HW1 extends JFrame {

    public HW1() {
        // 1. 設定視窗基本屬性
        setTitle("登入");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 確保關閉視窗時完全終止程式
        setLayout(null); // 使用絕對座標排版

        // 2. 建立帳號標籤與輸入框
        JLabel l1 = new JLabel("帳號:");
        l1.setBounds(30, 30, 50, 25);
        JTextField t1 = new JTextField();
        t1.setBounds(90, 30, 150, 25);

        // 3. 建立密碼標籤與密碼輸入框 (使用 JPasswordField 遮罩密碼)
        JLabel l2 = new JLabel("密碼:");
        l2.setBounds(30, 70, 50, 25);
        JPasswordField t2 = new JPasswordField();
        t2.setBounds(90, 70, 150, 25);

        // 4. 建立登入按鈕
        JButton btn = new JButton("登入");
        btn.setBounds(105, 110, 80, 30);

        // 5. 將所有元件加入視窗容器中
        add(l1);
        add(t1);
        add(l2);
        add(t2);
        add(btn);

        // 6. 註冊按鈕事件監聽器
        btn.addActionListener(e -> {
            // 將密碼欄位的字元陣列轉換為 String
            String password = new String(t2.getPassword());
            
            // 使用 .equals() 正確比對字串內容 (假設正確帳號為 admin，密碼為 123)
            if (t1.getText().equals("admin") && password.equals("123")) {
                System.out.println("登入成功");
            } else {
                System.out.println("帳號或密碼錯誤");
            }
        });

        // 7. 所有元件配置完畢後，再將視窗設為顯示
        setVisible(true);
    }

    public static void main(String[] args) {
        // 在 Java Swing 的事件調度執行緒 (EDT) 中安全地建立介面
        SwingUtilities.invokeLater(() -> {
            new HW1();
        });
    }
}
