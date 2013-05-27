package deathrat.mods.printermod.gui;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import deathrat.mods.printermod.PrinterMod;
import deathrat.mods.printermod.tile.TileBlockFabricator;

public class ContainerFabricator extends Container
{
	protected TileBlockFabricator fabricator;
	private int field_94535_f = -1;
	private int field_94536_g = 0;
	private final Set field_94537_h = new HashSet();

	public ContainerFabricator(InventoryPlayer inventory, TileEntity tileEntity)
	{
		fabricator = (TileBlockFabricator) tileEntity;

		for (int width = 0; width < 9; width++)
		{
			addSlotToContainer(new SlotFabricator(fabricator, width, 8 + width * 18, 15));

			fillFabricatorSlot(width, Math.max(1, fabricator.getMatterCount()));
		}
		for (int width = 0; width < 9; width++)
		{
			addSlotToContainer(new Slot(fabricator, 9 + width, 8 + width * 18, 60));
		}

		bindPlayerInventory(inventory);
	}

	protected void bindPlayerInventory(InventoryPlayer inventory)
	{
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				addSlotToContainer(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int i = 0; i < 9; i++)
		{
			addSlotToContainer(new Slot(inventory, i, 8 + i * 18, 142));
		}
	}

	public void fillFabricatorSlot(int index, int amount)
	{
		putStackInSlot(index, new ItemStack(PrinterMod.blockDecorative, amount, index * ((SlotFabricator) inventorySlots.get(index)).getPageNumber()));
	}

	public void refreshSlots()
	{
		for (int i = 0; i < 9; i++)
		{
			fillFabricatorSlot(i, Math.max(1, fabricator.getMatterCount()));
		}
	}

	@Override
	public void putStackInSlot(int index, ItemStack itemStack)
	{
		super.putStackInSlot(index, itemStack);
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityPlayer)
	{
		return fabricator.isUseableByPlayer(entityPlayer);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot)
	{
		ItemStack stack = null;
		Slot slotObject = (Slot) inventorySlots.get(slot);

		// null checks and checks if the item can be stacked (maxStackSize > 1)
		if (slotObject != null && slotObject.getHasStack())
		{
			ItemStack stackInSlot = slotObject.getStack();
			stack = stackInSlot.copy();

			// merges the item into player inventory since its in the tileEntity
			if (slot < 18)
			{
				if (!this.mergeItemStack(stackInSlot, 18, 54, true, slotObject))
				{
					return null;
				}
			}
			// places it into the tileEntity is possible since its in the player
			// inventory
			else if (!this.mergeItemStack(stackInSlot, 9, 18, false, slotObject))
			{
				return null;
			}

			if (stackInSlot.stackSize == 0)
			{
				slotObject.putStack(null);
			}
			else
			{
				slotObject.onSlotChanged();
			}

			if (stackInSlot.stackSize == stack.stackSize)
			{
				return null;
			}
			if (!isFabSlot(slotObject))
				slotObject.onPickupFromSlot(player, stackInSlot);
		}
		return stack;
	}

	public void changePage(int pageNum)
	{
		for (int i = 0; i < 9; i++)
		{
			SlotFabricator slot = (SlotFabricator) inventorySlots.get(i);

			fabricator.setPageNumber(pageNum);

			fillFabricatorSlot(slot.slotNumber, Math.max(1, fabricator.getMatterCount()));
		}
	}

