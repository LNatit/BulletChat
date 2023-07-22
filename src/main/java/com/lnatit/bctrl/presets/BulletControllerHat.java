package com.lnatit.bctrl.presets;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;

public class BulletControllerHat extends Item implements Equipable
{
    public static final String ITEM_NAME = "bullet_vision";

    public BulletControllerHat()
    {
        super(new Properties().stacksTo(1).rarity(Rarity.RARE));
    }

    @Override
    public EquipmentSlot getEquipmentSlot()
    {
        return EquipmentSlot.HEAD;
    }
}
