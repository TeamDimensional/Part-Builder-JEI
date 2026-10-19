package com.teamdimensional.partbuilderjei.integration.jei;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.tools.TinkerTools;
import slimeknights.tconstruct.tools.common.block.BlockToolTable;

@JEIPlugin
public class PBJEIPlugin implements IModPlugin {

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		registry.addRecipeCategories(new PartBuilderCategory(registry.getJeiHelpers().getGuiHelper()));
	}

	@Override
	public void register(@Nonnull IModRegistry registry) {
		registry.handleRecipes(PartBuilderWrapper.class, x -> x, PartBuilderCategory.UID);
		List<PartBuilderWrapper> objects = TinkerRegistry.getAllMaterials()
			.stream()
			.filter(t -> t.isCraftable())
			.flatMap(t -> TinkerRegistry.getToolParts()
				.stream()
				.filter(y -> y.canUseMaterial(t) && y instanceof Item && y.canBeCrafted())
				.map(y -> new PartBuilderWrapper(t, (Item & IToolPart) y)))
			.collect(Collectors.toList());
		registry.addRecipes(objects, PartBuilderCategory.UID);
		NonNullList<ItemStack> list = NonNullList.create();
		TinkerTools.toolTables.getSubBlocks(null, list);
		for (ItemStack stack : list) {
			if (stack.getMetadata() == BlockToolTable.TableTypes.PartBuilder.meta) {
				registry.addRecipeCatalyst(stack, PartBuilderCategory.UID);
			}
		}
    }

}
