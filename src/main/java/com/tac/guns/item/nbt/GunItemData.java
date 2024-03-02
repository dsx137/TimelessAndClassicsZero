package com.tac.guns.item.nbt;

import com.tac.guns.GunMod;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GunItemData {
    public static final ResourceLocation DEFAULT = new ResourceLocation(GunMod.MOD_ID, "ak47");
    public static final ResourceLocation DEFAULT_DISPLAY = new ResourceLocation(GunMod.MOD_ID, "ak47_display");
    public static final String GUN_ID_TAG = "GunId";
    @Nullable
    private ResourceLocation gunId = null;

    public static void serialization(@Nonnull CompoundTag nbt, @Nonnull GunItemData data) {
        if (data.gunId != null) {
            nbt.putString(GUN_ID_TAG, data.gunId.toString());
        }
    }

    public static @Nonnull GunItemData deserialization(@Nonnull CompoundTag nbt) {
        GunItemData data = new GunItemData();
        if (nbt.contains(GUN_ID_TAG, Tag.TAG_STRING)) {
            data.gunId = ResourceLocation.tryParse(nbt.getString(GUN_ID_TAG));
        }
        return data;
    }

    @Nonnull
    public ResourceLocation getGunId() {
        if (this.gunId == null) {
            return DEFAULT;
        }
        return gunId;
    }

    public void setGunId(@Nullable ResourceLocation gunId) {
        this.gunId = gunId;
    }
}
