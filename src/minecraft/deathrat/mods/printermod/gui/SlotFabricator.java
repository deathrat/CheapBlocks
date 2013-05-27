package deathrat.mods.printermod.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import deathrat.mods.printermod.tile.TileBlockFabricator;

public class SlotFabricator extends Slot
{
	private TileBlockFabricator te;

	public SlotFabricator(IInventory inventory, int index, int x, int y)
	{
		super(inventory, index, x, y);

		this.te = (TileBlockFabricator) inventory;
	}

	@Override
	public boolean canTakeStack(EntityPlayer par1EntityPlayer)
	{
		if (te.getMatterCount() == 0)
			return false;
		return true;
	}

	@Override
	public void onPickupFromSlot(EntityPlayer entityPlayer, ItemStack is)
	{
		int newMatterCount = te.getMatterCount() - is.stackSize;
		te.setMatterCount(newMatterCount);
		te.refreshSlots();
		ItemStack is2 = is.copy();
		is2.stackSize = newMatterCount;
		putStack(is2.copy());
	}

	@Override
	public void onSlotChanged()
	{
		super.onSlotChanged();
	}

	public int getPageNumber()
	{
		return te.getPageNumber();
	}
}
