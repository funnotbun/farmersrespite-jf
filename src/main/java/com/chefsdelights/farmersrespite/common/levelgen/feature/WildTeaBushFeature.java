package com.chefsdelights.farmersrespite.common.levelgen.feature;

import com.chefsdelights.farmersrespite.core.registry.FRBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;

public class WildTeaBushFeature implements Feature {
    public static final MapCodec<WildTeaBushFeature> CODEC = MapCodec.unit(new WildTeaBushFeature());

    @Override
    public MapCodec<? extends Feature> codec() {
        return CODEC;
    }

    @Override
    public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin) {
        BlockState blockstate = FRBlocks.WILD_TEA_BUSH.defaultBlockState();
        BlockPos pos = origin;
        if (blockstate.canSurvive(level, pos)) {
            level.setBlock(pos, blockstate, 19);
            return true;
        }
        return false;
    }
}
