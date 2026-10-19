package com.teamdimensional.partbuilderjei.integration.jei;

import java.util.List;

import com.teamdimensional.partbuilderjei.Tags;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class PartBuilderCategory implements IRecipeCategory<PartBuilderWrapper> {

	public static final String UID = "pbjei.part_builder";
	private final IDrawable background, arrowBackground, slot;
    private final int SLOT_SIZE = 18, SLOT_MARGIN = 0, ARROW_MARGIN = 4, IMAGE_MARGIN = 2;

	public PartBuilderCategory(IGuiHelper guiHelper) {
		ResourceLocation texture = new ResourceLocation("jei", "textures/gui/gui_vanilla.png");
		slot = guiHelper.getSlotDrawable();
		arrowBackground = guiHelper.createDrawable(texture, 24, 132, 24, 17);
		background = guiHelper.createBlankDrawable(IMAGE_MARGIN * 2 + ARROW_MARGIN * 2 + SLOT_SIZE * 3 + arrowBackground.getWidth(), IMAGE_MARGIN * 3 + SLOT_SIZE * 2);
	}

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        slot.draw(minecraft, IMAGE_MARGIN, IMAGE_MARGIN);
        slot.draw(minecraft, IMAGE_MARGIN + SLOT_SIZE + ARROW_MARGIN - (SLOT_SIZE - arrowBackground.getWidth()) / 2, IMAGE_MARGIN * 2 + SLOT_SIZE);
        slot.draw(minecraft, IMAGE_MARGIN + SLOT_SIZE + ARROW_MARGIN * 2 + arrowBackground.getWidth(), IMAGE_MARGIN);
        slot.draw(minecraft, IMAGE_MARGIN + SLOT_SIZE * 2 + ARROW_MARGIN * 2 + arrowBackground.getWidth(), IMAGE_MARGIN);
        arrowBackground.draw(minecraft, IMAGE_MARGIN + SLOT_SIZE + ARROW_MARGIN, IMAGE_MARGIN);
    }

    @Override
    public String getModName() {
        return Tags.MOD_NAME;
    }

    @Override
    public String getTitle() {
        return I18n.format("pbjei.jei.part_builder");
    }

    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public void setRecipe(IRecipeLayout layout, PartBuilderWrapper recipe, IIngredients ingredients) {
        IGuiItemStackGroup itemStacks = layout.getItemStacks();

        itemStacks.init(0, true, IMAGE_MARGIN, IMAGE_MARGIN + SLOT_MARGIN);
        itemStacks.init(1, true, IMAGE_MARGIN + SLOT_SIZE + ARROW_MARGIN + SLOT_MARGIN - (SLOT_SIZE - arrowBackground.getWidth()) / 2, IMAGE_MARGIN * 2 + SLOT_SIZE + SLOT_MARGIN);
        itemStacks.init(2, false, IMAGE_MARGIN + SLOT_SIZE + ARROW_MARGIN * 2 + SLOT_MARGIN + arrowBackground.getWidth(), IMAGE_MARGIN + SLOT_MARGIN);
        itemStacks.init(3, false, IMAGE_MARGIN + SLOT_SIZE * 2 + ARROW_MARGIN * 2 + SLOT_MARGIN + arrowBackground.getWidth(), IMAGE_MARGIN + SLOT_MARGIN);

        List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
        List<List<ItemStack>> outputs = ingredients.getOutputs(VanillaTypes.ITEM);
        itemStacks.set(0, inputs.get(0));
        itemStacks.set(1, inputs.get(1));
        itemStacks.set(2, outputs.get(0));
        itemStacks.set(3, outputs.get(1));

		layout.getItemStacks().addTooltipCallback(recipe);
    }
    
}
