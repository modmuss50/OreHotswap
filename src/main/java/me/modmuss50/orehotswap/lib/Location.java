package me.modmuss50.orehotswap.lib;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by modmuss50 on 21/04/2016.
 */
public class Location implements Comparable<Location> {
    public int x;
    public int y;
    public int z;
    public int depth;
    public World world;

    public Location(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location(int x, int y, int z, int depth) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.depth = depth;
    }

    public Location(int x, int y, int z, int depth, World world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.depth = depth;
        this.world = world;
    }

    public Location(int x, int y, int z, World world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    public Location(int xCoord, int yCoord, int zCoord, ForgeDirection dir) {
        this.x = xCoord + dir.offsetX;
        this.y = yCoord + dir.offsetY;
        this.z = zCoord + dir.offsetZ;
    }

    public Location(int[] coords) {
        if(coords.length >= 2) {
            this.x = coords[0];
            this.y = coords[1];
            this.z = coords[2];
        }

    }

    public Location(ChunkPosition pos) {
        if(pos != null) {
            this.x = pos.chunkPosX;
            this.y = pos.chunkPosY;
            this.z = pos.chunkPosZ;
        }

    }

    public Location(MovingObjectPosition blockLookedAt) {
        if(blockLookedAt != null) {
            this.x = blockLookedAt.blockX;
            this.y = blockLookedAt.blockY;
            this.z = blockLookedAt.blockZ;
        }

    }

    public Location(TileEntity par1) {
        this.x = par1.xCoord;
        this.y = par1.yCoord;
        this.z = par1.zCoord;
    }

    public boolean equals(Location toTest) {
        return this.x == toTest.x && this.y == toTest.y && this.z == toTest.z;
    }

    public void setLocation(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int newX) {
        this.x = newX;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int newY) {
        this.y = newY;
    }

    public int getZ() {
        return this.z;
    }

    public void setZ(int newZ) {
        this.z = newZ;
    }

    public int[] getLocation() {
        int[] ret = new int[]{this.x, this.y, this.z};
        return ret;
    }

    public void setLocation(int[] coords) {
        this.x = coords[0];
        this.y = coords[1];
        this.z = coords[2];
    }

    public int getDifference(Location otherLoc) {
        return (int)Math.sqrt(Math.pow((double)(this.x - otherLoc.x), 2.0D) + Math.pow((double)(this.y - otherLoc.y), 2.0D) + Math.pow((double)(this.z - otherLoc.z), 2.0D));
    }

    public String printLocation() {
        return "X: " + this.x + " Y: " + this.y + " Z: " + this.z;
    }

    public String printCoords() {
        return this.x + ", " + this.y + ", " + this.z;
    }

    public boolean compare(int x, int y, int z) {
        return this.x == x && this.y == y && this.z == z;
    }

    public Location getLocation(ForgeDirection dir) {
        return new Location(this.x + dir.offsetX, this.y + dir.offsetY, this.z + dir.offsetZ);
    }

    public Location modifyPositionFromSide(ForgeDirection side, int amount) {
        switch(side.ordinal()) {
            case 0:
                this.y -= amount;
                break;
            case 1:
                this.y += amount;
                break;
            case 2:
                this.z -= amount;
                break;
            case 3:
                this.z += amount;
                break;
            case 4:
                this.x -= amount;
                break;
            case 5:
                this.x += amount;
        }

        return this;
    }

    public Location modifyPositionFromSide(ForgeDirection side) {
        return this.modifyPositionFromSide(side, 1);
    }

    public TileEntity getTileEntity(IBlockAccess world) {
        return world.getTileEntity(this.x, this.y, this.z);
    }

    public final Location clone() {
        return new Location(this.x, this.y, this.z);
    }

    public TileEntity getTileEntityOnSide(World world, ForgeDirection side) {
        int x = this.x;
        int y = this.y;
        int z = this.z;
        switch(side.ordinal()) {
            case 0:
                --y;
                break;
            case 1:
                ++y;
                break;
            case 2:
                --z;
                break;
            case 3:
                ++z;
                break;
            case 4:
                --x;
                break;
            case 5:
                ++x;
                break;
            default:
                return null;
        }

        return world.blockExists(x, y, z)?world.getTileEntity(x, y, z):null;
    }

    public TileEntity getTileEntityOnSide(World world, int side) {
        int x = this.x;
        int y = this.y;
        int z = this.z;
        switch(side) {
            case 0:
                --y;
                break;
            case 1:
                ++y;
                break;
            case 2:
                --z;
                break;
            case 3:
                ++z;
                break;
            case 4:
                --x;
                break;
            case 5:
                ++x;
                break;
            default:
                return null;
        }

        return world.blockExists(x, y, z)?world.getTileEntity(x, y, z):null;
    }

    public int getDepth() {
        return this.depth;
    }

    public int compareTo(Location o) {
        return Integer.valueOf(this.depth).compareTo(Integer.valueOf(o.depth));
    }

    public World getWorld() {
        return this.world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public String toString() {
        return "Location{x=" + this.x + ", y=" + this.y + ", z=" + this.z + ", depth=" + this.depth + ", world=" + this.world + '}';
    }
}
