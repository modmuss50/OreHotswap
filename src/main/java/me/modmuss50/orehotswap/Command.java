package me.modmuss50.orehotswap;

import cpw.mods.fml.common.registry.GameData;
import me.modmuss50.orehotswap.config.ConfigLoader;
import net.minecraft.block.Block;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChatComponentText;

import java.util.List;

/**
 * Created by mark on 28/03/16.
 */
public class Command implements ICommand {
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
        if(args.length == 0){
            sender.addChatMessage(new ChatComponentText("Use some args!"));
        } else if (args[0].equals("getname")){
            EntityPlayer player = (EntityPlayer) sender;
            if(player.getHeldItem() != null){
                Block block = Block.getBlockFromItem(player.getHeldItem().getItem());
                if(block != null && block != Blocks.air){
                    sender.addChatMessage(new ChatComponentText(GameData.getBlockRegistry().getNameForObject(block) + " with a meta of " + player.getHeldItem().getItemDamage()));
                } else {
                    ((EntityPlayer) sender).addChatComponentMessage(new ChatComponentText("hold a Block!"));
                }
            } else {
                ((EntityPlayer) sender).addChatComponentMessage(new ChatComponentText("hold an item!"));
            }
        } else if(args[0].equals("gen")){
            if(args.length > 1){
                if(OreHotSwap.loader.configFiles.containsKey(args[1])){
                    sender.addChatMessage(new ChatComponentText("Generating ore in the world..."));
                    //TODO things
                    OreHotSwap.oreGenerator.
                } else {
                    sender.addChatMessage(new ChatComponentText("I could not find that file!"));
                }
            } else {
                sender.addChatMessage(new ChatComponentText("Please provide a file to generate"));
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
