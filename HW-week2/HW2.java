import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HW2 extends JFrame {
    private JLabel lblStatus;
    private JLabel lblDice;
    private JButton btnRoll;
    private int count = 0;
    private int sum = 0;

    public HW2() {
        // 1. 視窗標題「骰子模擬器」，尺寸 400x320，關閉時結束程式，開啟時置中[cite: 1]
        setTitle("骰子模擬器");
        setSize(400, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // 使用絕對定位
        setLayout(null);

        // 2. 視窗上方顯示「已擲 N 次，總和 M，平均 X.XX」[cite: 1]
        lblStatus = new JLabel("已擲 0 次，總和 0，平均 0.00", SwingConstants.CENTER);
        lblStatus.setBounds(50, 15, 300, 30);
        add(lblStatus);

        // 3. 中央有一個 JLabel 顯示目前點數，字體 60pt[cite: 1]
        lblDice = new JLabel("-", SwingConstants.CENTER);
        lblDice.setFont(new Font("SansSerif", Font.BOLD, 60));
        lblDice.setBounds(100, 65, 200, 110);
        add(lblDice);

        // 4. 下方一個「擲骰子」按鈕[cite: 1]
        btnRoll = new JButton("擲骰子");
        btnRoll.setBounds(140, 200, 120, 40);
        add(btnRoll);

        // 5. 按鈕事件處理：每按一次隨機產生 1-6 的點數並更新顯示[cite: 1]
        btnRoll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int dice = (int) (Math.random() * 6) + 1;
                count++;
                sum += dice;
                double avg = (double) sum / count;

                lblDice.setText(String.valueOf(dice));

                // 6. 點數為 6 時文字變綠色，為 1 時變紅色，其餘黑色[cite: 1]
                if (dice == 6) {
                    lblDice.setForeground(new Color(0, 140, 0)); // 綠色
                } else if (dice == 1) {
                    lblDice.setForeground(Color.RED);
                } else {
                    lblDice.setForeground(Color.BLACK);
                }

                lblStatus.setText(String.format("已擲 %d 次，總和 %d，平均 %.2f", count, sum, avg));
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new HW2().setVisible(true);
            }
        });
    }
}
