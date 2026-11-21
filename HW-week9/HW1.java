// HW1.java - 網路骰子對戰（含下注籌碼、豹子規則、排行榜、聊天室）
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import javax.swing.*;

public class HW1 extends JFrame {
    // 遊戲狀態與介面元件
    private DicePanel[] dice = new DicePanel[4]; // 0,1 玩家; 2,3 莊家
    private JLabel lblMsg = new JLabel("設定籌碼後按「擲骰子」開始", JLabel.CENTER);
    private JLabel lblScore = new JLabel("勝0 敗0 和0", JLabel.CENTER);
    private JLabel lblChips = new JLabel("目前籌碼：100", JLabel.CENTER);
    private JSpinner spinBet = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
    private JButton btnRoll = new JButton("擲骰子");
    private JButton btnRank = new JButton("排行榜");

    // 聊天室元件
    private JTextArea txtChat = new JTextArea(12, 22);
    private JTextField txtSay = new JTextField();
    private JButton btnSend = new JButton("傳送");

    // 玩家資料與網路串流
    private String playerName;
    private int win = 0, lose = 0, tie = 0;
    private int chips = 100;
    private boolean connected = true;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public HW1(String host, String name) throws IOException {
        super("網路骰子對戰 - " + name);
        this.playerName = name;

        socket = new Socket();
        socket.connect(new InetSocketAddress(host, 2525), 3000);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);

        // 連線後立即送出玩家名稱註冊
        out.println("NAME " + playerName);

        initUI();
        startListenerThread();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        // --- 左側：遊戲主畫面 ---
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 5));

        // 骰子區
        JPanel board = new JPanel(new GridLayout(2, 3, 12, 12));
        Color blue = new Color(30, 110, 200);
        Color red = new Color(210, 60, 50);

        JLabel lblP = new JLabel("玩家 (" + playerName + ")", JLabel.CENTER);
        lblP.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        JLabel lblD = new JLabel("莊家", JLabel.CENTER);
        lblD.setFont(new Font(Font.DIALOG, Font.BOLD, 14));

        board.add(lblP);
        board.add(dice[0] = new DicePanel(blue));
        board.add(dice[1] = new DicePanel(blue));
        board.add(lblD);
        board.add(dice[2] = new DicePanel(red));
        board.add(dice[3] = new DicePanel(red));

        // 狀態與下注控制區
        lblMsg.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
        lblChips.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        lblChips.setForeground(new Color(0, 128, 0));

        JPanel betPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 2));
        betPanel.add(lblChips);
        betPanel.add(new JLabel(" | 下注："));
        spinBet.setPreferredSize(new Dimension(65, 26));
        betPanel.add(spinBet);

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        btnPanel.add(btnRoll);
        btnPanel.add(btnRank);

        JPanel bottom = new JPanel(new GridLayout(4, 1, 4, 4));
        bottom.add(lblMsg);
        bottom.add(lblScore);
        bottom.add(betPanel);
        bottom.add(btnPanel);

        leftPanel.add(board, BorderLayout.CENTER);
