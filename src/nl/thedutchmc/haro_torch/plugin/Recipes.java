package nl.thedutchmc.haro_torch.plugin;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class Recipes {

	private HaroTorch plugin;
	
	public Recipes(HaroTorch plugin) {
		this.plugin = plugin;
	}
	
	    public ShapedRecipe getHaroTorchRecipe() {
	        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, "Haro_Torch"), getHaroTorch(1));
	        recipe.shape(" m ", " a ", " a ");
	        recipe.setIngredient('m', Material.NETHER_STAR);
	        recipe.setIngredient('a', Material.DIAMOND_BLOCK);
	        return recipe;
	    }
	 
	 public ItemStack getHaroTorch(int count) {
	        ItemStack miniDiamond = new ItemStack(Material.TORCH);
	        ItemMeta meta = miniDiamond.getItemMeta();
	        ArrayList<String> lore = new ArrayList<String>();
	        meta.getLore();
	        lore.add("This isn't a normal torch!");
	        lore.add("well it is in some ways");
	        lore.add("but it's not!");
	        meta.setLore(lore);
	        meta.setDisplayName(ChatColor.AQUA + "Haro's Torch");
	        miniDiamond.setItemMeta(meta);
	        miniDiamond.setAmount(count);
	        return miniDiamond;
	    }

}
