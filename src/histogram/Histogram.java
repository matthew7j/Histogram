package histogram;       
import java.awt.BorderLayout;     
import java.awt.Color;     
import java.awt.Container;     
import java.awt.Dimension;     
import java.awt.Rectangle;       
import java.awt.geom.*;       
import java.awt.Graphics;       
import java.awt.Graphics2D;       
import java.util.ArrayList;     
import javax.swing.JFrame;     
import javax.swing.JPanel;     
import javax.swing.JTextField;     
              
public class Histogram     
{                     
    protected ArrayList<Double> data = new ArrayList();     
    protected ArrayList<Double> bins = new ArrayList();     
    protected int numClasses;     
    protected int intervals;     
    protected double average;     
    protected String title;
    protected String xAxis;
    protected String yAxis;
    protected ArrayList<Bar> barss = new ArrayList();
    protected Color c;
          
    public void process(Histogram h, Color c, String title, String xAxis, String yAxis)     
    {     
        intervals = h.numClasses * 5;     
                  
        this.data = h.data;     
        this.bins = h.bins;     
        this.numClasses = h.numClasses;     
        this.intervals = h.intervals;     
        this.average = h.average; 
        this.c = c;
        this.title = title;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
          
        getHistogramPrint();     
    }             
                  
     public void paint(Graphics g, Histogram h)     
     {       
        Graphics2D g2 = (Graphics2D)g;           
    }       
    public void getHistogramPrint()     
    {     
        int j = 0;     
        ArrayList<Bar> bars = new ArrayList();     
          
        for (int i = 0; i < numClasses; i++)     
        {             
            Bar bar = new Bar();     
            j = 0;     
                      
            while (j < data.size())     
            {     
                if (i == 0)     
                {     
                    if (data.get(j) <= bins.get(i))                  
                    {         
                        bar.classNums.add(data.get(j));     
                        bar.height++;      
                    }          
                }        
                else
                {     
                    if (data.get(j) <= bins.get(i) && data.get(j) > bins.get(i - 1))     
                    {     
                        bar.classNums.add(data.get(j));     
                        bar.height++;       
                    }       
                }      
                j++;     
            } 
            bar.bin = bins.get(i);
            bar.width = bins.get(i);     
            double avg = 0;     
              
            for (int k = 0; k < bar.classNums.size(); k++)     
            {     
                avg += bar.classNums.get(k);     
                if (k == 0)    
                {    
                    bar.min = bar.classNums.get(k);    
                    bar.max = bar.classNums.get(k);    
                }       
                else
                {    
                    if (bar.max < bar.classNums.get(k))    
                        bar.max = bar.classNums.get(k);    
                    if (bar.min > bar.classNums.get(k))    
                        bar.min = bar.classNums.get(k);    
                }        
            }     
            avg = avg / bar.classNums.size();     
            bar.average = avg;     
            bars.add(bar);            
        }        
    
        barss = bars;     
        drawHisto(bars);     
    }     
    public void drawHisto(ArrayList<Bar> bars)     
    {     
        HistoGraph graph = new HistoGraph(bars, average, c, title, xAxis, yAxis);     
    }          
    public ArrayList<Bar> getBars()     
    {     
        return barss;     
    }             
                      
}     
class Bar     
{     
    double height = 0;  
    double bin;
    double width = 0;     
    double average;     
    double stdDev;     
    double min, max;    
    boolean shown = false;  
    int maxLength; 
    
    ArrayList<Double> classNums = new ArrayList();     
              
    public Bar()     
    {     
                  
    }        
    public double getSmallest()    
    {    
        return min;    
    }          
    public double getLargest()    
    {    
       return max;    
    }         
    @Override
    public String toString()     
    {     
        String s = "Height: " + height + "\nWidth: " + width + "\n" +     
                "Average: " + average + "\nNums: " + classNums + "\n";     
        return s;     
    }             
              
}