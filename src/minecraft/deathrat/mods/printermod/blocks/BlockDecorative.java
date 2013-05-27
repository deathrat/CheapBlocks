package deathrat.mods.printermod.blocks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import deathrat.mods.printermod.render.texture.ConnectedTexture;

public class BlockDecorative extends Block
{
	private Icon[] icons;
	private String[] blockNamesUnlocalized = { "blackBand", "blackDoubleBand", "blackPanel", "blackLights", "blackRingLights", "ceilingPanel", "greyPanel", "metalCover", "metalPoint", "reflectiveInsulation", "steelCover", "steelFrame", "whiteBlackHatch", "whiteBand", "whiteDoubleBand", "whitePanel" };
	public String[] blockNames = { "Black Band", "Black Double Band", "Black Panel", "Black Lights", "Black Ring Lights", "Ceiling Panel", "Grey Panel", "Metal Cover", "Metal Point", "Reflective Insulation", "Steel Cover", "Steel Frame", "White Black Hatch", "White Band", "White Double Band", "White Panel" };
	public ConnectedTexture textureRenderer;

	public BlockDecorative(int id)
	{
		super(id, Material.rock);
		icons = new Icon[blockNames.length];
		setHardness(2.0F);
		setResistance(10F);
		MinecraftForge.setBlockHarvestLevel(this, "pickaxe", 1);

		this.textureRenderer = new ConnectedTexture(id);
	}

	@Override
	public void setBlockBoundsForItemRender()
	{
		setBlockBounds(0, 0, 0, 1, 1, 1);
	}

	@Override
	public int getRenderType()
	{
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int itemID, CreativeTabs tab, List subBlocks)
	{
		for (int meta = 0; meta < blockNamesUnlocalized.length; meta++)
		{
			ItemStack iStack = new ItemStack(itemID, 1, meta);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconReg)
	{
		for (int i = 0; i < blockNamesUnlocalized.length; i++)
			icons[i] = iconReg.registerIcon("printermod:" + blockNamesUnlocalized[i]);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int meta)
	{
		if (((meta == 0) || (meta == 1) || (meta == 3)) && ((side == 0) || (side == 1)))
		{
			return icons[2];
		}
		if (((meta == 13) || (meta == 14)) && ((side == 0) || (side == 1)))
		{
			return icons[15];
		}
		return icons[meta];
	}

	@Override
	public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune)
	{
		ArrayList<ItemStack> itemStacks = new ArrayList<ItemStack>();

		itemStacks.add(new ItemStack(this, 1, metadata));
		return itemStacks;
	}

}
