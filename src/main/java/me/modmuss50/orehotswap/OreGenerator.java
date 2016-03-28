package me.modmuss50.orehotswap;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import me.modmuss50.orehotswap.config.OreConfig;
import me.modmuss50.orehotswap.lib.ChunkCoord;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.event.world.ChunkDataEvent;

import java.util.*;

/**
 * Created by mark on 28/03/16.
 */
public class OreGenerator {

    private static final Set<ChunkCoord> completedChunks = Sets.newHashSet();
    public final Deque<ChunkCoord> chunksToRetroGen = new ArrayDeque<ChunkCoord>(64);
    public final HashMap<ChunkCoord, OreConfig> oreTypeHasMap = new HashMap<>();

    public void markChunk(ChunkCoord coord)
    {
        completedChunks.add(coord);
    }

    private boolean isTickEligibleForRetroGen(TickEvent.WorldTickEvent event)
    {
        return event.phase == TickEvent.Phase.END || event.side == Side.SERVER;
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event)
    {
        if (isTickEligibleForRetroGen(event))
        {
            if (!chunksToRetroGen.isEmpty())
            {
                final ChunkCoord coord = chunksToRetroGen.pollFirst();
                OreHotSwap.logHelper.info("Regenerating ore in " + coord + '.');
                final World world = event.world;
                if (world.getChunkProvider().chunkExists(coord.getX(), coord.getZ()))
                {
                    final long seed = world.getSeed();
                    final Random rng = new Random(seed);
                    final long xSeed = rng.nextLong() >> 2 + 1L;
                    final long zSeed = rng.nextLong() >> 2 + 1L;
                    final long chunkSeed = (xSeed * coord.getX() + zSeed * coord.getZ()) * seed;
                    rng.setSeed(chunkSeed);
                    this.generate(rng, coord.getX() << 4, coord.getZ() << 4, world, null, null, oreTypeHasMap.get(coord));
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

    @SubscribeEvent
    public void onChunkSave(ChunkDataEvent.Save event)
    {
        final ChunkCoord coord = ChunkCoord.of(event);
        if (completedChunks.contains(coord))
        {
            completedChunks.remove(coord);
        }
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this).add("chunksToRetroGen", chunksToRetroGen).toString();
    }

    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider, OreConfig config) {
        Block block = GameData.getBlockRegistry().getObject(config.blockName);
        WorldGenMinable worldGenMinable = new WorldGenMinable(block, config.meta, config.veinSize, Blocks.stone);
        int xPos, yPos, zPos;
        for (int i = 0; i < config.veinsPerChunk; i++)
        {
            xPos = chunkX * 16 + random.nextInt(16);
            yPos = 10 + random.nextInt(config.maxYHeight - config.minYHeight);
            zPos = chunkZ * 16 + random.nextInt(16);
            worldGenMinable.generate(world, random, xPos, yPos, zPos);
        }
    }
}
