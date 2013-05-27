package deathrat.mods.printermod.proxy;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import deathrat.mods.printermod.gui.client.GuiFabricator;
import deathrat.mods.printermod.render.DecorativeSimpleHandler;
import deathrat.mods.printermod.tile.TileBlockFabricator;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
	@Override
	public void preInit()
	{
	}

	@Override
	public void init()
	{
		RenderingRegistry.registerBlockHandler(new DecorativeSimpleHandler());
	}

	@Override
	public void postInit()
	{

	}

	@Override
	public GuiScreen getClientGui(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		switch (ID)
		{
			case 0:
				return new GuiFabricator(player.inventory, (TileBlockFabricator) tileEntity);
		}

		return null;
	}

}
