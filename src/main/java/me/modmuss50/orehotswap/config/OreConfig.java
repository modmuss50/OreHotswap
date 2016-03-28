package me.modmuss50.orehotswap.config;

/**
 * Created by mark on 28/03/16.
 */
public class OreConfig {

    public String blockName;

    public int meta;

    public int veinSize;

    public int veinsPerChunk;

    public int minYHeight;

    public int maxYHeight;

    public int world;

    public OreConfig(String blockName, int meta, int veinSize, int veinsPerChunk, int minYHeight, int maxYHeight, int world) {
        this.blockName = blockName;
        this.meta = meta;
        this.veinSize = veinSize;
        this.veinsPerChunk = veinsPerChunk;
        this.minYHeight = minYHeight;
        this.maxYHeight = maxYHeight;
        this.world = world;
    }

    public OreConfig() {
    }
}
