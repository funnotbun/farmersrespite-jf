package com.chefsdelights.farmersrespite.common.block;

import com.chefsdelights.farmersrespite.core.FarmersRespite;
import com.chefsdelights.farmersrespite.core.registry.FRBlocks;
import com.chefsdelights.farmersrespite.core.registry.FRDamageSources;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WitherRootsBlock extends BushBlock implements BonemealableBlock {
    private static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);
    private static final VoxelShape SHAPE_SMALL = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 6.0D, 14.0D);
    //public static final DamageSource WITHER_ROOTS = new DamageSource(FarmersRespite.MOD_ID + ".witherRoots");

    public WitherRootsBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state == FRBlocks.WITHER_ROOTS.defaultBlockState()) {
            return SHAPE_SMALL;
        }
        return SHAPE;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos blockpos = pos.below();
        if (state.getBlock() == this)
            return level.getBlockState(blockpos).canSurvive(level, blockpos);
        return true;
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess tickAccess, BlockPos pos, Direction facing, BlockPos facingPos, BlockState facingState, RandomSource random) {
        if (!state.canSurvive(level, pos)) {
            if (level instanceof Level realLevel) {
                AreaEffectCloud cloud = new AreaEffectCloud(realLevel, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
                cloud.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 0));
                cloud.setDuration(600);
                cloud.setRadius(0.5F);
                cloud.setRadiusOnUse(-0.5F);
                realLevel.addFreshEntity(cloud);
            }
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, level, tickAccess, pos, facing, facingPos, facingState, random);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean bl) {
        if (entity instanceof LivingEntity) {
            entity.makeStuckInBlock(state, new Vec3(0.8F, 0.75D, 0.8F));
            if (!level.isClientSide() && (entity.xOld != entity.getX() || entity.zOld != entity.getZ())) {
                double d0 = Math.abs(entity.getX() - entity.xOld);
                double d1 = Math.abs(entity.getZ() - entity.zOld);
                if (d0 >= 0.003F || d1 >= 0.003F) {
                    entity.hurt(level.damageSources().source(FRDamageSources.WITHER_ROOTS), 1.0F);
                    ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 0));
                }
            }
        }
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide()) {
            AreaEffectCloud cloud = new AreaEffectCloud(level, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
            cloud.addEffect(new MobEffectInstance(MobEffects.WITHER, 200, 0));
            cloud.setDuration(600);
            cloud.setRadius(0.5F);
            cloud.setRadiusOnUse(-0.5F);
            cloud.setOwner(player);
            level.addFreshEntity(cloud);
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader p_50897_, BlockPos p_50898_, BlockState p_50899_, BonemealSource source) {
        return false;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos blockPos, BlockState blockState, BonemealSource source) {
        return false;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel, RandomSource randomSource, BlockPos blockPos, BlockState blockState, BonemealSource source) {

    }

}
