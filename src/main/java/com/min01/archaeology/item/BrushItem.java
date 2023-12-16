package com.min01.archaeology.item;

import java.util.function.Consumer;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import com.min01.archaeology.block.BrushableBlock;
import com.min01.archaeology.blockentity.BrushableBlockEntity;
import com.min01.archaeology.init.ArchaeologyArmPose;
import com.min01.archaeology.init.ArchaeologySounds;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class BrushItem extends Item {
   public static final int ANIMATION_DURATION = 10;
   private static final int USE_DURATION = 200;
   private static final double MAX_BRUSH_DISTANCE = Math.sqrt(ServerGamePacketListenerImpl.MAX_INTERACTION_DISTANCE) - 1.0D;

   public BrushItem(Item.Properties p_272907_) {
      super(p_272907_);
   }

   public InteractionResult useOn(UseOnContext p_272607_) {
      Player player = p_272607_.getPlayer();
      if (player != null && this.calculateHitResult(player).getType() == HitResult.Type.BLOCK) {
         player.startUsingItem(p_272607_.getHand());
      }

      return InteractionResult.CONSUME;
   }

   public UseAnim getUseAnimation(ItemStack p_273490_) {
      return UseAnim.CUSTOM; //UseAnim.BRUSH;
   }
   
   @Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
	   consumer.accept(new IClientItemExtensions() {
		   @Override
		   public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
			   int i = arm == HumanoidArm.RIGHT ? 1 : -1;
			   poseStack.translate((float)i * 0.56F, -0.52F + equipProcess * -0.6F, -0.72F);
			   
			   float f = (float)(player.getUseItemRemainingTicks() % 10);
			   float f1 = f - partialTick + 1.0F;
			   float f2 = 1.0F - f1 / 10.0F;
			   float f7 = -15.0F + 75.0F * Mth.cos(f2 * 2.0F * (float)Math.PI);
			   if (arm != HumanoidArm.RIGHT) {
				   poseStack.translate(0.1D, 0.83D, 0.35D);
				   poseStack.mulPose(Vector3f.XP.rotationDegrees(-80.0F));
				   poseStack.mulPose(Vector3f.YP.rotationDegrees(-90.0F));
				   poseStack.mulPose(Vector3f.XP.rotationDegrees(f7));
				   poseStack.translate(-0.3D, 0.22D, 0.35D);
			   } else {
				   poseStack.translate(-0.25D, 0.22D, 0.35D);
				   poseStack.mulPose(Vector3f.XP.rotationDegrees(-80.0F));
				   poseStack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
				   poseStack.mulPose(Vector3f.ZP.rotationDegrees(0.0F));
				   poseStack.mulPose(Vector3f.XP.rotationDegrees(f7));
			   }
			   return false;
		   }
		   
		   @Override
		   public @Nullable ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
			   return ArchaeologyArmPose.BRUSH;
		   }
	   });
   }

   public int getUseDuration(ItemStack p_272765_) {
      return 200;
   }

   public void onUseTick(Level p_273467_, LivingEntity p_273619_, ItemStack p_273316_, int p_273101_) {
      if (p_273101_ >= 0 && p_273619_ instanceof Player player) {
         HitResult hitresult = this.calculateHitResult(p_273619_);
         if (hitresult instanceof BlockHitResult blockhitresult) {
            if (hitresult.getType() == HitResult.Type.BLOCK) {
               int i = this.getUseDuration(p_273316_) - p_273101_ + 1;
               boolean flag = i % 10 == 5;
               if (flag) {
                  BlockPos blockpos = blockhitresult.getBlockPos();
                  BlockState blockstate = p_273467_.getBlockState(blockpos);
                  HumanoidArm humanoidarm = p_273619_.getUsedItemHand() == InteractionHand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
                  this.spawnDustParticles(p_273467_, blockhitresult, blockstate, p_273619_.getViewVector(0.0F), humanoidarm);
                  Block $$18 = blockstate.getBlock();
                  SoundEvent soundevent;
                  if ($$18 instanceof BrushableBlock) {
                     BrushableBlock brushableblock = (BrushableBlock) $$18;
                     soundevent = brushableblock.getBrushSound();
                  } else {
                     soundevent = ArchaeologySounds.BRUSH_GENERIC.get();
                  }

                  p_273467_.playSound(player, blockpos, soundevent, SoundSource.BLOCKS, 1, 1);
                  if (!p_273467_.isClientSide()) {
                     BlockEntity blockentity = p_273467_.getBlockEntity(blockpos);
                     if (blockentity instanceof BrushableBlockEntity) {
                        BrushableBlockEntity brushableblockentity = (BrushableBlockEntity)blockentity;
                        boolean flag1 = brushableblockentity.brush(p_273467_.getGameTime(), player, blockhitresult.getDirection());
                        if (flag1) {
                           EquipmentSlot equipmentslot = p_273316_.equals(player.getItemBySlot(EquipmentSlot.OFFHAND)) ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
                           p_273316_.hurtAndBreak(1, p_273619_, (p_279044_) -> {
                              p_279044_.broadcastBreakEvent(equipmentslot);
                           });
                        }
                     }
                  }
               }

               return;
            }
         }

         p_273619_.releaseUsingItem();
      } else {
         p_273619_.releaseUsingItem();
      }
   }

   private HitResult calculateHitResult(LivingEntity p_281264_) {
      return getHitResultOnViewVector(p_281264_, (p_281111_) -> {
         return !p_281111_.isSpectator() && p_281111_.isPickable();
      }, MAX_BRUSH_DISTANCE);
   }
   
   public static HitResult getHitResultOnViewVector(Entity p_278281_, Predicate<Entity> p_278306_, double p_278293_) {
	   Vec3 vec3 = p_278281_.getViewVector(0.0F).scale(p_278293_);
	   Level level = p_278281_.level;
	   Vec3 vec31 = p_278281_.getEyePosition();
	   return getHitResult(vec31, p_278281_, p_278306_, vec3, level);
   }

   private static HitResult getHitResult(Vec3 p_278237_, Entity p_278320_, Predicate<Entity> p_278257_, Vec3 p_278342_, Level p_278321_) {
	   Vec3 vec3 = p_278237_.add(p_278342_);
	   HitResult hitresult = p_278321_.clip(new ClipContext(p_278237_, vec3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, p_278320_));
	   if (hitresult.getType() != HitResult.Type.MISS) {
		   vec3 = hitresult.getLocation();
	   }

	   HitResult hitresult1 = ProjectileUtil.getEntityHitResult(p_278321_, p_278320_, p_278237_, vec3, p_278320_.getBoundingBox().expandTowards(p_278342_).inflate(1.0D), p_278257_);
	   if (hitresult1 != null) {
		   hitresult = hitresult1;
	   }

	   return hitresult;
   }

   public void spawnDustParticles(Level p_278327_, BlockHitResult p_278272_, BlockState p_278235_, Vec3 p_278337_, HumanoidArm p_285071_) {
      double d0 = 3.0D;
      int i = p_285071_ == HumanoidArm.RIGHT ? 1 : -1;
      int j = p_278327_.getRandom().nextInt(7, 12);
      BlockParticleOption blockparticleoption = new BlockParticleOption(ParticleTypes.BLOCK, p_278235_);
      Direction direction = p_278272_.getDirection();
      BrushItem.DustParticlesDelta brushitem$dustparticlesdelta = BrushItem.DustParticlesDelta.fromDirection(p_278337_, direction);
      Vec3 vec3 = p_278272_.getLocation();

      for(int k = 0; k < j; ++k) {
         p_278327_.addParticle(blockparticleoption, vec3.x - (double)(direction == Direction.WEST ? 1.0E-6F : 0.0F), vec3.y, vec3.z - (double)(direction == Direction.NORTH ? 1.0E-6F : 0.0F), brushitem$dustparticlesdelta.xd() * (double)i * 3.0D * p_278327_.getRandom().nextDouble(), 0.0D, brushitem$dustparticlesdelta.zd() * (double)i * 3.0D * p_278327_.getRandom().nextDouble());
      }

   }

   static record DustParticlesDelta(double xd, double yd, double zd) {
      private static final double ALONG_SIDE_DELTA = 1.0D;
      private static final double OUT_FROM_SIDE_DELTA = 0.1D;

      public static BrushItem.DustParticlesDelta fromDirection(Vec3 p_273421_, Direction p_272987_) {
         double d0 = 0.0D;
         BrushItem.DustParticlesDelta brushitem$dustparticlesdelta;
         switch (p_272987_) {
            case DOWN:
            case UP:
               brushitem$dustparticlesdelta = new BrushItem.DustParticlesDelta(p_273421_.z(), 0.0D, -p_273421_.x());
               break;
            case NORTH:
               brushitem$dustparticlesdelta = new BrushItem.DustParticlesDelta(1.0D, 0.0D, -0.1D);
               break;
            case SOUTH:
               brushitem$dustparticlesdelta = new BrushItem.DustParticlesDelta(-1.0D, 0.0D, 0.1D);
               break;
            case WEST:
               brushitem$dustparticlesdelta = new BrushItem.DustParticlesDelta(-0.1D, 0.0D, -1.0D);
               break;
            case EAST:
               brushitem$dustparticlesdelta = new BrushItem.DustParticlesDelta(0.1D, 0.0D, 1.0D);
               break;
            default:
               throw new IncompatibleClassChangeError();
         }

         return brushitem$dustparticlesdelta;
      }
   }
}