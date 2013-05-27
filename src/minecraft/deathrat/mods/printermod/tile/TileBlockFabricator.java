package deathrat.mods.printermod.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import deathrat.mods.printermod.network.ServerPacketHandler;

public class TileBlockFabricator extends TileEntity implements IInventory
{
	private ItemStack[] inv;
	private int matterCount = 0;
	private int pageNumber = 1;

	public TileBlockFabricator()
	{
		inv = new ItemStack[27];
	}

	@Override
	public int getSizeInventory()
	{
		return inv.length;
	}

	@Override
	public void onInventoryChanged()
	{
		super.onInventoryChanged();
	}

	@Override
	public ItemStack getStackInSlot(int index)
	{
		return inv[index];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amt)
	{
		ItemStack stack = getStackInSlot(slot);
		if (stack != null)
		{
			if (stack.stackSize <= amt)
			{
				setInventorySlotContents(slot, null);
			}
			else
			{
				stack = stack.splitStack(amt);
				if (stack.stackSize == 0)
				{
					setInventorySlotContents(slot, null);
				}
			}
		}
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int index)
	{
		ItemStack stack = getStackInSlot(index);
		if (stack != null)
		{
			setInventorySlotContents(index, null);
		}
		return stack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack itemstack)
	{
		inv[index] = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit())
		{
			itemstack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public String getInvName()
	{
		return "Fabricator";
	}

	@Override
	public boolean isInvNameLocalized()
	{
		return false;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this && player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
	}

	@Override
	public void openChest()
	{

	}

	@Override
	public void closeChest()
	{

	}

	@Override
	public boolean isStackValidForSlot(int slotIndex, ItemStack itemstack)
	{
		if (slotIndex > 8)
			return true;
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);

		setMatterCount(tag.getInteger("matterCount"));
		setPageNumber(tag.getInteger("pageNumber"));
	}

	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);

		tag.setInteger("matterCount", getMatterCount());
		tag.setInteger("pageNumber", getPageNumber());
	}

	public int getMatterCount()
	{
		return matterCount;
	}

	public void setMatterCount(int matterCount)
	{
		this.matterCount = matterCount;
	}

	public void handlePacketData(INetworkManager manager, Packet250CustomPayload packet, Player player, ByteArrayDataInput data, int matterCount, boolean isButtonPress, int buttonNum)
	{
		Side side = FMLCommonHandler.instance().getEffectiveSide();

		if (side == Side.CLIENT)
		{
			setMatterCount(matterCount);
		}
		else if (side == Side.SERVER)
		{
			if (isButtonPress)
			{
				switch (buttonNum)
				{
					case 0:
						meltDownJunk(player);
					case 1:
						setPageNumber(1);
					case 2:
						setPageNumber(2);
					case 3:

				}
			}
		}

	}

	private void meltDownJunk(Player player)
	{
		for (int i = 9; i < 18; i++)
		{
			ItemStack is = getStackInSlot(i);
			if (is == null)
				continue;
			matterCount += is.stackSize;
			setInventorySlotContents(i, null);
		}
		ServerPacketHandler.sendFabricatorUpdate(this, player, false, 3);
		refreshSlots();
	}

	public void refreshSlots()
	{
		for (int i = 0; i < 9; i++)
		{
			ItemStack is = getStackInSlot(i);
			if (is != null)
			{
				is.setItemDamage(is.getItemDamage() * pageNumber);
				is.stackSize = Math.max(1, getMatterCount());
				setInventorySlotContents(i, is);
				onInventoryChanged();
			}
		}
	}

	public int getPageNumber()
	{
		return pageNumber;
	}

	public void setPageNumber(int pageNumber)
	{
		this.pageNumber = pageNumber;
		refreshSlots();
	}
}
