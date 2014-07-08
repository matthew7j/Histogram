package histogram;   
      
import java.awt.FlowLayout;  
import java.util.Scanner;    
import java.io.BufferedReader;    
import java.io.File;    
import java.io.FileReader;    
import java.io.IOException;    
import java.util.ArrayList;    
import java.util.List;    
import   java.lang.Math.*;    
import java.text.DecimalFormat;    
import java.util.Collections;    
import java.util.Random; 
import javax.swing.JPanel;  
        
/**    
 *    
 * @author Terrance J. Woods    
 */
public class HistoMath  
{    
    ArrayList<Double> nums = new ArrayList();  
    ArrayList<Double> bins = new ArrayList();  
    Histogram h = new Histogram(); 
    boolean cont = true;
    double randMin, randMax, numOfRand;
    int num;
    int classNums;
        
    public ArrayList<Double> getNums()  
    {  
        if (nums.size() < 1) 
        { 
            return null; 
        }     
        else
        { 
            return nums; 
        }     
           
    }          
        
    public HistoMath(String fileName, String min, String max, String numOfRand, int classNums) throws IOException    
    {    
        DecimalFormat format = new DecimalFormat("0.##");    
        this.classNums = classNums;
        
        if ("NO".equals(fileName)) 
        { 
            if (min != null)
            {
                this.randMin = Double.parseDouble(min);
            }   
            else if (min == null && max != null)
            {
                this.randMin = 0;
            }     
            if (max != null)
            {
                this.randMax = Double.parseDouble(max);
            }   
            else if (max == null && min != null)
            {
                this.randMax = randMin + 1;
            }  
            if (numOfRand != null)
            {
                num = Integer.parseInt(numOfRand);
            }    
            else
            {
                num = 200;
            }    
            
            if (max == null && min == null)
            {
                Random gen = new Random(); 
                for (int i = 0; i < 200; i++) 
                { 
                    double randomFloat = gen.nextDouble(); 
                    nums.add(randomFloat); 
                }
            } 
            else
            {
                Random gen = new Random(); 
                for (int i = 0; i < num; i++) 
                { 
                    double randomFloat = randMin + (randMax - randMin) * gen.nextDouble(); 
                    nums.add(randomFloat); 
                }  
            }    
               
        }     
        else
        {     
            String line;    
            Scanner in = new Scanner(new File(fileName)); 
            boolean notLetter = true;
            if (!in.hasNext() && !in.hasNextLine()) 
            {
                cont = false;
            }
            while (in.hasNextLine() && notLetter)    
            {    
                line = in.nextLine();    
                //System.out.println(line);    
                String[] numbers = line.split("[\\[\\],\\s]+");    
                for (String number : numbers) 
                {
                    try
                    {    
                        nums.add(Double.parseDouble(number));
                    }
                    catch(Exception e)
                    {
                        System.out.println("Cannot read letter or symbol in from file");
                        nums.clear();
                        cont = false;
                        notLetter = false;
                        break;
                    }    
                    //System.out.println("Histotext has : " + number); //sherri
                }    
            }    
            
        }       
        if (cont)
        {    
            Collections.sort(nums);    
                
            int n = nums.size();    
            //System.out.println("There are " + n + " numbers in this data.");    
            Double x1 = nums.get(0);    
            //System.out.println("The smallest number in this set is " + x1);    
            Double xn = nums.get(n - 1);    
            //System.out.println("The biggest number in this data set is " + xn);    
            float range = (float) (xn - x1);    
            //System.out.println("The range for this data set is " + range);    
            int classes;    
            if (n > 1000)
            {
                classes = 25;
            }
            else
            {    
                classes = (int) Math.ceil(Math.sqrt(n)); 
            }    
            if (classNums > 0)
            {
                if (classNums < 25)
                {    
                    classes = classNums;
                }
                else
                {
                    classes = 25;
                }    
            }    
            if (classNums < 0)
            {
                classes = (int) Math.ceil(Math.sqrt(n)); 
                if (classes > 25)
                {
                    classes = 25;
                }    
            }  
            if (classes > 25)
            {
                classes = 25;
            }  
            float width;    
            width = range/classes;    
            //System.out.println("The width of each class is " + width);    
            double [] classlcl = new double[classes +1];    
            classlcl[0] = x1;    
            double [] classucl = new double[classes];    
            ArrayList<Double> uppers = new ArrayList();  
            classucl[classes-1] = xn;    
              
    
            for (int j = 1; j < classlcl.length - 1; j++)  
            {      
                classlcl[j] = classlcl[j-1] + width;    
            }             
            for (int k = classucl.length -2; k >= 0; --k)   
            {      
                classucl[k] = classucl[k+1] - width;   
            }  
            for (int d = 0; d < classlcl.length -1; d++)    
            {    
                uppers.add(classucl[d]);  
            }    
            double average = 0;  
            for (int i = 0; i < nums.size(); i++)  
            {  
                average += nums.get(i);  
            }      
            average = average / nums.size();  
            h.bins = uppers;  
            h.data = nums;  
            h.numClasses = classes;  
            h.average = average;  
        }    
              
    }     
      
    public Histogram getHistogram()  
    {  
        return h;  
    }          
}