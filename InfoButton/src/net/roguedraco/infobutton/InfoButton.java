package net.roguedraco.infobutton;

import java.io.File;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class InfoButton extends JavaPlugin {
	
	static Logger log;
	public static String filepath = "plugins" + File.separator + "InfoButton" + File.separator;
	public static InfoButton main = null;

	public void onEnable() {
		
		try {
			new File(filepath+"infoBooks/").mkdirs();
		}
		catch(Exception ex) {
			
		}
		
		log = Logger.getLogger("Minecraft");
		InfoButton.main = this;
		PluginManager pm = getServer().getPluginManager();
	    
	    Listener events = new Events();
	    pm.registerEvents(events, this);
	    
	    ButtonStorage.load();
	    
	    log.info("InfoButton enabled.");
	}
	
	public void onDisable() {
		ButtonStorage.save();
		log.info("InfoButton disabled.");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if ((sender instanceof Player)) {
			Player player = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("ib")){
				// Which command did they run?
				Boolean hasRunCommand = false;
				if(args.length > 0) {					
					if(args[0].equalsIgnoreCase("create")) { // /ib create
						if(player.hasPermission("infobutton.create")) {
							if(args.length == 1) { // 0:"create"
								// Create the InfoButton
								World world = player.getWorld();
								Block buttonBlock = player.getTargetBlock(null, 200);
								System.out.println("Event: "+buttonBlock.getX()+","+buttonBlock.getY()+","+buttonBlock.getZ());
								if(buttonBlock.getTypeId() == 77) {
									new IButton(world,buttonBlock.getLocation());
									ButtonStorage.save();
									player.sendMessage(ChatColor.GREEN+"InfoButton created! Now set its type and your ready!");
								}
								else {
									player.sendMessage(ChatColor.RED+"The block you are looking at is not a button!");
								}
							}
							else {
								player.sendMessage(ChatColor.RED+"/ib create");
							}
						}
						else {
							player.sendMessage(ChatColor.RED+"You do not have permission to create InfoButtons!");
						}
						return true;
					}
					else if(args[0].equalsIgnoreCase("command")) { // /ib command
						if(player.hasPermission("infobutton.create.command")) {
							if(args.length >= 2) { // 0:"command" 1:"<Command>" [2:"<arg1>" 3:"<arg2>" etc...]
								// Set InfoButton as CommandBased
								Block buttonBlock = player.getTargetBlock(null, 200);
								System.out.println("Event: "+buttonBlock.getX()+","+buttonBlock.getY()+","+buttonBlock.getZ());
								if(ButtonStorage.isInfoButton(buttonBlock)) {
									String command = args[1];
									for( int i = 2; i < args.length; i++ ) {
								    	command += " "+args[i];
								    }
									IButton iButton = ButtonStorage.getInfoButton(buttonBlock);
									iButton.setCommand(command);
									iButton.setIsCommandButton(true);
									ButtonStorage.save();
									player.sendMessage(ChatColor.GREEN+"InfoButton set as a command button with the command: "+ChatColor.GRAY+"/"+command);
								}
								else {
									player.sendMessage(ChatColor.RED+"The block you are looking at is not an InfoButton!");
								}
							}
							else {
								player.sendMessage(ChatColor.RED+"/ib command <Command> [<arg1> <arg2>...]");
							}
						}
						else {
							player.sendMessage(ChatColor.RED+"You do not have permission to create Command InfoButtons!");
						}
						return true;
					}
					else if(args[0].equalsIgnoreCase("file")) { // /ib file
						if(player.hasPermission("infobutton.create.file")) {
							if(args.length == 2) { // 0:"file" 1:"<FileName>"
								// Set InfoButton as FileBased
								Block buttonBlock = player.getTargetBlock(null, 200);
								if(ButtonStorage.isInfoButton(buttonBlock)) {
									String file = args[1];
									IButton iButton = ButtonStorage.getInfoButton(buttonBlock);
									iButton.setFile(file);
									iButton.setIsCommandButton(false);
									ButtonStorage.save();
									player.sendMessage(ChatColor.GREEN+"InfoButton set as a file button reading the file: "+ChatColor.GRAY+file+".txt");
								}
								else {
									player.sendMessage(ChatColor.RED+"The block you are looking at is not an InfoButton!");
								}
							}
							else {
								player.sendMessage(ChatColor.RED+"/ib file <Filename>");
							}
						}
						else {
							player.sendMessage(ChatColor.RED+"You do not have permission to create File InfoButtons!");
						}
						return true;
					}
					else if (args[0].equalsIgnoreCase("delete")) { // /ib delete
						if(player.hasPermission("infobutton.delete")) {
							if(args.length == 1) { // 0:"delete"
								// Delete InfoButton
								Block buttonBlock = player.getTargetBlock(null, 200);
								if(ButtonStorage.isInfoButton(buttonBlock)) {
									ButtonStorage.deleteButton(buttonBlock);
									ButtonStorage.save();
									player.sendMessage(ChatColor.GREEN+"InfoButton deleted.");
								}
								else {
									player.sendMessage(ChatColor.RED+"The block you are looking at is not an InfoButton!");
								}
							}
							else {
								player.sendMessage(ChatColor.RED+"/ib delete");
							}
						}
						else {
							player.sendMessage(ChatColor.RED+"You do not have permission to delete InfoButtons!");
						}
						return true;
					}
					else if( args[0].equalsIgnoreCase("save")) {
						if(player.hasPermission("infobutton.save")) {
							if(args.length == 1) { // 0:"save"
								ButtonStorage.save();
							}
							else {
								player.sendMessage(ChatColor.RED+"/ib save");
							}
						}
						else {
							player.sendMessage(ChatColor.RED+"You do not have permission to save InfoButtons!");
						}
						return true;
					}
				}
				
				if(hasRunCommand == false) {
					// Show plugin help
					player.sendMessage(ChatColor.AQUA+"====================[ "+ChatColor.DARK_AQUA+"InfoButton" + ChatColor.AQUA+" ]====================");
					player.sendMessage(ChatColor.GRAY+"All commands relate to the InfoButton block that you are looking at (Except save)");
					if(player.hasPermission("infobutton.create")) {
						player.sendMessage(ChatColor.GREEN + "/ib create" + ChatColor.GRAY + " Creates an InfoButton.");
					}
					if(player.hasPermission("infobutton.create.command")) {
						player.sendMessage(ChatColor.GREEN + "/ib command <Command> <Arg1>..." + ChatColor.GRAY + " Changes the InfoButton to run a command.");
					}
					if(player.hasPermission("infobutton.create.file")) {
						player.sendMessage(ChatColor.GREEN + "/ib file <Filename>" + ChatColor.GRAY + " Changes the InfoButton to read a file.");
					}
					if(player.hasPermission("infobutton.delete")) {
						player.sendMessage(ChatColor.GREEN + "/ib delete" + ChatColor.GRAY + " Deletes the InfoButton.");
					}
					if(player.hasPermission("infobutton.save")) {
						player.sendMessage(ChatColor.GREEN + "/ib save" + ChatColor.GRAY + " Saves the storage of all InfoButtons.");
					}
					return true;
				}
			}
			return false;
        } else {
           sender.sendMessage(ChatColor.RED + "You must be a player!");
           return false;
        }
	}
	
	public static String convertColors(String str) {
		Pattern color_codes = Pattern.compile("&([0-9A-Fa-fkKLlOoMmNn])");
		Matcher find_colors = color_codes.matcher(str);
		while (find_colors.find()) {
		 str = find_colors.replaceFirst(new StringBuilder().append(ChatColor.COLOR_CHAR).append(find_colors.group(1)).toString());
		 find_colors = color_codes.matcher(str);
		}
		return str;
	}
	
}
