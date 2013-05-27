package deathrat.mods.printermod.gui.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiFabricatorButton extends GuiButton
{
	public GuiFabricatorButton(int id, int x, int y, int width, int height, String displayString)
	{
		super(id, x, y, width, height, displayString);
	}

	@Override
	protected int getHoverState(boolean par1)
	{
		return super.getHoverState(par1);
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY)
	{
		if (this.drawButton)
		{
			mc.renderEngine.bindTexture("/mods/printermod/textures/gui/gui_fabricator.png");
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.field_82253_i = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
			int hoverstate = getHoverState(this.field_82253_i);
			int v = 0;
			int u = 176;

			this.drawTexturedModalRect(this.xPosition, this.yPosition, u, this.height * hoverstate, this.width, this.height);
		}
	}

}
