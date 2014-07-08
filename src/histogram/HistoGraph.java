/*      
 * To change this license header, choose License Headers in Project Properties.      
 * To change this template file, choose Tools | Templates      
 * and open the template in the editor.      
 */
            
package histogram;      
            
import java.awt.AWTException; 
import java.awt.BasicStroke;      
import java.awt.BorderLayout; 
import java.awt.Color;      
import java.awt.Component; 
import java.awt.FlowLayout; 
import java.awt.Font;     
import java.util.Random;      
import java.awt.Graphics;      
import java.awt.Graphics2D;      
import java.awt.Point; 
import java.awt.Rectangle;      
import java.awt.RenderingHints;      
import java.awt.Shape; 
import java.awt.Stroke;      
import java.awt.event.ActionEvent;      
import java.awt.event.ActionListener;      
import java.awt.event.MouseAdapter;      
import java.awt.event.MouseEvent;      
import java.awt.event.MouseListener;      
import java.awt.event.MouseMotionAdapter;      
import java.awt.event.MouseMotionListener;      
import java.awt.font.FontRenderContext; 
import java.awt.font.GlyphVector; 
import java.awt.geom.AffineTransform; 
import java.awt.geom.Point2D; 
import java.awt.image.BufferedImage; 
import java.text.AttributedCharacterIterator;      
import java.text.DecimalFormat;   
import java.util.ArrayList;      
import java.util.Calendar;      
import java.util.Dictionary;      
import java.util.HashMap;      
import java.util.Map;      
import java.util.Timer;      
import javax.swing.JButton; 
import javax.swing.JComponent;      
import javax.swing.JFrame;      
import javax.swing.JPanel;      
import javax.swing.JColorChooser; 
import javax.swing.SwingUtilities; 
import javax.imageio.ImageIO; 
import java.awt.Robot; 
import java.io.File; 
import java.io.IOException; 
import java.math.BigInteger; 
import java.util.logging.Level; 
import java.util.logging.Logger; 
  
            
public class HistoGraph extends JFrame      
{        
    public JFrame frame; 
      
    public HistoGraph(ArrayList<Bar> bars, double average, Color c, String title, String xAxis, String yAxis)      
    {      
        frame = new JFrame(); 
        frame.setTitle("Graph");      
        frame.add(new HistoDraw(bars, average, c, title, xAxis, yAxis, frame));      
        frame.setSize(1750,1000);      
        frame.setBackground(Color.DARK_GRAY);      
        frame.setLocationRelativeTo(null);      
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);      
        frame.setVisible(true);      
    }    
    public JFrame getFrame() 
    { 
        return frame; 
    } 
}      
class HistoDraw extends JPanel   
{      
    public ArrayList<Bar> bars;      
    ArrayList<Rectangle> rects = new ArrayList();      
    Map<Rectangle, Bar> dict = new HashMap();      
    public boolean displayTooltip = false;      
    JPanel test = new JPanel();     
      
    int y_hash_count;      
    Random rand = new Random();      
    JFrame frame;     
    float y = rand.nextFloat();      
    float s = rand.nextFloat();      
    float t = rand.nextFloat();      
    Graphics2D g2;      
    boolean tooltip = false;      
    int mouseX;      
    int mouseY;      
    Rectangle toBeChanged = new Rectangle();      
    Bar barToShow = new Bar();      
    double maxToTen;     
    double max;     
    double lengthOfYBar;     
    double lengthOfXBar;  
    double lengthOfXMGap; 
    double lengthOfBars; 
    double tempX; 
    double totalAvg;    
    double maxOverall;  
    int xToBeDrawn;    
    int yToBeDrawn;    
    int xToBeWidth;    
    int yToBeHeight;  
    int maxSize;  
    Color cur;    
    Color picked; 
    String title, xAxis, yAxis; 
          
    Rectangle r2 = new Rectangle();      
                
    public Color randomColor = new Color(y, s, t);      
            
