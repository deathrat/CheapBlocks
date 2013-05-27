package deathrat.mods.printermod.gui;

import net.minecraft.creativetab.CreativeTabs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import deathrat.mods.printermod.PrinterMod;

public class PrinterCreativeTab extends CreativeTabs
{

	public PrinterCreativeTab()
	{
		super(CreativeTabs.getNextID(), "printerTab");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getTabIconItemIndex()
	{
		return PrinterMod.blockFabricator.blockID;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getTabLabel()
	{
		return "Printer Mod";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getTranslatedTabLabel()
	{
		return "" + getTabLabel();
	}

}
