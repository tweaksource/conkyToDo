import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.util.*;
import net.miginfocom.swing.*;

public class Todo extends JFrame implements ActionListener
{
	// Declarations
	public static MigLayout layout = new MigLayout();
	public static JTextArea txtAOut = new JTextArea("");
	public static JButton btnSave = new JButton("Save");
	public static JButton btnRevert = new JButton("Revert");
	public static JButton btnClose = new JButton("Close");
	public static JPanel panel = new JPanel(layout);
	public static JScrollPane sp = new JScrollPane(panel);
	private final static String newline = System.getProperty("line.separator");
	private final static String inFile = "/home/brett/.conky/conky-tweak/.todo";
	private static File outFile = new File("/home/brett/.conky/conky-tweak/.todo");
	private static File bakFile = new File("/home/brett/.conky/conky-tweak/.todo_bak");

	// Constructor
	public Todo()
	{
			
	}

	public static void main(String[] argv)
	{
		Todo frame = new Todo();
		frame.getContentPane().add(sp);
		panel.add(txtAOut, "span 3, growx, growy, push, wrap");
		txtAOut.setFont(new Font("Arial", Font.PLAIN, 16));
		panel.add(btnSave, "split, align center");
		panel.add(btnRevert, "");
		panel.add(btnClose, "");
		btnSave.addActionListener(frame);
		btnRevert.addActionListener(frame);
		btnClose.addActionListener(frame);
		txtAOut.setLineWrap(true);
		txtAOut.setWrapStyleWord(true);
		frame.setPreferredSize(new Dimension(300, 200));
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		readFile();
	}

	private static void readFile()
	{
		try
		{
			// Open the file that is the first 
			// command line parameter
			FileInputStream fstream = new FileInputStream(inFile);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			//Read File Line By Line
			while ((strLine = br.readLine()) != null)   
			{
				// Print the content on the console
				// System.out.println (strLine);
				txtAOut.append(strLine + newline);
			}
			//Close the input stream
			in.close();
		}
		catch (Exception e)
		{
			//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	private static void writeFile(File outFile) throws FileNotFoundException, IOException 
	{
		if (outFile == null) 
		{
			throw new IllegalArgumentException("File should not be null.");
		}
		if (!outFile.exists()) 
		{
			throw new FileNotFoundException ("File does not exist: " + outFile);
		}
		if (!outFile.isFile()) 
		{
			throw new IllegalArgumentException("Should not be a directory: " + outFile);
		}
		if (!outFile.canWrite()) 
		{
			throw new IllegalArgumentException("File cannot be written: " + outFile);
		}
		else
		{
			backupFile();
			
			//use buffering
		    Writer output = new BufferedWriter(new FileWriter(outFile));
		    try 
		    {
		      //FileWriter always assumes default encoding is OK!
		      ArrayList contents = new ArrayList();
		      int lines = txtAOut.getLineCount();
		      System.out.println(lines);
		      txtAOut.write(output);
		    }
		    finally {
		      output.close();
		    }
		}
	}
	
	// Backup file before writing
	private static void backupFile()
	{	
		try
		{
		    FileReader in = new FileReader(inFile);
		    FileWriter out = new FileWriter(bakFile);
		    int c;
		
		    while ((c = in.read()) != -1)
		      out.write(c);
		
		    in.close();
		    out.close();
		}
		catch (Exception ex)
		{
			System.err.println("Error :" + ex.toString());
		}
	}
	
	// revert file
	private static void revertFile()
	{
		try
		{
		    FileReader in = new FileReader(bakFile);
		    FileWriter out = new FileWriter(inFile);
		    int c;
		
		    while ((c = in.read()) != -1)
		      out.write(c);
		
		    in.close();
		    out.close();
		}
		catch (Exception ex)
		{
			System.err.println("Error :" + ex.toString());
		}
	}

	// Override Action Performed
		public void actionPerformed(ActionEvent e)
		{
			Object src = e.getActionCommand();
			if (src.toString().equals("Save"))
			{
				// write contents of text area to file
				try
				{
					writeFile(outFile);
				}
				catch (Exception ex)
				{
					System.err.println("Error :" + ex.toString());
				}
			}	
			else if (src.toString().equals("Revert"))
			{
				revertFile();
			}
			else if (src.toString().equals("Close"))
			{
				System.exit(0);
			}
		}
}
