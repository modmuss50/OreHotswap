package me.modmuss50.orehotswap;

import me.modmuss50.orehotswap.lib.ChunkCoord;
import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.registry.GameData;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;

/**
 * Created by mark on 28/03/16.
 */
public class CommandHS extends CommandBase {
	@Override
	public String getName() {
		return "orehs";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return null;
	}

	@Override
	public List getAliases() {
		return Collections.emptyList();
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws PlayerNotFoundException, NumberInvalidException {
		if (args.length == 0) {
			sender.sendMessage(new TextComponentString("Use some args!"));
		} else if (args[0].equals("getname")) {
			EntityPlayer player = (EntityPlayer) sender;
			if (player.getHeldItem(EnumHand.MAIN_HAND) != null) {
				Block block = Block.getBlockFromItem(player.getHeldItem(EnumHand.MAIN_HAND).getItem());
				if (block != null && block != Blocks.AIR) {
					sender.sendMessage(new TextComponentString(GameData.getBlockRegistry().getNameForObject(block) + " with a meta of " + player.getHeldItem(EnumHand.MAIN_HAND).getItemDamage()));
				} else {
					((EntityPlayer) sender).sendMessage(new TextComponentString("hold a Block!"));
				}
			} else {
				((EntityPlayer) sender).sendMessage(new TextComponentString("hold an item!"));
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
					xPos = parseInt(sxPos);
				}

				int zPos;
				if (szPos.equals("~")) {
					zPos = (int) playerMP.posZ;
				} else {
					zPos = parseInt(sxPos);
				}
				int radius = parseInt(sRadius);
				if (radius < 1) {
					sender.sendMessage(new TextComponentString("The radius must be bigger than 0!"));
				}
				int chunkPosX = xPos >> 4;
				int chunkPosZ = zPos >> 4;
				System.out.println(chunkPosX + ":" + chunkPosZ);
				System.out.println(OreHotSwap.loader.configFiles);
				if (OreHotSwap.loader.configFiles.containsKey(configName)) {
					for (int x = -radius; x < radius; x++) {
						for (int z = -radius; z < radius; z++) {
							ChunkCoord coord = ChunkCoord.of(x + chunkPosX, z + chunkPosZ);
							OreHotSwap.oreGenerator.addChunk(coord, OreHotSwap.loader.configFiles.get(configName));
						}
					}
				} else {
					sender.sendMessage(new TextComponentString("I could not find that file!"));
				}
			} else {
				sender.sendMessage(new TextComponentString("Please provide  the correct arguments:"));
				sender.sendMessage(new TextComponentString("/orehs gen <configName> <centerx> <centerz> <chunkradius>"));
			}
		} else if (args[0].equals("reload")) {
			try {
				OreHotSwap.loader.load();
				sender.sendMessage(new TextComponentString("Config reloaded!"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				sender.sendMessage(new TextComponentString(e.getLocalizedMessage()));
			}
		}
	}

}
