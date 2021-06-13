package nl.thedutchmc.harotorch.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import dev.array21.classvalidator.annotations.Required;
import nl.thedutchmc.harotorch.HaroTorch;
import nl.thedutchmc.harotorch.annotations.Nullable;

public class ConfigManifest {
	
	/**
	 * What block to use as the HaroTorch, this does not have to be a torch
	 */
	@Required
	public String torchBlock;
	
	/**
	 * What language the user wants to use
	 */
	@Required
	public String activeLang;
	
	/**
	 * Statistics UUID, managed by PluginStatLib
	 */
	@Nullable
	public String statUuid;
	
	/**
	 * Should there be particles around the Torch when it is placed in the world
	 */
	@Required
	public Boolean enableTorchParticles;
	
	/**
	 * Should people be able to remove other people's torches
	 */
	@Required
	public Boolean allowRemoveNotOwnedTorch;
	
	/**
	 * Should only hostile mobs be prevented from spawning
	 * Withers and Ender Dragons are unaffected.
	 */
	@Required
	public Boolean onlyBlockHostileMobs;
	
	/**
	 * True if statistics should not be collected.
	 */
	@Nullable
	public Boolean disableStat;
	
	/**
	 * True if the Torch shape should be a circle.
	 * False if it should be a square.
	 */
	@Nullable
	private Boolean shapeCircle;
	
	/**
	 * What should the range of the Torch be
	 */
	@Required
	public Integer torchRange;
	
	/**
	 * What radius should we use to highlight Torches
	 */
	@Required
	public Integer torchHighlightRange;
	
	/**
	 * For how long should the Torches be highlighted. In seconds
	 */
	@Required
	public Integer torchHighlightTime;
	
	/**
	 * How many 'rows' of particles should be spawned when a user uses /torch aoe
	 */
	@Required
	public Integer torchAoeParticleHeight;
	
	/**
	 * Command cooldown in seconds. Default: 30. -1 to disable.
	 */
	@Required
	public Integer commandCooldown;
	
	/**
	 * How many torches are players allowed to place down. -1 to disable
	 */
	@Required
	public Integer torchPlaceLimit;
	
	/**
	 * The shape of the crafting recipe for a HaroTorch
	 */
	@Required
	public String[] recipeShape;
	
	/**
	 * The keys used in {@link #recipeShape} mapped to a material.
	 */
	@Required
	private String[] recipeKeys;
	
	/**
	 * What mobs should be excluded from the block list
	 */
	@Nullable
	private String[] mobsExcludeFromBlockList;
	
	/**
	 * Returns if Statistics are enabled
	 * @return True if statistics are enabled
	 */
	public boolean isStatEnabled() {
		if(this.disableStat != null && !this.disableStat ) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Get in what Shape the torch radius should be calculated
	 * @return Returns the TorchRangeShape
	 */
	public TorchRangeShape getTorchRangeShape() {
		if(this.shapeCircle == null) {
			return TorchRangeShape.CIRCLE;
		}
		
		if(this.shapeCircle) {
			return TorchRangeShape.CIRCLE;
		}
		
		return TorchRangeShape.SQUARE;
	}
	
	/**
	 * Get a HashMap of recipe keys
	 * @return Returns a HashMap of recipe keys
	 */
	public HashMap<Character, Material> getRecipeKeys() {
		HashMap<Character, Material> result = new HashMap<>(this.recipeKeys.length);
		for(String str : this.recipeKeys) {
			String[] parts = str.split("<-->");
			
			if(parts.length != 2) {
				HaroTorch.logWarn(String.format("Invalid configuration file. Recipe key '%s' is of an invalid format.", str));
			}
			
			char key = parts[0].toCharArray()[0];
			Material m = Material.matchMaterial(parts[1]);
			
			if(m == null) {
				HaroTorch.logWarn(String.format("Invalid configuration file. Recipe key '%s' contains an invalid Material.", str));
			}
			
			result.put(key, m);
		}
		
		return result;
	}
	
	/**
	 * Get a list of excluded EntityType's
	 * @return Returns a List of excluded EntityType's
	 */
	public List<EntityType> getExcludedEntities() {
		if(this.mobsExcludeFromBlockList == null) {
			return new ArrayList<>(0);
		}
		
		List<EntityType> result = new ArrayList<>(this.mobsExcludeFromBlockList.length);
		
		for(String str : this.mobsExcludeFromBlockList) {
			try {
				EntityType et = EntityType.valueOf(str);
				result.add(et);
			} catch(IllegalArgumentException e) {
				HaroTorch.logWarn("Provided mob type " + str + " is not valid. Please check your configuration file!");
			}
		}
		
		return result;
	}
	
	/**
	 * Get the shape of the HaroTorch recipe
	 * @return Returns the recipeShape as a List
	 */
	public List<String> getRecipeShape() {
		return Arrays.asList(this.recipeShape);
	}
	
	public enum TorchRangeShape {
		CIRCLE,
		SQUARE
	}
}
