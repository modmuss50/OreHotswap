package me.modmuss50.orehotswap.lib;

import com.google.common.base.Objects;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.ChunkEvent;
import org.apache.commons.lang3.tuple.ImmutablePair;

/**
 * Created by mark on 28/03/16.
 */
public class ChunkCoord implements Comparable<ChunkCoord> {
	private final ImmutablePair<Integer, Integer> data;

	private ChunkCoord(int x, int z) {
		data = ImmutablePair.of(x, z);
	}

	private ChunkCoord(ChunkEvent event) {
		this(event.getChunk().xPosition, event.getChunk().zPosition);
	}

	public static ChunkCoord of(int x, int z) {
		return new ChunkCoord(x, z);
	}

	public static ChunkCoord of(BlockPos coord) {
		return new ChunkCoord(coord.getX() >> 4, coord.getZ() >> 4);
	}

	public static ChunkCoord of(ChunkEvent event) {
		return new ChunkCoord(event);
	}

	public boolean containsWorldCoord(BlockPos coord) {
		return equals(of(coord));
	}

	public int getX() {
		return data.left;
	}

	public int getZ() {
		return data.right;
	}

	public BlockPos localToWorldCoords(BlockPos coord) {
		return new BlockPos((data.left << 4) + coord.getX(), coord.getY(), (data.right << 4) + coord.getZ());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(data.left, data.right);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		final ChunkCoord that = (ChunkCoord) o;
		return data.left.equals(that.data.left) && data.right.equals(that.data.right);
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("X", data.left).add("Z", data.right).toString();
	}

	@Override
	public int compareTo(ChunkCoord o) {
		return data.left.equals(o.data.left) ? data.right.compareTo(o.data.right) : data.left.compareTo(o.data.left);
	}
}
