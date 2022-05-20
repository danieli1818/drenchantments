package drenchantments.enchantments;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import drenchantments.utils.FileConfigurationsManager;

public class DREnchantmentsManager {

	private Map<Class<?>, Enchantment> customEnchantmentsIDs;
	private FileConfiguration enchantmentsFileConfiguration;
	
	private static DREnchantmentsManager instance = null;
	
	private DREnchantmentsManager() {
		this.customEnchantmentsIDs = new HashMap<Class<?>, Enchantment>();
		this.enchantmentsFileConfiguration = FileConfigurationsManager.getInstance().getFileConfiguration("config.yml");
	}
	
	public static DREnchantmentsManager getInstance() {
		if (instance == null) {
			instance = new DREnchantmentsManager();
		}
		return instance;
	}
	
	private void registerEnchantment(Function<Integer, Enchantment> enchantmentCreator, Integer id) {
		
		try {
			
			Field acceptingField = Enchantment.class.getDeclaredField("acceptingNew");
			acceptingField.setAccessible(true);
			acceptingField.set(null, true);
			
			if (id == null) {
				id = Enchantment.values().length;
			}
			
			Enchantment enchantment = enchantmentCreator.apply(id);
			
			Enchantment.registerEnchantment(enchantment);
			
			customEnchantmentsIDs.put(enchantment.getClass(), enchantment);
			
		} catch (Exception exception) {
			exception.printStackTrace();
		}
				
	}
	
	public void registerEnchantments(Map<String, Function<Integer, Enchantment>> enchantments) {
		
		for (Entry<String, Function<Integer, Enchantment>> enchantmentEntry : enchantments.entrySet()) {
			registerEnchantment(enchantmentEntry.getKey(), enchantmentEntry.getValue());
		}
		
	}
	
	public void registerEnchantment(String name, Function<Integer, Enchantment> enchantment) {
		
		Integer id = this.enchantmentsFileConfiguration.getInt(name, -1);
		if (id == -1) {
			id = null;
		}
		this.enchantmentsFileConfiguration.set(name, id);
		registerEnchantment(enchantment, id);
		
	}
	
	public boolean addEnchantment(Class<? extends Enchantment> enchantmentClass, ItemStack itemStack, int level) {
		
		Enchantment enchantment = customEnchantmentsIDs.get(enchantmentClass);
		
		if (enchantment == null) {
			return false;
		}
		
		itemStack.addEnchantment(enchantment, level);
		return true;
		
	}
	
	public boolean addEnchantmentFirstLevel(Class<? extends Enchantment> enchantmentClass, ItemStack itemStack) {
		
		Enchantment enchantment = customEnchantmentsIDs.get(enchantmentClass);
		
		if (enchantment == null) {
			return false;
		}
		
		itemStack.addEnchantment(enchantment, enchantment.getStartLevel());
		return true;
		
	}
	
	public boolean addEnchantmentMaxLevel(Class<? extends Enchantment> enchantmentClass, ItemStack itemStack) {
		
		Enchantment enchantment = customEnchantmentsIDs.get(enchantmentClass);
		
		if (enchantment == null) {
			return false;
		}
		
		itemStack.addEnchantment(enchantment, enchantment.getMaxLevel());
		return true;
		
	}
	
	public boolean saveEnchantments() {
		try {
			this.enchantmentsFileConfiguration.save("config.yml");
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
}
