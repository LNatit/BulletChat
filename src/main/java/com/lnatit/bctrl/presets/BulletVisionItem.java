package com.lnatit.bctrl.presets;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class BulletVisionItem extends ArmorItem implements Equipable
{
    public static final String ITEM_NAME = "bullet_vision";
    // TODO define item tags in json
    public static final TagKey<Item> CTRL_TAG = ItemTags.create(new ResourceLocation("bullet_vision"));

    public BulletVisionItem()
    {
        super(NotArmor.MATERIAL, Type.HELMET, new Properties().stacksTo(1).rarity(Rarity.RARE));
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

    public static class NotArmor implements ArmorMaterial
    {
        public static final NotArmor MATERIAL = new NotArmor();

        private NotArmor()
        {}

        @Override
        public int getDurabilityForType(@NotNull Type type)
        {
            return 0;
        }

        @Override
        public int getDefenseForType(@NotNull Type type)
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
}
