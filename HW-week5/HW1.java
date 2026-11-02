import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * 檔名：HW1.java（公開類別名稱必須與檔名相同）。
 * 編譯：javac -encoding UTF-8 HW1.java
 * 執行：java HW1
 *
 * 座標規則：左上角是 (0, 0)，x 向右增加，y 向下增加。
 * 畫布寬 500、高 400；可見像素的 x 為 0～499，y 為 0～399。
 * 長度單位都是像素。矩形和橢圓的 x、y 是外接矩形左上角，
 * 不是形狀中心；width、height 是寬度、高度，不是右下角座標。
 * 天空占上方320像素，草地占底部80像素，地平線位於 y=320。
 */
public class HW1 extends JPanel {
    private static final int CANVAS_WIDTH = 500;  // 畫布寬度：500 像素。
    private static final int CANVAS_HEIGHT = 400; // 畫布高度：400 像素。

    public HW1() {
        setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        // Dimension(width, height)：設定繪圖面板希望得到的寬、高。
        // setPreferredSize(Dimension)：把上述尺寸交給視窗的 pack() 使用。
        // 500×400 是實際畫布尺寸，視窗標題列及邊框會額外占用空間。
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Graphics g：Swing 傳入的畫筆，所有 g.xxx 都畫在這個面板上。
        super.paintComponent(g); // g：交給父類別清除舊畫面，避免重繪殘影。

        // ===== 1. 天空與草地：天空高320像素，草地高80像素 =====
        g.setColor(new Color(135, 206, 235));
        // Color(red, green, blue)：紅=135、綠=206、藍=235，混合成天藍色。
        // 每個色彩分量範圍為 0～255；setColor(color) 設定接下來的畫筆顏色。
        g.fillRect(0, 0, 500, 320);
        // fillRect(x, y, width, height)：左上角 (0,0)，寬500、高320。
        // 天空占 x=0～499、y=0～319。

        g.setColor(new Color(76, 175, 80));
        // Color：紅=76、綠=175、藍=80，混合成草綠色。
        g.fillRect(0, 320, 500, 80);
        // x=0：從最左側開始；y=320：從距離頂部320像素的位置往下畫。
        // width=500：鋪滿寬度；height=80：鋪滿底部80像素，直到 y=399。

        // ===== 2. 太陽：黃色正圓 + 8 條 drawLine 光芒 =====
        g.setColor(Color.YELLOW); // Color.YELLOW：黃色，套用於太陽和光芒。
        g.fillOval(385, 30, 70, 70);
        // fillOval(x, y, width, height)：外接矩形左上角 (385,30)。
        // 寬=70、高=70，兩者相同所以是正圓，半徑35，圓心 (420,65)。

        int centerX = 420;   // 太陽圓心的水平位置。
        int centerY = 65;    // 太陽圓心的垂直位置。
        int innerRadius = 43; // 光芒起點距離圓心43，與半徑35的太陽間隔8。
        int outerRadius = 60; // 光芒終點距離圓心60，每條光芒長17。
        int rayCount = 8;     // 光芒總數：剛好8條，符合至少8條的要求。

        for (int i = 0; i < rayCount; i++) {
            // i=0：第一條；i<8：只處理0～7；i++：每次增加1。
            double angle = 2.0 * Math.PI * i / rayCount;
            // 一整圈=2π弧度；乘 i/8，得到每次相隔 π/4（45度）的方向。
            // 螢幕的 y 向下增加，所以角度由向右方向開始，順時針增加。
            int x1 = centerX + (int) Math.round(innerRadius * Math.cos(angle));
            // cos(angle)：此弧度的水平比例；乘43得到起點的水平位移。
            // Math.round(value)：把位移四捨五入；(int) 轉成畫線所需的整數。
            // 加 centerX=420，把「相對圓心的位移」換成畫布上的 x 座標。
            int y1 = centerY + (int) Math.round(innerRadius * Math.sin(angle));
            // sin(angle)：垂直比例；乘43、四捨五入後加65，得到起點 y。
            int x2 = centerX + (int) Math.round(outerRadius * Math.cos(angle));
            // 使用外半徑60，計算同一方向的終點 x；其餘意義同上。
            int y2 = centerY + (int) Math.round(outerRadius * Math.sin(angle));
            // 使用外半徑60，計算同一方向的終點 y；其餘意義同上。
            g.drawLine(x1, y1, x2, y2);
            // drawLine(x1,y1,x2,y2)：從起點 (x1,y1) 畫到終點 (x2,y2)。
            // 循環完成後共有8條光芒；座標都在500×400畫布內。
        }

        // ===== 3. 雲朵：3 個互相重疊的白色 fillOval =====
        g.setColor(Color.WHITE); // 白色：接下來三個橢圓共同形成一朵雲。
        g.fillOval(60, 55, 80, 45);
        // x=60、y=55：左側雲瓣外接框左上角；寬80、高45。
        g.fillOval(100, 40, 80, 55);
        // x=100、y=40：中央雲瓣較高；寬80、高55，與左右雲瓣重疊。
        g.fillOval(140, 55, 80, 45);
        // x=140、y=55：右側雲瓣；寬80、高45，與中央雲瓣重疊。

        // ===== 4. 房屋主體：fillRect 填色 + drawRect 外框 =====
        g.setColor(new Color(255, 228, 181));
        // Color：紅255、綠228、藍181，混合成淡米黃色牆面。
        g.fillRect(150, 180, 200, 170);
        // x=150、y=180：牆面左上角；寬200、高170，房屋置中。
        // 牆面從天空區域延伸到草地上；底部位置為 y=350 附近。
        g.setColor(Color.BLACK); // 黑色：用於房屋外框。
        g.drawRect(150, 180, 200, 170);
        // drawRect(x,y,width,height)：左上角(150,180)，寬200、高170。
        // 外框右側在 x=150+200=350，底邊在 y=180+170=350。
        // drawRect 的邊線會包含右邊界和下邊界，與 fillRect 的像素規則不同。

        // ===== 5. 屋頂：只有三條 drawLine，不使用填色或多邊形 =====
        g.setColor(Color.RED); // 紅色：讓三角形屋頂輪廓與參考圖相近。
        g.drawLine(130, 180, 250, 90);
        // 起點(130,180)：左屋簷；終點(250,90)：屋頂頂點。
        g.drawLine(250, 90, 370, 180);
        // 起點(250,90)：頂點；終點(370,180)：右屋簷。
        g.drawLine(130, 180, 370, 180);
        // 起點(130,180)、終點(370,180)：水平底邊，封閉三角形。
        // 左右屋簷各比牆面多出20；三角形內部保留天空原色。

        // ===== 6. 門：fillRoundRect + 小 fillOval 門把 =====
        g.setColor(new Color(139, 69, 19));
        // Color：紅139、綠69、藍19，混合成棕色木門。
        g.fillRoundRect(225, 270, 50, 80, 16, 16);
        // fillRoundRect(x,y,width,height,arcWidth,arcHeight)：
        // x=225、y=270：門外接框左上角；width=50、height=80：門寬與門高。
        // arcWidth=16、arcHeight=16：四個角使用的圓弧寬、高（不是半徑）。
        // 兩者相等時角落近似半徑8的四分之一圓；此方法讓四個角都變圓。
        // 門底部貼近牆面底邊 y=350。
        g.setColor(Color.YELLOW); // 黃色：門把顏色。
        g.fillOval(262, 310, 7, 7);
        // x=262、y=310：門把外接框左上角；寬7、高7：小正圓。
        // 門把靠近門右側；這兩個座標不是門把圓心。

        // ===== 7. 兩扇窗戶：填色、外框、十字窗框 =====
        g.setColor(Color.WHITE); // 白色：兩扇窗戶的玻璃底色。
        g.fillRect(170, 210, 45, 50);
        // 左窗：左上角(170,210)，寬45、高50。
        g.fillRect(285, 210, 45, 50);
        // 右窗：左上角(285,210)，寬45、高50；與左窗同高、同尺寸。

        g.setColor(Color.BLACK); // 黑色：兩扇窗戶的邊框和十字窗框。
        g.drawRect(170, 210, 45, 50);
        // 左窗外框：x=170、y=210、寬45、高50；右邊界215、下邊界260。
        g.drawLine(192, 210, 192, 260);
        // 左窗垂直框：從(192,210)到(192,260)，x相同所以是垂直線。
        // 45為奇數，中央位置192.5取整數192，左右寬度相差1像素。
        g.drawLine(170, 235, 215, 235);
        // 左窗水平框：從(170,235)到(215,235)，y相同所以是水平線。
        // y=235=210+50/2，正好位於窗戶高度中間。

        g.drawRect(285, 210, 45, 50);
        // 右窗外框：x=285、y=210、寬45、高50；右邊界330、下邊界260。
        g.drawLine(307, 210, 307, 260);
        // 右窗垂直框：起點(307,210)、終點(307,260)，靠近水平中央307.5。
        g.drawLine(285, 235, 330, 235);
        // 右窗水平框：起點(285,235)、終點(330,235)，位於高度中央。

        // ===== 8. 在畫面左下角簽名 =====
        g.setColor(Color.BLACK); // 黑色：讓簽名在綠色草地上清楚可見。
        g.setFont(new Font("SansSerif", Font.PLAIN, 16));
        // Font(name,style,size)：name="SansSerif"是Java邏輯字型名稱，
        // 由系統對應可用字型；style=Font.PLAIN是一般字體；size=16是字級。
        // setFont(font)：設定接下來 drawString 使用的字型。
        g.drawString("A1123382 蕭語婕", 12, 385);
        // drawString(text,x,y)：text是要顯示的內容，請改成自己的學號與姓名。
        // x=12：文字左側離畫布左邊12像素。
        // y=385：文字「基準線」位置，不是文字頂端；距畫布底部15像素。
    }

    public static void main(String[] args) {
        // args：命令列傳入的字串陣列；本程式不需要額外執行參數。
        SwingUtilities.invokeLater(() -> {
            // invokeLater(Runnable)：把建立視窗的工作交給Swing事件處理執行緒。
            // () -> { ... }：沒有傳入參數的工作內容，確保Swing介面操作順序正確。
            JFrame frame = new JFrame("HW1 - 小房子風景畫");
            // JFrame(title)：title是視窗標題列文字，不會畫進500×400畫布內。
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            // operation=EXIT_ON_CLOSE：使用者關閉視窗時結束程式。
            frame.setContentPane(new HW1());
            // contentPane=new HW1()：把自訂繪圖面板放進視窗內容區。
            frame.pack();
            // 無參數：依面板偏好尺寸調整視窗，確保內容區正好500×400。
            frame.setResizable(false);
            // resizable=false：禁止手動改變視窗大小，維持指定的畫布尺寸。
            frame.setLocationRelativeTo(null);
            // component=null：以螢幕為基準，讓視窗置中顯示。
            frame.setVisible(true);
            // visible=true：顯示視窗，Swing隨後會呼叫paintComponent(g)繪圖。
        });
    }
}

