package me.modmuss50.orehotswap;

import cpw.mods.fml.common.registry.GameData;
import me.modmuss50.orehotswap.lib.ChunkCoord;
import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChatComponentText;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by mark on 28/03/16.
 */
public class Command extends CommandBase {
    @Override
    public String getCommandName() {
        return "orehs";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public List getCommandAliases() {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.addChatMessage(new ChatComponentText("Use some args!"));
        } else if (args[0].equals("getname")) {
            EntityPlayer player = (EntityPlayer) sender;
            if (player.getHeldItem() != null) {
                Block block = Block.getBlockFromItem(player.getHeldItem().getItem());
                if (block != null && block != Blocks.air) {
                    sender.addChatMessage(new ChatComponentText(GameData.getBlockRegistry().getNameForObject(block) + " with a meta of " + player.getHeldItem().getItemDamage()));
                } else {
                    ((EntityPlayer) sender).addChatComponentMessage(new ChatComponentText("hold a Block!"));
                }
            } else {
                ((EntityPlayer) sender).addChatComponentMessage(new ChatComponentText("hold an item!"));
            }
        } else if (args[0].equals("gen")) {
            if (args.length == 5) {
                EntityPlayerMP playerMP = getCommandSenderAsPlayer(sender);
                String configName = args[1];
                String sxPos = args[2];
                String szPos = args[3];
                String sRadius = args[4];

                int xPos;
                if (sxPos.equals("~")) {
                    xPos = (int) playerMP.posX;
                } else {
                    xPos = parseInt(sender, sxPos);
                }

                int zPos;
                if (szPos.equals("~")) {
                    zPos = (int) playerMP.posZ;
                } else {
                    zPos = parseInt(sender, sxPos);
                }
                int radius = parseInt(sender, sRadius);
                if (radius < 1) {
                    sender.addChatMessage(new ChatComponentText("The radius must be bigger than 0!"));
                }
                int chunkPosX = xPos >> 4;
                int chunkPosZ = zPos >> 4;
                System.out.println(chunkPosX + ":" + chunkPosZ);
                if (OreHotSwap.loader.configFiles.containsKey(configName)) {
                    for (int x = -radius; x < radius; x++) {
                        for (int z = -radius; z < radius; z++) {
                            ChunkCoord coord = ChunkCoord.of(x + chunkPosX, z + chunkPosZ);
                            OreHotSwap.oreGenerator.addChunk(coord, OreHotSwap.loader.configFiles.get(configName));
                        }
                    }
                } else {
                    sender.addChatMessage(new ChatComponentText("I could not find that file!"));
                }
            } else {
                sender.addChatMessage(new ChatComponentText("Please provide  the correct arguments:"));
                sender.addChatMessage(new ChatComponentText("/orehs gen <configName> <centerx> <centerz> <chunkradius>"));
            }
        } else if (args[0].equals("reload")) {
            try {
                OreHotSwap.loader.load();
                sender.addChatMessage(new ChatComponentText("Config reloaded!"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                sender.addChatMessage(new ChatComponentText(e.getLocalizedMessage()));
            }
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
        return false;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
