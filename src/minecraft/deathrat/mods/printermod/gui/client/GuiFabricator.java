package deathrat.mods.printermod.gui.client;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import deathrat.mods.printermod.gui.ContainerFabricator;
import deathrat.mods.printermod.network.ClientPacketHandler;
import deathrat.mods.printermod.tile.TileBlockFabricator;

@SideOnly(Side.CLIENT)
public class GuiFabricator extends GuiContainer
{
	private TileBlockFabricator te;

	private GuiFabricatorButton button;

	private GuiFabricatorPageButton pageButton1;
	private GuiFabricatorPageButton pageButton2;

	public GuiFabricator(InventoryPlayer inventoryPlayer, TileBlockFabricator te)
	{
		super(new ContainerFabricator(inventoryPlayer, te));

		this.te = te;
	}

	@Override
	public void initGui()
	{
		super.initGui();

		int xOffset = (this.width - this.xSize) / 2;
		int yOffset = (this.height - this.ySize) / 2;

		button = new GuiFabricatorButton(1, xOffset + 61, yOffset + 42, 53, 14, "");

		buttonList.add(button);

		pageButton1 = new GuiFabricatorPageButton(2, xOffset + 7, yOffset + 31, 18, 9, "", 1);

		buttonList.add(pageButton1);

		pageButton2 = new GuiFabricatorPageButton(3, xOffset + 25, yOffset + 31, 18, 9, "", 2);

		buttonList.add(pageButton2);
	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();
	}

	@Override
	protected void actionPerformed(GuiButton guiButton)
	{
		switch (guiButton.id)
		{
			case 1:
				convertButtonPressed();
			case 2:
				setPage(1);
			case 3:
				setPage(2);
		}

	}

	private void setPage(int i)
	{
		ClientPacketHandler.sendFabricatorUpdate(te, true, i);
	}

	private void convertButtonPressed()
	{
		ClientPacketHandler.sendFabricatorUpdate(this.te, true, 0);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		drawCenteredString(fontRenderer, "Matter: " + te.getMatterCount(), xSize / 2, 33, 0x144b7f);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
	{
		this.mc.renderEngine.bindTexture("/mods/printermod/textures/gui/gui_fabricator.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}

}
