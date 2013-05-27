package deathrat.mods.printermod.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import deathrat.mods.printermod.PrinterMod;
import deathrat.mods.printermod.network.ServerPacketHandler;
import deathrat.mods.printermod.tile.TileBlockFabricator;

public class BlockBlockFabricator extends BlockContainer
{
	private TileEntity tEntity;
	private Icon icon;

	public BlockBlockFabricator(int id)
	{
		super(id, Material.iron);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconReg)
	{
		icon = iconReg.registerIcon("printermod:printerBlock");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int meta)
	{
		return icon;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int meta, float hitX, float hitY, float hitZ)
	{
		TileEntity tileeEntity = world.getBlockTileEntity(x, y, z);
		if ((tileeEntity != null) && (!entityPlayer.isSneaking()))
		{
			entityPlayer.openGui(PrinterMod.instance, 0, world, x, y, z);
			ServerPacketHandler.sendFabricatorUpdate(world.getBlockTileEntity(x, y, z), (Player) entityPlayer, false, 3);
			return true;
		}
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return tEntity = new TileBlockFabricator();
	}

}
