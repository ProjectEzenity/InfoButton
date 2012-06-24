package net.roguedraco.infobutton;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class Events implements Listener {

	@EventHandler
	public void onButtonPress(PlayerInteractEvent event) {
		
		// Respect other plugins
		if(event.isCancelled()) {
			return;
		}
		
		// Check if the block is a button
		if(event.getClickedBlock().getTypeId() == 77) {
			
			// Is this button an InfoButton?
			if(ButtonStorage.isInfoButton(event.getClickedBlock())) {
				// Run the button
				IButton button = ButtonStorage.getInfoButton(event.getClickedBlock());
				button.runInfoButton(event);
			}
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		// Respect other plugins
		if(event.isCancelled()) {
			return;
		}
		
		// Check if the block is a button
		if(event.getBlock().getTypeId() == 77) {
			
			// Is this button an InfoButton?
			if(ButtonStorage.isInfoButton(event.getBlock())) {
				// Delete the button
				ButtonStorage.deleteButton(event.getBlock());
			}
		}
	}
	
}
