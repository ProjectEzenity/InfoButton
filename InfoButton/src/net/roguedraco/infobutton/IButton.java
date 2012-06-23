package net.roguedraco.infobutton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class IButton implements Serializable {

	private static final long serialVersionUID = -745432263306745409L;
	public int x,y,z; // Position for the button (Block)
	public String world; // World the button is in
	
	public boolean isCommandButton; // Is it a command button or file button?
	public String file; // The file the button is associated with
	public String command; // The command the button will run
	
	public IButton(World w, Location location) {
		this.world = w.getName();
		this.x = location.getBlockX();
		this.y = location.getBlockY();
		this.z = location.getBlockZ();
		String key = world+":"+x+":"+y+":"+z;
		System.out.println("IButton: "+key); // DEBUG
		ButtonStorage.infoButtons.put(key, this);
	}
	
	// General InfoButton Stuff
	
	public void runInfoButton(PlayerInteractEvent event) {
		
		Player player = event.getPlayer();
		
		if(this.isCommandButton == true) {
			player.chat("/"+this.command);
		}
		else {
			File f = new File(InfoButton.filepath+"infoBooks" + File.separator + this.file + ".txt");
			boolean exists = f.exists();
			if(exists) {
				// Text file exists, parse it into colours and send to the player
				BufferedReader fileInput;
				try {
					fileInput = new BufferedReader(new FileReader(f));

					List<String> lineList = new ArrayList<String>();
				    try {
				    	String line;
				    	while( ( line = fileInput.readLine() ) != null ) {
				    		lineList.add( line );
				    	}
				    }
				    catch( IOException e ) {
				    	e.printStackTrace();
				    }
				    finally {
				    	try {
				    		fileInput.close();
				    	}
				    	catch( IOException ex ) {
				    		ex.printStackTrace();
				    	}
				    }
				    String[] text = new String[ lineList.size() ];
				    
				    lineList.toArray(text);
				    
				    // File read into string[] "text" now format colours
				    for( int i = 0; i < text.length; i++ ) {
				    	text[i] = InfoButton.convertColors(text[i]);
				    }
			    	// Send the message to the player who requested it
			    	player.sendMessage(text);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					player.sendMessage(ChatColor.RED+"Error whilst reading "+file+".txt");
				}
			}
			else {
				player.sendMessage(""+ChatColor.RED+file+".txt could not be found!");
			}
		}
	}

	public String getWorld() {
		return world;
	}

	public String getFile() {
		return file;
	}

	public String getCommand() {
		return command;
	}

	public void setIsCommandButton(Boolean isCommand) {
		this.isCommandButton = isCommand;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public void setCommand(String command) {
		this.command = command;
	}
}
