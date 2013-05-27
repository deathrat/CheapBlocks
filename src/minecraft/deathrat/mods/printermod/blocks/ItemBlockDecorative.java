package deathrat.mods.printermod.blocks;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockDecorative extends ItemBlock
{
	private String[] blockNames = { "Black Band", "Black Double Band", "Black Panel", "Black Lights", "Black Ring Lights", "Ceiling Panel", "Grey Panel", "Metal Cover", "Metal Point", "Reflective Insulation", "Steel Cover", "Steel Frame", "White Black Hatch", "White Band", "White Double Band", "White Panel" };

	public ItemBlockDecorative(int id)
	{
		super(id);
		setHasSubtypes(true);
	}

	@Override
	public String getLocalizedName(ItemStack is)
	{
		return blockNames[is.getItemDamage()];
	}

	@Override
	public String getUnlocalizedName(ItemStack is)
	{
		return blockNames[is.getItemDamage()];
	}

	@Override
	public int getMetadata(int meta)
	{
		return meta;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int itemId, CreativeTabs par2CreativeTabs, List subList)
	{
		for (int i = 0; i < blockNames.length; i++)
		{
			subList.add(new ItemStack(itemId, 1, i));
		}
	}
}
