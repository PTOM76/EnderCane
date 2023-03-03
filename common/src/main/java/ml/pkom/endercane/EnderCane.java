package ml.pkom.endercane;

import ml.pkom.mcpitanlibarch.api.event.item.ItemUseEvent;
import ml.pkom.mcpitanlibarch.api.event.item.ItemUseOnBlockEvent;
import ml.pkom.mcpitanlibarch.api.item.ExtendItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;

public class EnderCane extends ExtendItem {
    public EnderCane(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> onRightClick(ItemUseEvent event) {
        return super.onRightClick(event);
    }

    @Override
    public ActionResult onRightClickOnBlock(ItemUseOnBlockEvent event) {
        return super.onRightClickOnBlock(event);
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        return super.getItemBarStep(stack);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        float f = Math.max(0.0F, ((float)this.getMaxDamage() - (float)stack.getDamage()) / (float)this.getMaxDamage());
        return MathHelper.hsvToRgb(f / 3.0F + 3.0F, 1.0F, 1.0F);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return true;
    }
}
