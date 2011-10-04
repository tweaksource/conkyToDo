using System;
using Gtk;

public partial class MainWindow : Gtk.Window
{
	string infile = "~/.conky/conky-tweak/todoTest";
	string outfile = "~/.conky/conky-tweak/todoTest_bak";
    
	
	public MainWindow () : base(Gtk.WindowType.Toplevel)
	{
		Build ();
		selectFile();
		readFile();
		btnRevert.Visible = false;
	}
	
	// Select a file with chooser
	protected void selectFile()
	{
		// Create and display a fileChooserDialog
     	FileChooserDialog chooser = new FileChooserDialog(
        "Please select a logfile to view ...",
        this,
        FileChooserAction.Open,
        "Cancel", ResponseType.Cancel,
        "Open", ResponseType.Accept );
		     
    	if( chooser.Run() == ( int )ResponseType.Accept )
  		{
			infile = chooser.Filename;
			outfile = chooser.Filename + "_bak";
		} // end if
 	    chooser.Destroy();
	}
	
	protected void readFile()
	{
		tvEdit.Buffer.Text = "";
		try
		{
      		// Open the file for reading.
			System.IO.StreamReader file =
        	System.IO.File.OpenText( infile );
        
      		// Copy the contents into the tvEdit
        	tvEdit.Buffer.Text = file.ReadToEnd();
        
        	// Set the MainWindow Title to the filename.
        	this.Title = "Editing " + infile;
                
        	// Close the file so as to not leave a mess.
        	file.Close();
		}
		catch (Exception ex)
		{
			Console.WriteLine("Error: " + ex.ToString());	
		}
	}
	
	// Backup original file and save textview contents in original file
	protected void writeFile()
	{
		try
		{
		// backup tvEdit to file_bak		
		System.IO.File.Copy(infile, outfile, true);
		// write contents of tvEdit to file		
		System.IO.StreamWriter sw = new System.IO.StreamWriter(infile);
		string txt = tvEdit.Buffer.Text;
		sw.Write(txt);
		sw.Close();
		}
		catch (Exception ex)
		{
			Console.WriteLine("Error: " + ex.ToString());	
		}	
	}

	// When form is closed?
	protected void OnDeleteEvent (object sender, DeleteEventArgs a)
	{
		Application.Quit ();
		a.RetVal = true;
	}
	
	// When save button is clicked, write contents of textview to file
	protected virtual void OnBtnSaveClicked (object sender, System.EventArgs e)
	{
		writeFile();
		btnRevert.Visible = true;
	}
	// when revert button is clicked, rewrite file with contents of backup file
	
	protected virtual void OnBtnRevertClicked (object sender, System.EventArgs e)
	{
		// write content of todo_bak to todo
		System.IO.File.Copy(outfile, infile, true);
		readFile();
	}
	// When close button is clicked, close app
	protected virtual void OnBtnCloseClicked (object sender, System.EventArgs e)
	{
		Application.Quit();
	}
	
	// Create new text file
	protected virtual void OnNewActionActivated (object sender, System.EventArgs e)
	{
		// create new text file
	}
	// select a file
	protected virtual void OnOpenActionActivated (object sender, System.EventArgs e)
	{
		selectFile();
		readFile();
	}
	// show about box
	protected virtual void OnAboutActionActivated (object sender, System.EventArgs e)
	{
		ShowMessage(this,"About Tweaksource","ToDo v0.1 - By Brett Marshall\nemail: brett@tweaksource.com");
	}
	
	// create dialog
	void ShowMessage (Window parent, string title, string message)
	{
	    Dialog dialog = null;
	    try {
	        dialog = new Dialog (title, parent,
	            DialogFlags.DestroyWithParent | DialogFlags.Modal,
	            ResponseType.Ok);
	        dialog.VBox.Add (new Label (message));
	        dialog.ShowAll ();
	
	        dialog.Run ();
	    } finally {
	        if (dialog != null)
	            dialog.Destroy ();
	    }
	}	
}

