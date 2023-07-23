package com.lnatit.bctrl.presets;

import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.world.level.block.Blocks.GLASS;

// TODO overwrite BlockItem logic
public class BulletVisionItem extends BlockItem implements Equipable
{
    public static final String ITEM_NAME = "bullet_vision";

    public BulletVisionItem()
    {
//        super(NotArmor.MATERIAL, Type.HELMET, new Properties().stacksTo(1).rarity(Rarity.RARE));
        super(GLASS, new Properties().stacksTo(1).rarity(Rarity.RARE));
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context)
    {
        return InteractionResult.PASS;
    }

    @Override
    public @NotNull EquipmentSlot getEquipmentSlot()
    {
        return EquipmentSlot.HEAD;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand)
    {
        return this.swapWithEquipmentSlot(this, level, player, hand);
    }

    @Override
    public @NotNull String getDescriptionId()
    {
        return this.getOrCreateDescriptionId();
    }

    public static class NotArmor implements ArmorMaterial
    {
        public static final NotArmor MATERIAL = new NotArmor();

        private NotArmor()
        {}

        @Override
        public int getDurabilityForType(@NotNull ArmorItem.Type type)
        {
            return 0;
        }

        @Override
        public int getDefenseForType(@NotNull ArmorItem.Type type)
        {
            return 0;
        }

        @Override
        public int getEnchantmentValue()
        {
            return 0;
        }

        @Override
        public @NotNull SoundEvent getEquipSound()
        {
            return SoundEvents.ARMOR_EQUIP_NETHERITE;
        }

        @Override
        public @NotNull Ingredient getRepairIngredient()
        {
            return Ingredient.EMPTY;
        }

        @Override
        public @NotNull String getName()
        {
            return "vision";
        }

        @Override
        public float getToughness()
        {
            return 0;
        }

        @Override
        public float getKnockbackResistance()
        {
            return 0;
        }
    }

    public static class RenderProperty implements IClientItemExtensions
    {
        @Override
        public BlockEntityWithoutLevelRenderer getCustomRenderer()
        {
            return IClientItemExtensions.super.getCustomRenderer();
        }

        public static class VisionRenderer extends BlockEntityWithoutLevelRenderer
        {
            public VisionRenderer(BlockEntityRenderDispatcher p_172550_, EntityModelSet p_172551_)
            {
                super(p_172550_, p_172551_);
            }
        }
    }
}
