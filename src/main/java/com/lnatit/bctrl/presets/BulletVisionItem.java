package com.lnatit.bctrl.presets;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static com.lnatit.bctrl.BulletChatController.MODLOG;
import static net.minecraft.world.level.block.Blocks.GLASS;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BulletVisionItem extends BlockItem implements Equipable
{
    public static final String ITEM_NAME = "bullet_vision";

    public BulletVisionItem()
    {
        super(GLASS, new Properties().stacksTo(1).rarity(Rarity.EPIC));
    }

    @Override
    public InteractionResult useOn(UseOnContext context)
    {
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult place(BlockPlaceContext context)
    {
        MODLOG.warn("Who the f**k is trying to place Bullet Vision???");
        return InteractionResult.PASS;
    }

    @Override
    protected boolean canPlace(BlockPlaceContext context, BlockState state)
    {
        return false;
    }

    @Override
    public EquipmentSlot getEquipmentSlot()
    {
        return EquipmentSlot.HEAD;
    }

    @Override
    public SoundEvent getEquipSound()
    {
        return SoundEvents.ARMOR_EQUIP_NETHERITE;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        return this.swapWithEquipmentSlot(this, level, player, hand);
    }

    @Override
    public String getDescriptionId()
    {
        return this.getOrCreateDescriptionId();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> lines, TooltipFlag flag)
    {
        lines.add(Component.translatable("tooltip.bctrl.vision_desc"));
        lines.add(Component.translatable("tooltip.bctrl.vision_upgraded"));
        lines.add(Component.translatable("tooltip.bctrl.vision_meme"));
    }
}
