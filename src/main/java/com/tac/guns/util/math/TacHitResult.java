package com.tac.guns.util.math;

import com.tac.guns.entity.EntityBullet;
import net.minecraft.world.phys.EntityHitResult;

public class TacHitResult extends EntityHitResult {
    private final boolean headshot;

    public TacHitResult(EntityBullet.EntityResult result) {
        super(result.getEntity(), result.getHitPos());
        this.headshot = result.isHeadshot();
    }

    public boolean isHeadshot() {
        return this.headshot;
    }
}