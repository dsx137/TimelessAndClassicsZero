package com.tac.guns.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.tac.guns.GunMod;
import com.tac.guns.block.GunSmithTableBlock;
import com.tac.guns.block.entity.GunSmithTableBlockEntity;
import com.tac.guns.client.model.bedrock.BedrockModel;
import com.tac.guns.client.resource.InternalAssetLoader;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

import java.util.Optional;

public class GunSmithTableRenderer implements BlockEntityRenderer<GunSmithTableBlockEntity> {
    public static final ResourceLocation MODEL_LOCATION = new ResourceLocation(GunMod.MOD_ID, "models/bedrock/gun_smith_table.json");
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(GunMod.MOD_ID, "textures/block/gun_smith_table.png");

    public GunSmithTableRenderer(BlockEntityRendererProvider.Context context) {
    }

    public static Optional<BedrockModel> getModel() {
        return InternalAssetLoader.getBedrockModel(MODEL_LOCATION);
    }

    public static ResourceLocation getTextureLocation() {
        return TEXTURE_LOCATION;
    }

    @Override
    public void render(GunSmithTableBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        getModel().ifPresent(model -> {
            BlockState blockState = blockEntity.getBlockState();
            if (blockState.getValue(GunSmithTableBlock.PART).equals(BedPart.HEAD)) {
                return;
            }
            Direction facing = blockState.getValue(GunSmithTableBlock.FACING);
            poseStack.pushPose();
            poseStack.translate(0.5, 1.5, 0.5);
            poseStack.mulPose(Vector3f.ZN.rotationDegrees(180));
            poseStack.mulPose(Vector3f.YN.rotationDegrees(90 - facing.get2DDataValue() * 90));
            RenderType renderType = RenderType.entityTranslucent(TEXTURE_LOCATION);
            model.render(poseStack, ItemTransforms.TransformType.NONE, renderType, combinedLightIn, combinedOverlayIn);
            poseStack.popPose();
        });
    }

    @Override
    public boolean shouldRenderOffScreen(GunSmithTableBlockEntity blockEntity) {
        return true;
    }
}