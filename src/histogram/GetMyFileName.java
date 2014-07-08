package histogram;  
    
import java.awt.*;   
import java.awt.event.*;   
import javax.swing.*;   
import java.io.*;   
    
public class GetMyFileName extends JFrame  
{  
    JTextField   _fileNameTF  = new JTextField(15);   
    JTextField   _wordCountTF = new JTextField(4);   
    JFileChooser _fileChooser = new JFileChooser();  
    String wholeFileName;
        
    String fileChosen;  
    
    public String getFileChosen()  
    {  
        return fileChosen;  
    }          
    public GetMyFileName()  
    {  
        //... Create / set component characteristics.   
        _fileNameTF.setEditable(false);   
        _wordCountTF.setEditable(false);   
      
        //... Add listeners   
      
        //... Create content pane, layout components   
        JPanel content = new JPanel();   
        content.setLayout(new FlowLayout());   
        content.add(new JLabel("File:"));   
        content.add(_fileNameTF);   
        content.add(_wordCountTF);   
        content.setVisible(true);  
      
        //... Create menu elements (menubar, menu, menu item)   
        JMenuBar menubar  = new JMenuBar();   
        JMenu    fileMenu = new JMenu("File");   
        JMenuItem openItem = new JMenuItem("Open...");   
        openItem.addActionListener(new GetMyFileName.OpenAction());   
      
        //... Assemble the menu   
        menubar.add(fileMenu);   
        fileMenu.add(openItem);   
      
        //... Set window characteristics   
        this.setJMenuBar(menubar);   
        this.setContentPane(content);   
        this.setTitle("File Name");   
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
        this.pack();                      // Layout components.   
        this.setLocationRelativeTo(null); // Center window.   
    }          
    
    class OpenAction implements ActionListener {   
        @Override
        public void actionPerformed(ActionEvent ae) {   
            //... Open a file dialog.   
            int retval = _fileChooser.showOpenDialog(GetMyFileName.this);   
            if (retval == JFileChooser.APPROVE_OPTION) {   
                //... The user selected a file, get it, use it.   
                File file = _fileChooser.getSelectedFile();   
                wholeFileName = file.getAbsolutePath();
                //... Update user interface.   
                _fileNameTF.setText(file.getName());   
                //_wordCountTF.setText("" + countWordsInFile(file));   
                fileChosen = wholeFileName;  
                System.out.println(fileChosen);  
            }   
        }   
    }   
}