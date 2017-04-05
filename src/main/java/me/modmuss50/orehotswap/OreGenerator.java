package me.modmuss50.orehotswap;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import me.modmuss50.orehotswap.config.OreConfig;
import me.modmuss50.orehotswap.lib.ChunkCoord;
import net.minecraft.block.Block;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

/**
 * Created by mark on 28/03/16.
 */
public class OreGenerator {

    private static final Set<ChunkCoord> completedChunks = Sets.newHashSet();
    public final ArrayList<ChunkCoord> chunksToRetroGen = new ArrayList<ChunkCoord>();
    public final HashMap<ChunkCoord, OreConfig> oreTypeHashMap = new HashMap<>();

    public void markChunk(ChunkCoord coord) {
        completedChunks.add(coord);
        chunksToRetroGen.remove(coord);
    }

    private boolean isTickEligibleForRetroGen(TickEvent.WorldTickEvent event) {
        return event.phase == TickEvent.Phase.END || event.side == Side.SERVER;
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        if (isTickEligibleForRetroGen(event)) {
            if (!chunksToRetroGen.isEmpty()) {
                ChunkCoord coord = chunksToRetroGen.get(0);
                OreHotSwap.logHelper.debug("Regenerating ore in " + coord + '.' + event.world.provider.getDimension());
                final World world = event.world;
                if (world.provider.getDimension() == oreTypeHashMap.get(coord).world) {
                    final long seed = world.getSeed();
                    final Random rng = new Random(seed);
                    final long xSeed = rng.nextLong() >> 2 + 1L;
                    final long zSeed = rng.nextLong() >> 2 + 1L;
                    final long chunkSeed = (xSeed * coord.getX() + zSeed * coord.getZ()) * seed;
                    rng.setSeed(chunkSeed);
                    this.generate(rng, coord.getX(), coord.getZ(), world, oreTypeHashMap.get(coord));
                    markChunk(coord);
                }
            }
        }
    }

    //TODO THIS WITH THE COMMAND
//    @SubscribeEvent
//    public void onChunkLoad(ChunkDataEvent.Load event)
//    {
//        final ChunkCoord coord = ChunkCoord.of(event);
//        OreHotSwap.logHelper.info("Queueing retro ore gen for " + coord + '.');
//        chunksToRetroGen.addLast(coord);
//    }

    public void addChunk(ChunkCoord coord, OreConfig config) {
        chunksToRetroGen.add(coord);
        oreTypeHashMap.put(coord, config);
    }

    @SubscribeEvent
    public void onChunkSave(ChunkDataEvent.Save event) {
        final ChunkCoord coord = ChunkCoord.of(event);
        if (completedChunks.contains(coord)) {
            completedChunks.remove(coord);
        }
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("chunksToRetroGen", chunksToRetroGen).toString();
    }

    public void generate(Random random, int chunkX, int chunkZ, World world, OreConfig config) {
        Block block = GameData.getBlockRegistry().getObject(config.getBlock());
        Block block2 = GameData.getBlockRegistry().getObject(config.getReplacementBlock());
        if(block2 == null){
            block2 = Blocks.STONE;
        }
        WorldGenMinable worldGenMinable = new WorldGenMinable(block.getDefaultState(), config.veinSize, BlockMatcher.forBlock(block2));
        int xPos, yPos, zPos;
        for (int i = 0; i < config.veinsPerChunk; i++) {
            xPos = chunkX * 16 + random.nextInt(16);
            yPos = 10 + random.nextInt(config.maxYHeight - config.minYHeight);
            zPos = chunkZ * 16 + random.nextInt(16);
            worldGenMinable.generate(world, random, new BlockPos(xPos, yPos, zPos));
        }
    }
}
