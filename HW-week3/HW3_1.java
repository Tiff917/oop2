import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class HW3_1 extends JFrame {
    private JComboBox<String> typeComboBox;
    private JTextField tf1, tf2;
    private JComboBox<String> cb1, cb2;
    private JButton convertButton;

    // 各類型對應的完整單位選項
    private final String[] lengthUnits = {"公尺", "公分", "英吋", "英呎"};
    private final String[] weightUnits = {"公斤", "公克", "磅", "盎司"};
    private final String[] tempUnits = {"攝氏", "華氏", "克氏"};

    // 用來避免在更新 cb2 時觸發無限迴圈或重複事件
    private boolean isUpdatingCb2 = false;

    public HW3_1() {
        // 設定視窗標題與大小 (480x280)
        setTitle("單位換算器");
        setSize(480, 280);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 讓視窗出現在畫面中央
        setLayout(new BorderLayout());

        // NORTH: JComboBox 選擇換算類型
        JPanel northPanel = new JPanel();
        northPanel.add(new JLabel("選擇換算類型："));
        typeComboBox = new JComboBox<>(new String[]{"長度", "重量", "溫度"});
        northPanel.add(typeComboBox);
        add(northPanel, BorderLayout.NORTH);

        // CENTER: 兩列，每列一個 JTextField + 一個 JComboBox
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 5, 5));

        // 第一列：來源數值與單位
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        tf1 = new JTextField(10);
        cb1 = new JComboBox<>(lengthUnits);
        row1.add(new JLabel("來源："));
        row1.add(tf1);
        row1.add(cb1);

        // 第二列：目標結果與單位 (結果顯示在第二個 JTextField)
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        tf2 = new JTextField(10);
        tf2.setEditable(false); // 設為唯讀，專門用來顯示計算結果
        cb2 = new JComboBox<>();
        row2.add(new JLabel("目標："));
        row2.add(tf2);
        row2.add(cb2);

        centerPanel.add(row1);
        centerPanel.add(row2);
        add(centerPanel, BorderLayout.CENTER);

        // SOUTH: 「換算」按鈕
        JPanel southPanel = new JPanel();
        convertButton = new JButton("換算");
        southPanel.add(convertButton);
        add(southPanel, BorderLayout.SOUTH);

        // 初始化 cb2 的選項（依據 cb1 初始預設值排除）
        updateTargetUnits();

        // 事件監聽：切換換算類型時，動態更新 cb1 與 cb2 的選項
        typeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedType = (String) typeComboBox.getSelectedItem();
                cb1.removeAllItems();
                
                String[] units = lengthUnits;
                if ("長度".equals(selectedType)) {
                    units = lengthUnits;
                } else if ("重量".equals(selectedType)) {
                    units = weightUnits;
                } else if ("溫度".equals(selectedType)) {
                    units = tempUnits;
                }

                for (String u : units) {
                    cb1.addItem(u);
                }
                updateTargetUnits();
            }
        });

        // 當 cb1 (來源單位) 改變時，動態從 cb2 移除相同的選項
        cb1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isUpdatingCb2) {
                    updateTargetUnits();
                }
            }
        });

        // 事件監聽：點擊「換算」按鈕後進行計算
        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculate();
            }
        });
    }

    // 動態更新目標單位的選單，將來源單位相同的選項從 cb2 中移除
    private void updateTargetUnits() {
        isUpdatingCb2 = true;
        String selectedSource = (String) cb1.getSelectedItem();
        String currentTarget = (String) cb2.getSelectedItem();
        
        cb2.removeAllItems();

        String selectedType = (String) typeComboBox.getSelectedItem();
        String[] units = lengthUnits;
        if ("重量".equals(selectedType)) {
            units = weightUnits;
        } else if ("溫度".equals(selectedType)) {
            units = tempUnits;
        }

        for (String u : units) {
            if (!u.equals(selectedSource)) {
                cb2.addItem(u);
            }
        }

        // 如果原本選的目標單位不在被移除的行列中，嘗試保留它
        if (currentTarget != null && !currentTarget.equals(selectedSource)) {
            cb2.setSelectedItem(currentTarget);
        }
        isUpdatingCb2 = false;
    }

    // 計算邏輯函式
    private void calculate() {
        try {
            String text = tf1.getText().trim();
            if (text.isEmpty()) {
                tf2.setText("請輸入數值");
                return;
            }

            double val = Double.parseDouble(text);
            String type = (String) typeComboBox.getSelectedItem();
            String u1 = (String) cb1.getSelectedItem();
            String u2 = (String) cb2.getSelectedItem();
            double result = 0;

            if ("長度".equals(type)) {
                double meters = val;
                if ("公分".equals(u1)) meters = val / 100.0;
                else if ("英吋".equals(u1)) meters = val * 0.0254;
                else if ("英呎".equals(u1)) meters = val * 0.3048;

                if ("公尺".equals(u2)) result = meters;
                else if ("公分".equals(u2)) result = meters * 100.0;
                else if ("英吋".equals(u2)) result = meters / 0.0254;
                else if ("英呎".equals(u2)) result = meters / 0.3048;

            } else if ("重量".equals(type)) {
                double kg = val;
                if ("公克".equals(u1)) kg = val / 1000.0;
                else if ("磅".equals(u1)) kg = val * 0.45359237;
                else if ("盎司".equals(u1)) kg = val * 0.0283495231;

                if ("公斤".equals(u2)) result = kg;
                else if ("公克".equals(u2)) result = kg * 1000.0;
                else if ("磅".equals(u2)) result = kg / 0.45359237;
                else if ("盎司".equals(u2)) result = kg / 0.0283495231;

            } else if ("溫度".equals(type)) {
                double celsius = val;
                if ("華氏".equals(u1)) celsius = (val - 32) * 5.0 / 9.0;
                else if ("克氏".equals(u1)) celsius = val - 273.15;

                if ("攝氏".equals(u2)) result = celsius;
                else if ("華氏".equals(u2)) result = celsius * 9.0 / 5.0 + 32;
                else if ("克氏".equals(u2)) result = celsius + 273.15;
            }

            tf2.setText(String.format("%.4f", result));
        } catch (NumberFormatException ex) {
            tf2.setText("格式錯誤");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new HW3_1().setVisible(true);
            }
        });
    }
}