package drenchantments;

import org.bukkit.plugin.java.JavaPlugin;

import drenchantments.enchantments.DREnchantmentsManager;
import drenchantments.utils.FileConfigurationsManager;

public class DREnchantments extends JavaPlugin {

	@Override
	public void onEnable() {
		super.onEnable();
		
		FileConfigurationsManager fcm = FileConfigurationsManager.getInstance(this);
		fcm.createConfigurationFile("config.yml");
		fcm.reloadAllFiles();
		
		System.out.println(getName() + " has been loaded successfully!");
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		
		if (DREnchantmentsManager.getInstance().saveEnchantments()) {
			System.out.println("Successfully saved enchantments!");
		} else {
			System.out.println("An error has occured while saving the enchantments check out log for info.");
		}
		
		System.out.println(getName() + " has been disable successfully!");
	};
	
}
