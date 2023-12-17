package com.min01.archaeology.init;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.IArmPoseTransformer;

public class ArchaeologyArmPose implements IArmPoseTransformer
{
	public static HumanoidModel.ArmPose BRUSH;
	
	@Override
	public void applyTransform(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm) 
	{
		if(arm == HumanoidArm.LEFT) 
		{
			model.leftArm.xRot = model.leftArm.xRot * 0.5F - ((float)Math.PI / 5F);
            model.leftArm.yRot = 0.0F;
		}
		else if(arm == HumanoidArm.RIGHT)
		{
			model.rightArm.xRot = model.rightArm.xRot * 0.5F - ((float)Math.PI / 5F);
            model.rightArm.yRot = 0.0F;
		}
	}
}
