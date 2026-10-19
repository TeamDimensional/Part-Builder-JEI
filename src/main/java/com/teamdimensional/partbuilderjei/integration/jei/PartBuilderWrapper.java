package com.teamdimensional.partbuilderjei.integration.jei;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.teamdimensional.partbuilderjei.PartBuilderJEI;

import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.library.tools.Pattern;
import slimeknights.tconstruct.tools.TinkerTools;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class PartBuilderWrapper implements IRecipeWrapper, ITooltipCallback<ItemStack> {

    final ItemStack pattern;
    final ItemStack inputItem;
    final ItemStack outputItem;
    final ItemStack outputShard;

    public <T extends Item & IToolPart> PartBuilderWrapper(Material material, T part) {
        this.inputItem = material.getRepresentativeItem().copy();
        Optional<RecipeMatch.Match> matchOptional = material.matches(new ItemStack[]{inputItem});
        if (!matchOptional.isPresent()) {
            throw new IllegalArgumentException("Material " + material + " has no representative item");
        }
        int itemCount = (int) Math.ceil(((double) part.getCost()) / matchOptional.get().amount);
        this.inputItem.setCount(itemCount);
        this.outputItem = part.getItemstackWithMaterial(material);
        int surplusCost = itemCount * matchOptional.get().amount - part.getCost();
        if (surplusCost > 0) {
            if (surplusCost % Material.VALUE_Shard != 0) {
                PartBuilderJEI.LOGGER.warn("Material {}, part {}: surplus amount is not divisible by shard cost");
            }
            outputShard = material.getShard().copy();
            if (!outputShard.isEmpty()) {
                outputShard.setCount(surplusCost / Material.VALUE_Shard);
            }
        } else {
            outputShard = ItemStack.EMPTY;
        }
        pattern = new ItemStack(TinkerTools.pattern);
        Pattern.setTagForPart(pattern, part);
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        List<ItemStack> inputs = new ArrayList<>(), outputs = new ArrayList<>();
        inputs.add(inputItem);
        inputs.add(pattern);
        outputs.add(outputItem);
        outputs.add(outputShard);
        ingredients.setInputs(VanillaTypes.ITEM, inputs);
        ingredients.setOutputs(VanillaTypes.ITEM, outputs);
    }

    @Override
    public void onTooltip(int index, boolean isInput, ItemStack item, List<String> tooltips) {
        if (index == 1 && isInput) {
            tooltips.add(I18n.format("pbjei.jei.part_builder.not_consumed"));
        }
    }
    
}
