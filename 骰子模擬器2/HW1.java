import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class HW3_2 extends JFrame {
    private JLabel lblStatus;         // 顯示總輪數、總骰子數、總和與平均
    private JLabel lblDiceDisplay;    // 顯示最後一輪的多顆骰子結果與該輪總和
    private JTextField txtRounds;     // 設定模擬輪數（要擲幾次）
    private JTextField txtDicePerRoll;// 設定每輪同時擲幾顆骰子
    private JButton btnRoll;          // 擲骰子按鈕
    
    // 統計 1~6 點各自出現的總次數
    private JLabel[] lblStats = new JLabel[6];
    
    // 極端值與統計標籤面板的 Label
    private JLabel lblMaxMin;
    private JLabel lblLuckyUnlucky;

    public HW3_2() {
        // 設定視窗標題、尺寸、關閉模式與置中
        setTitle("進階骰子模擬器");
        setSize(500, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. 上方：顯示整體狀態
        lblStatus = new JLabel("請設定參數後點擊「擲骰子」", SwingConstants.CENTER);
        lblStatus.setFont(new Font("SansSerif", Font.BOLD, 13));
        add(lblStatus, BorderLayout.NORTH);

        // 2. 中央：顯示最後一輪的骰子陣列與總和
        lblDiceDisplay = new JLabel("準備就緒", SwingConstants.CENTER);
        lblDiceDisplay.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(lblDiceDisplay, BorderLayout.CENTER);

        // 3. 下方綜合控制與統計面板
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

        // 參數設定與按鈕面板
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("模擬輪數:"));
        txtRounds = new JTextField("10", 4);
        inputPanel.add(txtRounds);

        inputPanel.add(new JLabel("每輪骰子數:"));
        txtDicePerRoll = new JTextField("1", 4);
        inputPanel.add(txtDicePerRoll);

        btnRoll = new JButton("擲骰子");
        inputPanel.add(btnRoll);
        bottomPanel.add(inputPanel);

        // 1~6 點個別出現次數統計面板
        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        statsPanel.setBorder(BorderFactory.createTitledBorder("1~6 點總出現次數統計"));
        for (int i = 0; i < 6; i++) {
            lblStats[i] = new JLabel((i + 1) + " 點: 0 次", SwingConstants.CENTER);
            statsPanel.add(lblStats[i]);
        }
        bottomPanel.add(statsPanel);

        // 極端值與統計標籤面板
        JPanel tagPanel = new JPanel(new GridLayout(2, 1, 2, 2));
        tagPanel.setBorder(BorderFactory.createTitledBorder("進階統計標籤"));
        
        lblMaxMin = new JLabel("最大單顆點數: - | 最小單顆點數: -", SwingConstants.CENTER);
        lblLuckyUnlucky = new JLabel("幸運之星: - | 衰神: -", SwingConstants.CENTER);
        tagPanel.add(lblMaxMin);
        tagPanel.add(lblLuckyUnlucky);
        
        bottomPanel.add(tagPanel);

        add(bottomPanel, BorderLayout.SOUTH);

        // 4. 按鈕事件監聽器
        btnRoll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simulateDice();
            }
        });
    }

    private void simulateDice() {
        int rounds, dicePerRoll;
        try {
            rounds = Integer.parseInt(txtRounds.getText().trim());
            dicePerRoll = Integer.parseInt(txtDicePerRoll.getText().trim());
            if (rounds <= 0 || dicePerRoll <= 0) {
                JOptionPane.showMessageDialog(this, "輪數與每輪骰子數必須大於 0！", "錯誤", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "請輸入有效的整數！", "錯誤", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Random random = new Random();
        long totalSum = 0;
        long totalDiceCount = (long) rounds * dicePerRoll;
        int[] counts = new int[6]; // 記錄 1~6 各自總次數
        int globalMax = 1;
        int globalMin = 6;
        
        int[] lastRollResults = new int[dicePerRoll];

        // 進行多輪與多顆骰子模擬
        for (int r = 0; r < rounds; r++) {
            for (int d = 0; d < dicePerRoll; d++) {
                int val = random.nextInt(6) + 1;
                totalSum += val;
                counts[val - 1]++;
                
                if (val > globalMax) globalMax = val;
                if (val < globalMin) globalMin = val;
                
                // 記錄最後一輪的結果以便畫面展示
                if (r == rounds - 1) {
                    lastRollResults[d] = val;
                }
            }
        }

        double average = (double) totalSum / totalDiceCount;

        // 計算「幸運之星」（出現最多次的點數）與「衰神」（出現最少次的點數）
        int maxCount = -1;
        int minCount = Integer.MAX_VALUE;
        List<Integer> luckyStars = new ArrayList<>();
        List<Integer> unluckyStars = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            if (counts[i] > maxCount) {
                maxCount = counts[i];
                luckyStars.clear();
                luckyStars.add(i + 1);
            } else if (counts[i] == maxCount) {
                luckyStars.add(i + 1);
            }

            if (counts[i] < minCount) {
                minCount = counts[i];
                unluckyStars.clear();
                unluckyStars.add(i + 1);
            } else if (counts[i] == minCount) {
                unluckyStars.add(i + 1);
            }
        }

        // 1. 更新上方狀態
        lblStatus.setText(String.format("模擬 %d 輪 | 總骰數: %d | 總和: %d | 平均: %.2f", rounds, totalDiceCount, totalSum, average));

        // 2. 更新中央最後一輪顯示
        StringBuilder sb = new StringBuilder("最後一輪結果: [");
        int lastRoundSum = 0;
        for (int i = 0; i < lastRollResults.length; i++) {
            sb.append(lastRollResults[i]);
            lastRoundSum += lastRollResults[i];
            if (i < lastRollResults.length - 1) sb.append(", ");
        }
        sb.append("] (總和: ").append(lastRoundSum).append(")");
        lblDiceDisplay.setText(sb.toString());

        // 3. 更新下方 1~6 點統計
        for (int i = 0; i < 6; i++) {
            lblStats[i].setText((i + 1) + " 點: " + counts[i] + " 次");
        }

        // 4. 更新極端值與統計標籤
        lblMaxMin.setText(String.format("最大單顆點數: %d 點 | 最小單顆點數: %d 點", globalMax, globalMin));
        lblLuckyUnlucky.setText(String.format("幸運之星: %s (各 %d 次) | 衰神: %s (各 %d 次)", 
            luckyStars.toString(), maxCount, unluckyStars.toString(), minCount));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new HW3_2().setVisible(true);
            }
        });
    }
}
