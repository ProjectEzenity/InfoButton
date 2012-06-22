package net.roguedraco.infobutton;

import java.io.File;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class InfoButton extends JavaPlugin {
	
	static Logger log;
	public static String filepath = "plugins" + File.separator + "InfoButton" + File.separator;
	public static InfoButton main = null;

	public void onEnable() {
		log = Logger.getLogger("Minecraft");
		InfoButton.main = this;
		PluginManager pm = getServer().getPluginManager();
	    
	    Listener events = new Events();
	    pm.registerEvents(events, this);
	    
	    log.info("InfoButton enabled.");
	}
	
	public void onDisable() {
		
		log.info("InfoButton disabled.");
		
	}
	
	public static String convertColors(String str) {
		Pattern color_codes = Pattern.compile("&([0-9A-Fa-fkK])");
		Matcher find_colors = color_codes.matcher(str);
		while (find_colors.find()) {
		 str = find_colors.replaceFirst(new StringBuilder().append(ChatColor.COLOR_CHAR).append(find_colors.group(1)).toString());
		 find_colors = color_codes.matcher(str);
		}
		return str;
	}
	
}
