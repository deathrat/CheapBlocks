package deathrat.mods.printermod.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import deathrat.mods.printermod.PrinterMod;

public class GUIHandler implements IGuiHandler
{

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return PrinterMod.proxy.getServerGui(ID, player, world, x, y, z);
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return PrinterMod.proxy.getClientGui(ID, player, world, x, y, z);
	}

}
