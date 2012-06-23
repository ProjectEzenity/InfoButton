package net.roguedraco.infobutton;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.block.Block;

public class ButtonStorage implements Serializable {
	
	private static final long serialVersionUID = -3972401686847472298L;
	public static Map<String,IButton> infoButtons = new HashMap<String,IButton>();	
	
	public static boolean isInfoButton(Block block) {
		return isInfoButton(block.getWorld().getName(),block.getX(),block.getY(),block.getZ());
	}
	
	public static boolean isInfoButton(String world,int x,int y, int z) {
		String key = world+":"+x+":"+y+":"+z;
		System.out.println("IButton: "+key); // DEBUG
		
		if(ButtonStorage.infoButtons.containsKey(key)) {
			return true;
		}
		else return false;
	}
	
	public static IButton getInfoButton(Block block) {
		return getInfoButton(block.getWorld().getName(),block.getX(),block.getY(),block.getZ());
	}
	
	public static IButton getInfoButton(String world,int x,int y, int z) {
		String key = world+":"+x+":"+y+":"+z;
		System.out.println("IButton: "+key); // DEBUG
		if(ButtonStorage.infoButtons.containsKey(key)) {
			return ButtonStorage.infoButtons.get(key);
		}
		else return null;
	}
	
	public static void deleteButton(Block block) {
		deleteButton(block.getWorld().getName(),block.getX(),block.getY(),block.getZ());
	}
	
	public static void deleteButton(String world,int x,int y, int z) {
		String key = world+":"+x+":"+y+":"+z;
		System.out.println("IButton: "+key); // DEBUG
		if(ButtonStorage.infoButtons.containsKey(key)) {
			ButtonStorage.infoButtons.remove(key);
		}
	}
	
	// File Stuff - Actual Storage
	
	public static void save() {
		try {
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
	public static void load() {
		try{
			String path = InfoButton.filepath + "infoButtons.dat";
			
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
			Object result = ois.readObject();
			HashMap<String,IButton> inputhashset = null;
			
			if (result instanceof HashMap<?,?>) {
				inputhashset = (HashMap<String,IButton>) result;
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
