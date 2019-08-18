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
	
	//Shaped recipe for the HaroTorch, takes two diamond blocks, and one nether star in the center column
    public ShapedRecipe getHaroTorchRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, "Haro_Torch"), getHaroTorch(1));
        recipe.shape("ddd", "dnd", "ooo");
        recipe.setIngredient('n', Material.NETHER_STAR);
        recipe.setIngredient('d', Material.DIAMOND);
        recipe.setIngredient('o', Material.OBSIDIAN);
        return recipe;
    }
	 
    //The HaroTorch ItemStack, with properties to keep it seperate from a normal torch.
    public ItemStack getHaroTorch(int count) {
    	ItemStack torch = new ItemStack(Material.WHITE_STAINED_GLASS);
        ItemMeta meta = torch.getItemMeta();
        ArrayList<String> lore = new ArrayList<String>();
        meta.getLore();
        lore.add("This isn't a normal torch!");
        lore.add("well it is in some ways");
        lore.add("but it's not!");
        meta.setLore(lore);
        meta.setDisplayName(ChatColor.AQUA + "Haro's Torch");
        torch.setItemMeta(meta);
        torch.setAmount(count);
        return torch;
    }
}