package deathrat.mods.printermod.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import deathrat.mods.printermod.gui.ContainerFabricator;

public class CommonProxy
{
	public void preInit()
	{

	}

	public void init()
	{

	}

	public void postInit()
	{

	}

	public Object getClientGui(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return null;
	}

	public Container getServerGui(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		switch (ID)
		{
			case 0:
				return new ContainerFabricator(player.inventory, tileEntity);
		}

		return null;
	}
}
