package net.roguedraco.infobutton;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class Events implements Listener {

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
				ButtonStorage button = ButtonStorage.getInfoButton(event.getClickedBlock());
				button.runInfoButton(event);
			}
		}
	}
	
}