    public HistoDraw(ArrayList<Bar> bars, double average, Color c, String title, String xAxis, String yAxis, final JFrame frame)      
    {      
        this.title = title; 
        this.frame = frame; 
        this.yAxis = yAxis; 
        this.xAxis = xAxis; 
        this.bars = bars;      
        this.totalAvg = average;    
        MouseMotionListener myListener = new MouseListener();      
        this.addMouseMotionListener(myListener);      
        picked = c; 
          
        for (int i = 0; i < bars.size(); i++)     
        {     
            if ((bars.get(i).height) > max)     
                max = bars.get(i).height;   
        }         
        for (int i = 0; i < 9; i++)     
        {     
            if (max % 10 == 0)     
                break;     
            else
                max++;                  
        }        
        this.maxToTen = max; 
        JButton saveMe = new JButton(); 
        saveMe.setText("SAVE"); 
        saveMe.addActionListener(new ActionListener()  
        {   @Override
            public void actionPerformed(ActionEvent e) 
            { 
            try { 
  
                File file = new File("histogram.png"); 
                File file1 = null; 
                boolean fileExists = false; 
                int i = 1;  
                  
                if (file.exists()) 
                { 
                    fileExists = true; 
                }  
                else
                { 
                    file1 = file; 
                }     
                while (fileExists) 
                { 
                    file1 = new File("histogram" + i + ".png"); 
                    if (file1.exists()) 
                    { 
                          i++; 
                    }   
                    else
                    { 
                        i = 1; 
                        fileExists = false; 
                    } 
                }     
     
                BufferedImage img = getScreenShot(frame.getContentPane()); 
                ImageIO.write(img, "png", file1); 
                  
            } 
            catch (IOException ex)  
            { 
                System.out.println(ex); 
            } 
            }     
                      
        }); 
        this.setLayout(new FlowLayout(FlowLayout.RIGHT)); 
        this.add(saveMe, BorderLayout.SOUTH); 
    } 
    public BufferedImage getScreenShot(Component component) 
    { 
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), 
                        BufferedImage.TYPE_INT_RGB); 
        paintComponent(image.getGraphics()); 
        return image; 
    }         
    @Override
    protected void paintComponent(Graphics g)      
    {              
        rects.clear();    
        int width = getWidth();      
        int height = getHeight();      
        int borderGap = (int) (width * .08);      
        int j = 0;      
                            
        super.paintComponent(g);      
          
        g2 = (Graphics2D)g;      
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,       
                RenderingHints.VALUE_ANTIALIAS_ON);      
       
        g2.setColor(Color.BLACK);      
        g2.drawLine(borderGap, (height - borderGap), borderGap, borderGap); // Y axis    
        lengthOfYBar = (height - borderGap) - borderGap;         
        g2.drawLine(borderGap, (height - borderGap), width - borderGap, height -       
                borderGap); // X axis      
                  
        lengthOfXBar = (width - borderGap) - borderGap;     
                    
        for (int i = 0; i < bars.size(); i++)      
        {      
            Bar b = bars.get(i);      
            if (b.classNums.size() > j)      
            {      
                j = b.classNums.size();      
            }          
        }       
  
        double fontSize = (borderGap / 7); 
   
        g2.setFont(new Font("TimesRoman", Font.BOLD, (int)fontSize));       
            
        g2.drawString(title, (int) ((width / 2) - (title.length() * (fontSize / 4))), borderGap / 3); 
          
        g2.setFont(new Font("TimesRoman", Font.PLAIN, (int)fontSize));   
        y_hash_count = 10;      
                    
        /* Creates the hash marks for the y axis      
        *       
        */
        double m = maxToTen / 10;     
        double k = m;     
        double multiple = maxToTen;     
        for (int i = 0; i < y_hash_count; i++)      
        {      
            double x0 = (int) (borderGap - (width * .015));      
            double x1 = (int) (borderGap + (width * .015));      
            double y0 = (borderGap) + ((lengthOfYBar/ 10) * i);     
            double y1 = y0;      
                
            int x2,x3,y4,y5;     
                      
            x2 = (int)x0;     
            x3 = (int)x1;     
            y4 = (int)y0;     
            y5 = (int)y1;     
            g2.drawLine(x2, y4, x3, y5);         
            g2.setFont(new Font("TimesRoman", Font.PLAIN, (int) ((int)fontSize * 0.85)));    
            DecimalFormat format = new DecimalFormat("###"); 
            String numm = format.format(multiple);   
            if (maxToTen > 999) 
            { 
                g2.drawString(numm, (int) ((borderGap - (width * .028)) -      
                    (width * .0185)), (int) (((int) ((borderGap) + ((lengthOfYBar/ 10) * i)))    
                           + (borderGap * .05)));  
            }    
            else
            { 
                g2.drawString(numm, (int) ((borderGap - (width * .014)) -      
                    (width * .0185)), (int) (((int) ((borderGap) + ((lengthOfYBar/ 10) * i)))    
                           + (borderGap * .05)));  
            }     
                 
            multiple = multiple - k;     
        }         
        g2.drawString("0", (int) ((borderGap - (width * .015))), (height - borderGap));     
            
        // Draws Bars     
        double x0, x1 = 0, y0, y1;     
        double h;     
        for (int i = 0; i < bars.size(); i++)     
        {         
                             
            int gap = (int) (width * .03);  
            lengthOfXMGap = lengthOfXBar - (gap + gap); 
            if (i == 0)    
            {    
                x0 = gap + borderGap;    
            }     
            else
            {    
                x0 = (gap + borderGap) + ((lengthOfXMGap / (bars.size()))   
                        * i);    
            }        
            x1 = (lengthOfXMGap / (bars.size())); 
            h = ((height - borderGap) - (lengthOfYBar * (bars.get(i).height / maxToTen)));    
                          
            y1 = (height - borderGap) - h;    
      
            y0 = (int)h;    
            Rectangle r = new Rectangle();     
                    
            int x2,x3,y4,y5;    
                    
            x2 = (int)x0;    
            x3 = (int)x1;    
            y4 = (int)y0;    
            y5 = (int)y1;    
                    
            r.x = x2;     
            r.y = y4;     
            r.width = x3;     
            r.height = y5;     
                           
            rects.add(r);     
            dict.put(r, bars.get(i));     
            Stroke oldstroke = g2.getStroke();     
            g2.setStroke(new BasicStroke(20));     
            g2.setColor(picked);     
            if (bars.get(i).height != 0) 
            {     
                g2.draw3DRect(x2, y4, x3, y5, true);     
                g2.fill3DRect(x2, y4, x3, y5, true); 
            }     
                g2.setStroke(oldstroke);                      
        }   
        
        // X axis      
                    
        Bar b = bars.get(0);      
        boolean goHere = false;      
        int num = width / (bars.size() + 1);      
                    
        for (int i = 0; i < bars.size() + 1; i++)     
        {     
            Color curr = g2.getColor();  
                
            double smallest, largest;    
                    
            int gap = (int) (width * .03);    
  
            g2.setColor(Color.BLACK);  
                  
            if (i < bars.size())  
            {      
                double barMin; 
                double barMax = bars.get(i).bin;  
                double avgLine;  
                     
                if (i > 0) 
                { 
                    barMin = bars.get(i - 1).bin; 
                    if (totalAvg > bars.get(i - 1).bin && totalAvg < bars.get(i).bin)  
                    {  
                        double diff = barMax - barMin;  
                        g2.setColor(Color.WHITE);                                      
                        double avgX = (gap + borderGap) + ((lengthOfXMGap / (bars.size()))   
                        * i); 
                        double avgY = (gap + borderGap) + ((lengthOfXMGap / (bars.size()))   
                        * (i + 1)); 
                        avgLine = avgX + (((totalAvg - barMin) / (barMax - barMin)) * (avgY - avgX)); 
                        for (int jk = 0; jk < 10; jk++) 
                        { 
                            g2.drawLine((int) avgLine + (jk - 5), (int) (borderGap * 0.93),   
                                (int) avgLine + (jk - 5), height - borderGap);  
                        } 
                        g2.setColor(Color.RED); 
                        for (int jk = 0; jk < 8; jk++) 
                        { 
                            g2.drawLine((int) avgLine + (jk - 4), (int) (borderGap * 0.93),   
                            (int) avgLine + (jk - 4), height - borderGap);  
                        } 
                        DecimalFormat format = new DecimalFormat("0.00"); 
                        if (totalAvg > 9999) 
                        { 
                            double newAvg; 
                            if (totalAvg > 999999) 
                            { 
                                if (totalAvg > 999999999) 
                                { 
                                    newAvg = totalAvg / 1000000000; 
                                    String st = format.format(newAvg); 
                                    //st = st + "B"; 
                                    if (st.length() > 6) 
                                    { 
                                        double reallyBig = Double.parseDouble(st); 
                                        reallyBig = reallyBig / 1000; 
                                        st = format.format(reallyBig); 
                                        st = st + "T"; 
                                        g2.drawString(st, (int) (avgLine - (width * .013)),  
                                        (int)(borderGap * 0.85));       
                                    }  
                                    else
                                    {
                                        st = st + "B";
                                        g2.drawString(st, (int) (avgLine - (width * .013)),  
                                            (int)(borderGap * 0.85));
                                    }  
                                }  
                                else
                                {     
                                    newAvg = totalAvg / 1000000; 
                                    String st = format.format(newAvg); 
                                    st = st + "M"; 
                                    g2.drawString(st, (int) (avgLine - (width * .013)),  
                                        (int)(borderGap * 0.85)); 
                                } 
                            }     
                            else
                            { 
                                newAvg = totalAvg / 1000; 
                                String st = format.format(newAvg); 
                                st = st + "K"; 
                                g2.drawString(st, (int) (avgLine - (width * .013)),  
                                    (int)(borderGap * 0.85)); 
                            }     
                        } 
                        else
                        {    
                            double newAvg = totalAvg; 
                            String st = format.format(totalAvg); 
                            g2.drawString(st, (int) (avgLine - (width * .013)),  
                                    (int)(borderGap * 0.85)); 
                        }    
                    } 
                } 
                else
                { 
                    barMin = 0; 
                    if (totalAvg < bars.get(i).bin && totalAvg > 0)  
                    {  
                        double diff = barMax - barMin;  
                        g2.setColor(Color.WHITE);                                      
  
                        double avgX = (gap + borderGap) + ((lengthOfXMGap / (bars.size()))   
                        * i); 
  
                        double avgY = (gap + borderGap) + ((lengthOfXMGap / (bars.size()))   
                        * (i + 1)); 
  
                        avgLine = avgX + (((totalAvg - barMin) / (barMax - barMin)) * (avgY - avgX)); 
                        for (int jk = 0; jk < 10; jk++) 
                        { 
                            g2.drawLine((int) avgLine + (jk - 5), (int) (borderGap * 0.93),   
                                (int) avgLine + (jk - 5), height - borderGap);  
                        } 
                        g2.setColor(Color.RED); 
                        for (int jk = 0; jk < 8; jk++) 
                        { 
                            g2.drawLine((int) avgLine + (jk - 4), (int) (borderGap * 0.93),   
                            (int) avgLine + (jk - 4), height - borderGap);  
                        } 
                        DecimalFormat format = new DecimalFormat("0.00"); 
                        if (totalAvg > 9999) 
                        { 
                            double newAvg; 
                            if (totalAvg > 999999) 
                            { 
                                if (totalAvg > 999999999) 
                                { 
                                    newAvg = totalAvg / 1000000000; 
                                    String st = format.format(newAvg); 
                                    //st = st + "B"; 
                                    if (st.length() > 6) 
                                    { 
                                        double reallyBig = Double.parseDouble(st); 
                                        reallyBig = reallyBig / 1000; 
                                        st = format.format(reallyBig); 
                                        st = st + "T"; 
                                        g2.drawString(st, (int) ((gap + borderGap) + ((lengthOfXMGap / (bars.size()))   
                                            * i) - (borderGap * .18)),  (int) ((int) height - (borderGap - (height * .0375))));      
                                    }  
                                    else
                                    {
                                        st = st + "B";
                                        g2.drawString(st, (int) (avgLine - (width * .013)),  
                                            (int)(borderGap * 0.85));
                                    }    
                                }  
                                else
                                {     
                                    newAvg = totalAvg / 1000000; 
                                    String st = format.format(newAvg); 
                                    st = st + "M"; 
                                    g2.drawString(st, (int) (avgLine - (width * .013)),  
                                        (int)(borderGap * 0.85)); 
                                } 
                            }     
                            else
                            { 
                                newAvg = totalAvg / 1000; 
                                String st = format.format(newAvg); 
                                st = st + "K"; 
                                g2.drawString(st, (int) (avgLine - (width * .013)),  
                                    (int)(borderGap * 0.85)); 
                            }     
                        } 
                        String st = format.format(totalAvg); 
                        g2.drawString(st, (int) (avgLine - (width * .013)),  
                                    (int)(borderGap * 0.85)); 
                    } 
                }     
  
                g2.setColor(Color.BLACK);  
            } 
                          
            g2.setColor(Color.BLACK);  
                  
            if (i == 0) 
            { 
                x0 = gap + borderGap; 
            }  
            else
            {                 
                x0 = (gap + borderGap) + ((lengthOfXMGap / (bars.size()))   
                    * i);  
            } 
            x1 = x0;     
            y0 = (int) ((int) height - (borderGap - (height * .015)));     
            y1 = (int) ((int) height - (borderGap + (height * .015)));      
                  
            int x2,x3,y4,y5;    
                    
            x2 = (int)x0;    
            x3 = (int)x1;    
            y4 = (int)y0;    
            y5 = (int)y1;    
            g2.drawLine(x2, y4, x3, y5);    
  
            if (i < bars.size())    
            {        
                DecimalFormat format; 
                boolean lot = false; 
                  
                if (bars.size() > 13) 
                { 
                    if (bars.get(i).bin > 110) 
                    {     
                        format = new DecimalFormat("0.00"); 
                        lot = true; 
                    } 
                    else if (bars.get(i).bin > 1000) 
                    { 
                        format = new DecimalFormat("0.00"); 
                        lot = true; 
                    }     
                    else
                    { 
                        format = new DecimalFormat("0.00"); 
                        lot = true; 
                    }     
                }     
                else
                { 
                    format = new DecimalFormat("0.00");  
                    lot = false; 
                }     
                   
                g2.setFont(new Font("TimesRoman", Font.PLAIN, (int) (float)   
                        ((int)fontSize * .85)));  
                if (bars.get(bars.size() - 1).bin > 1000) 
                { 
                    g2.setFont(new Font("TimesRoman", Font.PLAIN, (int) (float)   
                        ((int)fontSize * .55)));  
                } 
                smallest = (bars.get(i).getSmallest());    
                largest = bars.get(i).getLargest();   
                    
                smallest = smallest / 1.00;  
    
                String str = (format.format(smallest));    
                    
                    
                if (i == 0) 
                { 
                    if (lot) 
                    { 
                        str = format.format(0.00); 
                        g2.drawString(str, (int) ((gap + borderGap) - (borderGap * .05)),   
                            (int) ((int) height - (borderGap - (height * .0375)))); 
                    }    
                    else
                    { 
                        str = format.format(0.00); 
                        g2.drawString(str, (int) ((gap + borderGap) - (borderGap * .15)),   
                            (int) ((int) height - (borderGap - (height * .0375)))); 
                    }           
                }   
                else
                {     
                    if (lot) 
                    {     
                        if (bars.get(i - 1).bin > 999999) 
                        { 
                            if (bars.get(i - 1).bin > 999999999) 
                            {                             
                                format = new DecimalFormat("0.00");  
                                double shortD = bars.get(i - 1).bin / 1000000000; 
                                str = format.format(shortD); 
                                if (str.length() > 6) 
                                { 
                                    double reallyBig = Double.parseDouble(str); 
                                    reallyBig = reallyBig / 1000; 
                                    str = format.format(reallyBig); 
                                    str = str + "T"; 
                                    g2.drawString(str, (int) ((gap + borderGap) + ((lengthOfXMGap / (bars.size()))   
                                        * i) - (borderGap * .18)),  (int) ((int) height - (borderGap - (height * .0375))));      
                                }   
                                else
                                {     
                                    str = str + "B"; 
                                    g2.drawString(str, (int) ((gap + borderGap) + ((lengthOfXMGap / (bars.size()))   
                                        * i) - (borderGap * .18)),  (int) ((int) height - (borderGap - (height * .0375))));   
                                } 
                            }    
                            else
                            { 
                                format = new DecimalFormat("0.00");  
                                double shortD = bars.get(i - 1).bin / 1000000; 
                                str = format.format(shortD); 
                                str = str + "M"; 
                                g2.drawString(str, (int) ((gap + borderGap) + ((lengthOfXMGap / (bars.size()))   
                                    * i) - (borderGap * .18)),  (int) ((int) height - (borderGap - (height * .0375)))); 
                            }     
                              
                        }    
                        else
                        {    
                            if (bars.get(i - 1).bin > 9999) 
                            { 
                                double newBarD = bars.get(i - 1).bin; 
                                newBarD = newBarD / 1000; 
                                str = format.format(newBarD); 
                                str = str + "K"; 
                                g2.drawString(str, (int) ((gap + borderGap) + ((lengthOfXMGap / (bars.size()))   
                                * i) - (borderGap * .18)),  (int) ((int) height - (borderGap - (height * .0375)))); 
                            }  
                            else
                            {     
                                str = format.format(bars.get(i - 1).bin);  
                                g2.drawString(str, (int) ((gap + borderGap) + ((lengthOfXMGap / (bars.size()))   
                                * i) - (borderGap * .18)),  (int) ((int) height - (borderGap - (height * .0375)))); 
                            } 
                        } 
                    } 
                    else
                    { 
                        str = format.format(bars.get(i - 1).bin); 
                        g2.drawString(str, (int) ((gap + borderGap) + ((lengthOfXMGap / (bars.size()))   
                            * i) - (borderGap * .15)),  (int) ((int) height - (borderGap - (height * .0375)))); 
                    }     
                }       
                if (i == bars.size() - 1)  
                {  
                    maxOverall = bars.get(i).max;  
                    str = (format.format(largest));  
   
                    if (lot) 
                    { 
                        if (bars.get(i).max > 999999) 
                        { 
                            if (bars.get(i).max > 999999999) 
                            { 
                                format = new DecimalFormat("0.00");  
                                double shortDi = bars.get(i).max / 1000000000; 
                                str = format.format(shortDi);  
                                if (str.length() > 6) 
                                { 
                                    double reallyBig = Double.parseDouble(str); 
                                    reallyBig = reallyBig / 1000; 
                                    str = format.format(reallyBig); 
                                    str = str + "T"; 
                                    g2.drawString(str, (int) ((gap + borderGap) + ((lengthOfXMGap / (bars.size()))   
                                        * (i + 1))) - (gap / (8/2)), (int) ((int) height -   
                                        (borderGap - (height * .0375))));      
                                }   
                                else
                                {     
                                    double shortD = bars.get(i).bin / 1000000000; 
                                    str = format.format(shortD); 
                                    str = str + "B"; 
                                    g2.drawString(str, (int) ((gap + borderGap) + ((lengthOfXMGap / (bars.size()))   
                                        * (i + 1))) - (gap / (8/2)), (int) ((int) height -   
                                        (borderGap - (height * .0375)))); 
                                } 
                            }     
                                  
                            else
                            {     
                                format = new DecimalFormat("0.00");  
                                double shortD = bars.get(i).bin / 1000000; 
                                str = format.format(shortD); 
                                str = str + "M"; 
                                g2.drawString(str, (int) ((gap + borderGap) + ((lengthOfXMGap / (bars.size()))   
                                    * (i + 1))) - (gap / (8/2)), (int) ((int) height -   
                                    (borderGap - (height * .0375)))); 
                            } 
                        }  
                        else
                        {     
                            if (bars.get(i).max > 9999) 
                            { 
                                double barDob = bars.get(i).max; 
                                barDob = barDob / 1000; 
                                str = format.format(barDob); 
                                str = str + "K"; 
                                g2.drawString(str, (int) ((gap + borderGap) + ((lengthOfXMGap / (bars.size()))   
                                * (i + 1))) - (gap / (8/2)), (int) ((int) height -   
                                (borderGap - (height * .0375)))); 
                            }    
                            else
                            {     
                                format = new DecimalFormat("0.00");  
                                str = format.format(bars.get(i).bin); 
                                g2.drawString(str, (int) ((gap + borderGap) + ((lengthOfXMGap / (bars.size()))   
                                    * (i + 1))) - (gap / (8/2)), (int) ((int) height -   
                                    (borderGap - (height * .0375)))); 
                            } 
                        } 
                    }     
                    else
                    {     
                        g2.drawString(str, (int) ((gap + borderGap) + ((lengthOfXMGap / (bars.size()))   
                            * (i + 1))) - (gap / (6/2)), (int) ((int) height -   
                            (borderGap - (height * .0375)))); 
                    } 
                }      
            
                g2.setFont(new Font("TimesRoman", Font.PLAIN, (int)fontSize));  
                  
            } 
                
        }      
  
        DecimalFormat format = new DecimalFormat("0.00");   
            
        if (tooltip)      
        {      
            String s;  
            String t; 
  
            g2.setColor(Color.WHITE);     
                      
            r2.x = mouseX;     
            r2.y = mouseY;     
            r2.height = 100;     
            r2.width = 200;     
                  
            cur = g2.getColor();  
            if (picked.getBlue() > 230 && picked.getGreen() > 230) 
            { 
                g2.setColor(Color.YELLOW); 
            } 
            else
                g2.setColor(Color.CYAN);   
              
                  
            g2.draw3DRect(xToBeDrawn, yToBeDrawn, xToBeWidth, yToBeHeight, tooltip);   
            g2.fill3DRect(xToBeDrawn, yToBeDrawn, xToBeWidth, yToBeHeight, tooltip);   
              
            g2.setColor(cur);   
                  
            if (yToBeHeight < (int) (((lengthOfYBar / 10)) * .80))   
            {   
                g2.drawRoundRect((int)(xToBeDrawn + (borderGap * .05)),    
                    (int) (yToBeDrawn - (borderGap * .8)),    
                    (int) (xToBeWidth - (borderGap * .07)),    
                    (int) (((lengthOfYBar / 10)) * .80), 15, 15);    
                g2.fillRoundRect((int)(xToBeDrawn + (borderGap * .05)),    
                    (int) (yToBeDrawn - (borderGap * .8)),    
                    (int) (xToBeWidth - (borderGap * .07)),    
                    (int) (((lengthOfYBar / 10)) * .80), 15, 15);    
                          
                g2.setColor(Color.BLACK);   
                s = format.format(barToShow.average);  
                String t1 = "Avg:"; 
                g2.setFont(new Font("TimesRoman", Font.PLAIN, (int) ((int)fontSize * 0.55)));  
                g2.drawString(t1, (int) ((int)(xToBeDrawn + (borderGap * .10)) +    
                    (borderGap * .025)), (int) ((int) ((yToBeDrawn + (borderGap * .05)) +    
                            ((int) (((lengthOfYBar / 10)) * .80) / 2)) - (borderGap * .88))) ; 
                g2.drawString(s, (int) ((int)(xToBeDrawn + (borderGap * .10)) +    
                    (borderGap * .025)), (int) ((int) ((yToBeDrawn + (borderGap * .05)) +    
                            ((int) (((lengthOfYBar / 10)) * .80) / 2)) - (borderGap * .72))) ;      
            }     
            else
            {                 
                g2.drawRoundRect((int)(xToBeDrawn + (borderGap * .05)),    
                    (int) (yToBeDrawn + (borderGap * .05)),    
                    (int) (xToBeWidth - (borderGap * .07)),    
                    (int) (((lengthOfYBar / 10)) * .80), 15, 15);    
                g2.fillRoundRect((int)(xToBeDrawn + (borderGap * .05)),    
                    (int) (yToBeDrawn + (borderGap * .05)),    
                    (int) (xToBeWidth - (borderGap * .07)),    
                    (int) (((lengthOfYBar / 10)) * .80), 15, 15);   
                      
                //s = "Average: " + format.format(barToShow.average);     
                g2.setColor(Color.BLACK);   
                s = format.format(barToShow.average);  
    
                t = "Avg:"; 
                  
                g2.setFont(new Font("TimesRoman", Font.PLAIN, (int) ((int)fontSize * 0.55)));   
                g2.drawString(t, (int) ((int)(xToBeDrawn + (borderGap * .10)) +    
                    (borderGap * .025)), (int) (yToBeDrawn + (borderGap * .010)) +    
                            ((int) (((lengthOfYBar / 10)) * .80) / 2)) ; 
                if (barToShow.average > 999 && bars.size() > 10) 
                { 
                    DecimalFormat format2 = new DecimalFormat("0.00");  
                    if (barToShow.average > 100000 && bars.size() > 10) 
                    { 
                        if (barToShow.average < 1000000) 
                        { 
                            double barTo = barToShow.average; 
                            barTo = barTo / 1000; 
                            s = format2.format(barTo); 
                            s = s + "K"; 
                            g2.setFont(new Font("TimesRoman", Font.PLAIN, (int) ((int)fontSize * 0.49)));  
                            g2.drawString(s, (int) ((int)(xToBeDrawn + (borderGap * .05)) +    
                            (borderGap * .025)), (int) (yToBeDrawn + (borderGap * .110)) +    
                                ((int) (((lengthOfYBar / 10)) * .80) / 2)) ; 
                        }     
                        else if (barToShow.average < 1000000000) 
                        { 
                            double barTo = barToShow.average; 
                            barTo = barTo / 1000000; 
                            s = format2.format(barTo); 
                            s = s + "M"; 
                            g2.setFont(new Font("TimesRoman", Font.PLAIN, (int) ((int)fontSize * 0.49)));  
                            g2.drawString(s, (int) ((int)(xToBeDrawn + (borderGap * .05)) +    
                            (borderGap * .025)), (int) (yToBeDrawn + (borderGap * .110)) +    
                                ((int) (((lengthOfYBar / 10)) * .80) / 2)) ; 
                        }  
                        else
                        { 
                              
                            double barTo = barToShow.average; 
                            barTo = barTo / 1000000000; 
                            //format2.format(barTo); 
                            s = format2.format(barTo); 
                            if (s.length() > 6) 
                            { 
                                double newBar = barTo / 1000; 
                                s = format2.format(newBar); 
                                s = s + "T"; 
                                g2.setFont(new Font("TimesRoman", Font.PLAIN, (int) ((int)fontSize * 0.49)));  
  
                                g2.drawString(s, (int) ((int)(xToBeDrawn + (borderGap * .05)) +    
                                (borderGap * .025)), (int) (yToBeDrawn + (borderGap * .110)) +    
                                    ((int) (((lengthOfYBar / 10)) * .80) / 2)) ; 
                            } 
                            else
                            {     
                                s = s + "B"; 
                                g2.setFont(new Font("TimesRoman", Font.PLAIN, (int) ((int)fontSize * 0.49)));  
  
                                g2.drawString(s, (int) ((int)(xToBeDrawn + (borderGap * .05)) +    
                                (borderGap * .025)), (int) (yToBeDrawn + (borderGap * .110)) +    
                                    ((int) (((lengthOfYBar / 10)) * .80) / 2)) ; 
                            } 
                        }     
                    }    
                    else
                    {     
                        s = format2.format(barToShow.average); 
                        g2.drawString(s, (int) ((int)(xToBeDrawn + (borderGap * .05)) +    
                        (borderGap * .005)), (int) (yToBeDrawn + (borderGap * .110)) +    
                                ((int) (((lengthOfYBar / 10)) * .80) / 2)) ; 
                    } 
                }   
                else
                {     
                      
                    if (barToShow.average > 99 && bars.size() > 19) 
                    { 
                        g2.drawString(s, (int) ((int)(xToBeDrawn + (borderGap * .020)) +    
                            (borderGap * .030)), (int) (yToBeDrawn + (borderGap * .110)) +    
                                    ((int) (((lengthOfYBar / 10)) * .80) / 2)) ; 
                    }    
                    else
                    {     
                        g2.drawString(s, (int) ((int)(xToBeDrawn + (borderGap * .10)) +    
                            (borderGap * .015)), (int) (yToBeDrawn + (borderGap * .110)) +    
                                    ((int) (((lengthOfYBar / 10)) * .80) / 2)) ;   
                    } 
                } 
                g2.setColor(Color.YELLOW); 
                Stroke oldOne = g2.getStroke(); 
                g2.setStroke(new BasicStroke(3)); 
                g2.drawLine((int) ((xToBeDrawn + xToBeWidth) - 1), yToBeDrawn, borderGap, yToBeDrawn); 
                g2.setColor(Color.BLACK); 
                DecimalFormat thisF = new DecimalFormat("#"); 
                String thisS = thisF.format(barToShow.height); 
                g2.drawString(thisS, (int) ((xToBeDrawn + xToBeWidth) - (xToBeWidth * .5)), (int) (yToBeDrawn - (borderGap * .1))); 
                g2.setStroke(oldOne); 
                      
            }       
            g2.setFont(new Font("TimesRoman", Font.PLAIN, (int)fontSize));      
        }          
        else if (!tooltip)      
        {      
    
        }          
            
        Font currFont = g2.getFont(); 
        AffineTransform orig = g2.getTransform(); 
        g2.setFont(new Font("TimesRoman", Font.PLAIN, (int) (float)   
                    ((int)fontSize * .85))); 
        g2.rotate(- Math.PI / 2, 0, 0); 
        g2.translate(-height, 0); 
  
        if (maxToTen > 999) 
        { 
            g2.drawString(yAxis, (int) ((height / 2) - (xAxis.length() * (fontSize / 4))), borderGap / 4); 
        }    
        else
        {     
            g2.drawString(yAxis, (int) ((height / 2) - (xAxis.length() * (fontSize / 4))), borderGap / 3); 
        }     
        g2.setTransform(orig); 
        g2.drawString(xAxis, (int) ((width / 2) - (xAxis.length() * (fontSize / 4))), height - (borderGap / 3)); 
        g2.setFont(currFont);             
    }      
    public void displayTooltip(int x, int y, Rectangle r, Bar b)      
    {      
        if (b.shown == false)   
        {       
            barToShow = b;     
            tooltip = true;     
            mouseX = x;     
            mouseY = y;     
            this.toBeChanged = r;   
            xToBeDrawn = (int) r.getX();   
            yToBeDrawn = (int) r.getY();   
            xToBeWidth = (int) r.width;   
            yToBeHeight = (int) r.height;   
        }       
                
        repaint();    
                    
    }        
  
    class MouseListener implements MouseMotionListener      
    {      
        @Override
        public void mouseDragged(MouseEvent e)      
        {      
            int x = e.getX();      
            int y = e.getY();      
            Rectangle r = new Rectangle();      
            for (int i = 0; i < rects.size(); i++)      
            {      
                if (rects.get(i).contains(x, y))      
                {      
                    r.height = rects.get(i).height;      
                    r.width = rects.get(i).width;      
                    r.x = rects.get(i).x;      
                    r.y = rects.get(i).y;      
            
                    Bar b = dict.get(rects.get(i));      
                }         
            }      
        }      
            
        @Override
        public void mouseMoved(MouseEvent me)       
        {      
            {     
                int x = me.getX();     
                int y = me.getY();     
                boolean found = false;   
                Rectangle r = new Rectangle();     
                      
                for (int i = 0; i < rects.size(); i++)     
                {     
                    if (rects.get(i).contains(x, y))     
                    {     
                        r.height = rects.get(i).height;     
                        r.width = rects.get(i).width;     
                        r.x = rects.get(i).x;     
                        r.y = rects.get(i).y;     
          
                        Bar b = dict.get(rects.get(i));     
                        found = true;   
                        displayTooltip(x, y, r, b);     
                    }          
                    else
                    {     
       
                    }         
                }     
                if (found == false)   
                {   
                    tooltip = false;   
                    //g2.setColor(cur);   
                    repaint();   
                }                     
            }     
        }          
    }     
}