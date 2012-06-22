package net.roguedraco.infobutton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.roguedraco.core.RogueDracoPlugin;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class ButtonStorage implements Serializable {
	
	private static final long serialVersionUID = -3972401686847472298L;
	private static Map<String,ButtonStorage> infoButtons = new HashMap<String,ButtonStorage>();

	public int x,y,z; // Position for the button (Block)
	public String world; // World the button is in
	
	public boolean isCommandButton; // Is it a command button or file button?
	public String file; // The file the button is associated with
	public String command; // The command the button will run
	
	public static boolean isInfoButton(Block block) {
		return isInfoButton(block.getWorld().getName(),block.getX(),block.getY(),block.getZ());
	}
	
	public static boolean isInfoButton(String world,int x,int y, int z) {
		String key = world+":"+x+":"+y+":"+z;
		
		if(infoButtons.containsKey(key)) {
			return true;
		}
		else return false;
	}
	
	public static ButtonStorage getInfoButton(Block block) {
		return getInfoButton(block.getWorld().getName(),block.getX(),block.getY(),block.getZ());
	}
	
	public static ButtonStorage getInfoButton(String world,int x,int y, int z) {
		String key = world+":"+x+":"+y+":"+z;
		if(infoButtons.containsKey(key)) {
			return infoButtons.get(key);
		}
		else return null;
	}
	
	// General InfoButton Stuff
	
	public void runInfoButton(PlayerInteractEvent event) {
		
		Player player = event.getPlayer();
		
		if(this.isCommandButton == true) {
			player.chat("/"+this.command);
		}
		else {
			File file = new File(InfoButton.filepath+"infoBooks" + File.separator + this.file + ".txt");
			boolean exists = file.exists();
			if(exists) {
				// Text file exists, parse it into colours and send to the player
				String infoBook = file
				InfoButton.convertColors(str);
			}
		}
		
		
	}
	
	
	// File Stuff - Actual Storage
	
	public void save() {
		try {
			String key = world+":"+x+":"+y+":"+z;
			infoButtons.put(key, this);
			String path = InfoButton.filepath + "infoButtons.dat";
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
			oos.writeObject(infoButtons);
			oos.flush();
			oos.close();
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void load() {
		try{
			String path = InfoButton.filepath + "infoButtons.dat";
			
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
			Object result = ois.readObject();
			HashMap<String,ButtonStorage> inputhashset = null;
			
			if (result instanceof HashMap<?,?>) {
				inputhashset = (HashMap<String,ButtonStorage>) result;
			}
			if (inputhashset != null) {
				ButtonStorage.infoButtons = inputhashset;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