	protected boolean mergeItemStack(ItemStack stack, int slotStart, int slotRange, boolean reverse, Slot slotObject)
	{
		boolean successful = false;
		int slotIndex = slotStart;
		int maxStack = Math.min(stack.getMaxStackSize(), fabricator.getMatterCount());

		if (reverse)
		{
			slotIndex = slotRange - 1;
		}

		Slot slot;
		ItemStack existingStack;

		if (stack.isStackable())
		{
			while (stack.stackSize > 0 && (!reverse && slotIndex < slotRange || reverse && slotIndex >= slotStart))
			{
				slot = (Slot) this.inventorySlots.get(slotIndex);
				existingStack = slot.getStack();

				if (existingStack != null && existingStack.itemID == stack.itemID && (!stack.getHasSubtypes() || stack.getItemDamage() == existingStack.getItemDamage()) && ItemStack.areItemStackTagsEqual(stack, existingStack))
				{
					int existingSize = existingStack.stackSize + stack.stackSize;

					if (existingSize <= maxStack)
					{
						if (isFabSlot(slotObject))
							fabricator.setMatterCount(fabricator.getMatterCount() - stack.stackSize);
						stack.stackSize = 0;
						existingStack.stackSize = existingSize;
						slot.onSlotChanged();
						successful = true;
					}
					else if (existingStack.stackSize < maxStack)
					{
						int tempSize = stack.stackSize - maxStack - existingStack.stackSize;
						int tempMaxStack = maxStack;
						if (isFabSlot(slotObject))
							fabricator.setMatterCount(fabricator.getMatterCount() - tempSize);
						stack.stackSize = tempSize;
						existingStack.stackSize = tempMaxStack;
						slot.onSlotChanged();
						successful = true;
					}
				}

				if (reverse)
				{
					--slotIndex;
				}
				else
				{
					++slotIndex;
				}
			}
		}

		if (stack.stackSize > 0)
		{
			if (reverse)
			{
				slotIndex = slotRange - 1;
			}
			else
			{
				slotIndex = slotStart;
			}

			while (!reverse && slotIndex < slotRange || reverse && slotIndex >= slotStart)
			{
				slot = (Slot) this.inventorySlots.get(slotIndex);
				existingStack = slot.getStack();

				if (existingStack == null)
				{
					if (isFabSlot(slotObject))
						fabricator.setMatterCount(fabricator.getMatterCount() - stack.stackSize);
					slot.putStack(stack.copy());
					stack.stackSize = 0;
					slot.onSlotChanged();
					successful = true;
					break;
				}

				if (reverse)
				{
					--slotIndex;
				}
				else
				{
					++slotIndex;
				}
			}
		}

		return successful;
	}

	private boolean isFabSlot(Slot cl)
	{
		if (cl instanceof SlotFabricator)
			return true;
		return false;
	}

