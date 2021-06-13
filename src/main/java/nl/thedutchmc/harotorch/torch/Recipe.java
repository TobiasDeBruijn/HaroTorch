package nl.thedutchmc.harotorch.torch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import nl.thedutchmc.harotorch.HaroTorch;

public class Recipe {

	private HaroTorch plugin;
	
	public Recipe(HaroTorch plugin) {
		this.plugin = plugin;
	}
	
	public ShapedRecipe getTorchRecipe() {
		HashMap<Character, Material> keys = this.plugin.getConfigManifest().getRecipeKeys();
		List<String> shape = this.plugin.getConfigManifest().getRecipeShape();
		
		ItemStack stack = TorchHandler.getTorch(1);
		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, "haro_torch"), stack);
		
		int recipeShapeSize = shape.size();
			
		if(recipeShapeSize == 1) {
			recipe.shape(shape.get(0));
		} else if(recipeShapeSize == 2) {
			recipe.shape(shape.get(0), shape.get(1));
		} else if(recipeShapeSize == 3) {
			recipe.shape(shape.get(0), shape.get(1), shape.get(2));
		}
		
		for(Map.Entry<Character, Material> entry : keys.entrySet()) {
			
			char c = entry.getKey();
			Material m = entry.getValue();
			
			recipe.setIngredient(c, m);
		}
		
		return recipe;
	}
}
