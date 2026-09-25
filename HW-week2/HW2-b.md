作業二

請實作一個「骰子模擬器」

我與AI的對話截圖：

<img width="1632" height="6014" alt="HW2的對話截圖" src="https://github.com/user-attachments/assets/ccf9bffb-b429-4346-987a-e8f645f1c4fa" />

我與AI的對話發生的問題：

1.我有記得java的檔名的限制了，因此沒有出現檔名導致的錯誤

2.我看到這項規定：開啟時置中，然後看了上課的簡報有看到絕對定位跟相對定位，因此多問了ai如果是絕對定位該怎麼寫以及相對定位該怎麼寫這樣
  我也有做過測試，兩種出來的結果都是一樣的，我學到了定位的部分
  這是絕對定位：
   1. //視窗標題「骰子模擬器」，尺寸 400x320，關閉時結束程式，開啟時置中
        setTitle("骰子模擬器");
        setSize(400, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // 使用絕對定位
        setLayout(null);
   這是相對定位：
   採用 BorderLayout 搭配 JPanel。修改方式如下：

取消絕對定位：移除 setLayout(null);。

設定版面配置：將 JFrame 的 Layout 設為 BorderLayout。

分區配置元件：

北方 (BorderLayout.NORTH) 放入 lblStatus。

中央 (BorderLayout.CENTER) 放入 lblDice。

南方 (BorderLayout.SOUTH) 放入包覆按鈕的面板（用 FlowLayout 讓按鈕置中並保留邊距）。

相對定位的程式碼修改範例片段：

Java
// 改用 BorderLayout 進行相對定位
setLayout(new BorderLayout());

// 上方狀態
lblStatus = new JLabel("已擲 0 次，總和 0，平均 0.00", SwingConstants.CENTER);
add(lblStatus, BorderLayout.NORTH);

// 中央點數
lblDice = new JLabel("-", SwingConstants.CENTER);
lblDice.setFont(new Font("SansSerif", Font.BOLD, 60));
add(lblDice, BorderLayout.CENTER);

// 下方按鈕（用 Panel 置中並保留下方間距）
btnRoll = new JButton("擲骰子");
JPanel panelSouth = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
panelSouth.add(btnRoll);
add(panelSouth, BorderLayout.SOUTH);
採用相對定位的好處是當視窗大小改變或在不同作業系統解析度下，元件會自動依據配置管理器排列，不需手動計算 setBounds 的 X, Y 座標。

執行結果：

6是呈現綠色的：
<img width="1917" height="1077" alt="image" src="https://github.com/user-attachments/assets/b85c0626-3b08-4c29-91c9-f667fa8545a4" />

其餘數字呈現黑色的：
<img width="1917" height="1077" alt="image" src="https://github.com/user-attachments/assets/3d68350a-98be-47f2-95d3-c187cc38f68a" />

1是呈現紅色的：
<img width="1917" height="1077" alt="image" src="https://github.com/user-attachments/assets/fc20e5bb-ccde-49b9-9970-634ccf788459" />