	@Override
	public ItemStack slotClick(int par1, int par2, int par3, EntityPlayer par4EntityPlayer)
	{
		ItemStack itemstack = null;
		InventoryPlayer inventoryplayer = par4EntityPlayer.inventory;
		int l;
		ItemStack itemstack1;

		if (par3 == 5)
		{
			int i1 = this.field_94536_g;
			this.field_94536_g = func_94532_c(par2);

			if ((i1 != 1 || this.field_94536_g != 2) && i1 != this.field_94536_g)
			{
				this.func_94533_d();
			}
			else if (inventoryplayer.getItemStack() == null)
			{
				this.func_94533_d();
			}
			else if (this.field_94536_g == 0)
			{
				this.field_94535_f = func_94529_b(par2);

				if (func_94528_d(this.field_94535_f))
				{
					this.field_94536_g = 1;
					this.field_94537_h.clear();
				}
				else
				{
					this.func_94533_d();
				}
			}
			else if (this.field_94536_g == 1)
			{
				Slot slot = (Slot) this.inventorySlots.get(par1);

				if (slot != null && func_94527_a(slot, inventoryplayer.getItemStack(), true) && slot.isItemValid(inventoryplayer.getItemStack()) && inventoryplayer.getItemStack().stackSize > this.field_94537_h.size() && this.func_94531_b(slot))
				{
					this.field_94537_h.add(slot);
				}
			}
			else if (this.field_94536_g == 2)
			{
				if (!this.field_94537_h.isEmpty())
				{
					itemstack1 = inventoryplayer.getItemStack().copy();
					l = inventoryplayer.getItemStack().stackSize;
					Iterator iterator = this.field_94537_h.iterator();

					while (iterator.hasNext())
					{
						Slot slot1 = (Slot) iterator.next();

						if (slot1 != null && func_94527_a(slot1, inventoryplayer.getItemStack(), true) && slot1.isItemValid(inventoryplayer.getItemStack()) && inventoryplayer.getItemStack().stackSize >= this.field_94537_h.size() && this.func_94531_b(slot1))
						{
							ItemStack itemstack2 = itemstack1.copy();
							int j1 = slot1.getHasStack() ? slot1.getStack().stackSize : 0;
							func_94525_a(this.field_94537_h, this.field_94535_f, itemstack2, j1);

							if (itemstack2.stackSize > itemstack2.getMaxStackSize())
							{
								itemstack2.stackSize = itemstack2.getMaxStackSize();
							}

							if (itemstack2.stackSize > slot1.getSlotStackLimit())
							{
								itemstack2.stackSize = slot1.getSlotStackLimit();
							}

							l -= itemstack2.stackSize - j1;
							slot1.putStack(itemstack2);
						}
					}

					itemstack1.stackSize = l;

					if (itemstack1.stackSize <= 0)
					{
						itemstack1 = null;
					}

					inventoryplayer.setItemStack(itemstack1);
				}

				this.func_94533_d();
			}
			else
			{
				this.func_94533_d();
			}
		}
		else if (this.field_94536_g != 0)
		{
			this.func_94533_d();
		}
		else
		{
			Slot slot2;
			int k1;
			ItemStack itemstack3;

			if ((par3 == 0 || par3 == 1) && (par2 == 0 || par2 == 1))
			{
				if (par1 == -999)
				{
					if (inventoryplayer.getItemStack() != null && par1 == -999)
					{
						if (par2 == 0)
						{
							par4EntityPlayer.dropPlayerItem(inventoryplayer.getItemStack());
							inventoryplayer.setItemStack((ItemStack) null);
						}

						if (par2 == 1)
						{
							par4EntityPlayer.dropPlayerItem(inventoryplayer.getItemStack().splitStack(1));

							if (inventoryplayer.getItemStack().stackSize == 0)
							{
								inventoryplayer.setItemStack((ItemStack) null);
							}
						}
					}
				}
				else if (par3 == 1)
				{
					if (par1 < 0)
					{
						return null;
					}

					slot2 = (Slot) this.inventorySlots.get(par1);

					if (slot2 != null && slot2.canTakeStack(par4EntityPlayer))
					{
						itemstack1 = this.transferStackInSlot(par4EntityPlayer, par1);

						if (itemstack1 != null)
						{
							l = itemstack1.itemID;
							itemstack = itemstack1.copy();

							if (slot2 != null && slot2.getStack() != null && slot2.getStack().itemID == l)
							{
								this.retrySlotClick(par1, par2, true, par4EntityPlayer);
							}
						}

						if (isFabSlot(slot2))
						{
							// fillFabricatorSlot(par1, Math.max(1,
							// fabricator.getMatterCount()));
							refreshSlots();
						}
					}
				}
				else
				{
					if (par1 < 0)
					{
						return null;
					}

					slot2 = (Slot) this.inventorySlots.get(par1);

					if (slot2 != null)
					{
						itemstack1 = slot2.getStack();
						ItemStack itemstack4 = inventoryplayer.getItemStack();

						if (itemstack1 != null)
						{
							itemstack = itemstack1.copy();
						}

						if (itemstack1 == null)
						{
							if (itemstack4 != null && slot2.isItemValid(itemstack4))
							{
								k1 = par2 == 0 ? itemstack4.stackSize : 1;

								if (k1 > slot2.getSlotStackLimit())
								{
									k1 = slot2.getSlotStackLimit();
								}

								slot2.putStack(itemstack4.splitStack(k1));

								if (itemstack4.stackSize == 0)
								{
									inventoryplayer.setItemStack((ItemStack) null);
								}
							}
						}
						else if (slot2.canTakeStack(par4EntityPlayer))
						{
							if (itemstack4 == null)
							{
								k1 = par2 == 0 ? itemstack1.stackSize : (itemstack1.stackSize + 1) / 2;
								itemstack3 = slot2.decrStackSize(k1);
								inventoryplayer.setItemStack(itemstack3);

								if (itemstack1.stackSize == 0)
								{
									slot2.putStack((ItemStack) null);
								}

								slot2.onPickupFromSlot(par4EntityPlayer, inventoryplayer.getItemStack());
							}
							else if (slot2.isItemValid(itemstack4))
							{
								if (itemstack1.itemID == itemstack4.itemID && itemstack1.getItemDamage() == itemstack4.getItemDamage() && ItemStack.areItemStackTagsEqual(itemstack1, itemstack4))
								{
									k1 = par2 == 0 ? itemstack4.stackSize : 1;

									if (k1 > slot2.getSlotStackLimit() - itemstack1.stackSize)
									{
										k1 = slot2.getSlotStackLimit() - itemstack1.stackSize;
									}

									if (k1 > itemstack4.getMaxStackSize() - itemstack1.stackSize)
									{
										k1 = itemstack4.getMaxStackSize() - itemstack1.stackSize;
									}

									itemstack4.splitStack(k1);

									if (itemstack4.stackSize == 0)
									{
										inventoryplayer.setItemStack((ItemStack) null);
									}

									itemstack1.stackSize += k1;
								}
								else if (itemstack4.stackSize <= slot2.getSlotStackLimit())
								{
									slot2.putStack(itemstack4);
									inventoryplayer.setItemStack(itemstack1);
								}
							}
							else if (itemstack1.itemID == itemstack4.itemID && itemstack4.getMaxStackSize() > 1 && (!itemstack1.getHasSubtypes() || itemstack1.getItemDamage() == itemstack4.getItemDamage()) && ItemStack.areItemStackTagsEqual(itemstack1, itemstack4))
							{
								k1 = itemstack1.stackSize;

								if (k1 > 0 && k1 + itemstack4.stackSize <= itemstack4.getMaxStackSize())
								{
									itemstack4.stackSize += k1;
									itemstack1 = slot2.decrStackSize(k1);

									if (itemstack1.stackSize == 0)
									{
										slot2.putStack((ItemStack) null);
									}

									slot2.onPickupFromSlot(par4EntityPlayer, inventoryplayer.getItemStack());
								}
							}
						}

						slot2.onSlotChanged();
					}
				}
			}
			else if (par3 == 2 && par2 >= 0 && par2 < 9)
			{
				slot2 = (Slot) this.inventorySlots.get(par1);

				if (slot2.canTakeStack(par4EntityPlayer))
				{
					itemstack1 = inventoryplayer.getStackInSlot(par2);
					boolean flag = itemstack1 == null || slot2.inventory == inventoryplayer && slot2.isItemValid(itemstack1);
					k1 = -1;

					if (!flag)
					{
						k1 = inventoryplayer.getFirstEmptyStack();
						flag |= k1 > -1;
					}

					if (slot2.getHasStack() && flag)
					{
						itemstack3 = slot2.getStack();
						inventoryplayer.setInventorySlotContents(par2, itemstack3.copy());

						if ((slot2.inventory != inventoryplayer || !slot2.isItemValid(itemstack1)) && itemstack1 != null)
						{
							if (k1 > -1)
							{
								inventoryplayer.addItemStackToInventory(itemstack1);
								slot2.decrStackSize(itemstack3.stackSize);
								slot2.putStack((ItemStack) null);
								slot2.onPickupFromSlot(par4EntityPlayer, itemstack3);
							}
						}
						else
						{
							slot2.decrStackSize(itemstack3.stackSize);
							slot2.putStack(itemstack1);
							slot2.onPickupFromSlot(par4EntityPlayer, itemstack3);
						}
					}
					else if (!slot2.getHasStack() && itemstack1 != null && slot2.isItemValid(itemstack1))
					{
						inventoryplayer.setInventorySlotContents(par2, (ItemStack) null);
						slot2.putStack(itemstack1);
					}
				}
			}
			else if (par3 == 3 && par4EntityPlayer.capabilities.isCreativeMode && inventoryplayer.getItemStack() == null && par1 >= 0)
			{
				slot2 = (Slot) this.inventorySlots.get(par1);

				if (slot2 != null && slot2.getHasStack())
				{
					itemstack1 = slot2.getStack().copy();
					itemstack1.stackSize = itemstack1.getMaxStackSize();
					inventoryplayer.setItemStack(itemstack1);
				}
			}
			else if (par3 == 4 && inventoryplayer.getItemStack() == null && par1 >= 0)
			{
				slot2 = (Slot) this.inventorySlots.get(par1);

				if (slot2 != null && slot2.getHasStack() && slot2.canTakeStack(par4EntityPlayer))
				{
					itemstack1 = slot2.decrStackSize(par2 == 0 ? 1 : slot2.getStack().stackSize);
					slot2.onPickupFromSlot(par4EntityPlayer, itemstack1);
					par4EntityPlayer.dropPlayerItem(itemstack1);
				}
			}
			else if (par3 == 6 && par1 >= 0)
			{
				slot2 = (Slot) this.inventorySlots.get(par1);
				itemstack1 = inventoryplayer.getItemStack();

				if (itemstack1 != null && (slot2 == null || !slot2.getHasStack() || !slot2.canTakeStack(par4EntityPlayer)))
				{
					l = par2 == 0 ? 0 : this.inventorySlots.size() - 1;
					k1 = par2 == 0 ? 1 : -1;

					for (int l1 = 0; l1 < 2; ++l1)
					{
						for (int i2 = l; i2 >= 0 && i2 < this.inventorySlots.size() && itemstack1.stackSize < itemstack1.getMaxStackSize(); i2 += k1)
						{
							Slot slot3 = (Slot) this.inventorySlots.get(i2);

							if (slot3.getHasStack() && func_94527_a(slot3, itemstack1, true) && slot3.canTakeStack(par4EntityPlayer) && this.func_94530_a(itemstack1, slot3) && (l1 != 0 || slot3.getStack().stackSize != slot3.getStack().getMaxStackSize()))
							{
								int j2 = Math.min(itemstack1.getMaxStackSize() - itemstack1.stackSize, slot3.getStack().stackSize);
								ItemStack itemstack5 = slot3.decrStackSize(j2);
								itemstack1.stackSize += j2;

								if (itemstack5.stackSize <= 0)
								{
									slot3.putStack((ItemStack) null);
								}

								slot3.onPickupFromSlot(par4EntityPlayer, itemstack5);
							}
						}
					}
				}

				this.detectAndSendChanges();
			}
		}

		return itemstack;
	}
}
