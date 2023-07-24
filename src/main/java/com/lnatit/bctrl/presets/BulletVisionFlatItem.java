package com.lnatit.bctrl.presets;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BulletVisionFlatItem extends ArmorItem implements Equipable
{
    public static final String ITEM_NAME = "bullet_vision_flat";

    public BulletVisionFlatItem()
    {
        super(NotArmor.MATERIAL, Type.HELMET, new Properties().stacksTo(1).rarity(Rarity.RARE));
    }

    @Override
    public EquipmentSlot getEquipmentSlot()
    {
        return EquipmentSlot.HEAD;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        return this.swapWithEquipmentSlot(this, level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> lines, TooltipFlag flag)
    {
        lines.add(Component.translatable("tooltip.bctrl.vision_desc"));
        lines.add(Component.translatable("tooltip.bctrl.vision_meme"));
    }

    @Override
    public int getDefaultTooltipHideFlags(ItemStack stack)
    {
        return 0b0010;
    }

    public static class NotArmor implements ArmorMaterial
    {
        public static final NotArmor MATERIAL = new NotArmor();

        private NotArmor()
        {}

        @Override
        public int getDurabilityForType(Type type)
        {
            return 0;
        }

        @Override
        public int getDefenseForType(Type type)
        {
            return 0;
        }

        @Override
        public int getEnchantmentValue()
        {
            return 0;
        }

        @Override
        public SoundEvent getEquipSound()
        {
            return SoundEvents.ARMOR_EQUIP_NETHERITE;
        }

        @Override
        public Ingredient getRepairIngredient()
        {
            return Ingredient.EMPTY;
        }

        @Override
        public String getName()
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
}

