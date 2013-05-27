package deathrat.mods.printermod.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class DecorativeSimpleHandler implements ISimpleBlockRenderingHandler
{
	public static int renderId;

	public DecorativeSimpleHandler()
	{
		renderId = RenderingRegistry.getNextAvailableRenderId();
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderblocks)
	{

	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		renderer.renderStandardBlock(block, x, y, z);

		int meta = world.getBlockMetadata(x, y, z);
		if ((meta == 3) || (meta == 4))
		{
			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			lightmapLastX = OpenGlHelper.lastBrightnessX;
			lightmapLastY = OpenGlHelper.lastBrightnessY;
			RenderHelper.disableStandardItemLighting();
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
		}

		return true;
	}

	@Override
	public boolean shouldRender3DInInventory()
	{
		return true;
	}

	@Override
	public int getRenderId()
	{
		return renderId;
	}

	private static float lightmapLastX;
	private static float lightmapLastY;
}
