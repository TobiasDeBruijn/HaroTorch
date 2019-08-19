package nl.thedutchmc.haro_torch.plugin;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import nl.thedutchmc.haro_torch.plugin.torch.TorchHandler;

public class Recipes {

	private HaroTorch plugin;

	public Recipes(HaroTorch plugin) {
		this.plugin = plugin;
	}

	// Shaped recipe for the HaroTorch, takes two diamond blocks, and one nether
	// star in the center column
	public ShapedRecipe getHaroTorchRecipe() {
		ItemStack stack = TorchHandler.getNewTorch(1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, "Haro_Torch"), stack);
		recipe.shape("ddd", "dnd", "ooo");
		recipe.setIngredient('n', Material.NETHER_STAR);
		recipe.setIngredient('d', Material.DIAMOND);
		recipe.setIngredient('o', Material.OBSIDIAN);
		return recipe;
	}
}