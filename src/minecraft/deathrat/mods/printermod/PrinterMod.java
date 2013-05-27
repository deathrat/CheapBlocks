package deathrat.mods.printermod;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import deathrat.mods.printermod.blocks.BlockBlockFabricator;
import deathrat.mods.printermod.blocks.BlockDecorative;
import deathrat.mods.printermod.blocks.ItemBlockDecorative;
import deathrat.mods.printermod.gui.GUIHandler;
import deathrat.mods.printermod.gui.PrinterCreativeTab;
import deathrat.mods.printermod.network.ConnectionHandler;
import deathrat.mods.printermod.network.ServerPacketHandler;
import deathrat.mods.printermod.proxy.CommonProxy;
import deathrat.mods.printermod.tile.TileBlockFabricator;

@Mod(modid = "PrinterMod")
@NetworkMod(serverSideRequired = true, clientSideRequired = true, channels = { "printermod" }, packetHandler = ServerPacketHandler.class, connectionHandler = ConnectionHandler.class)
public class PrinterMod
{
	@Instance("PrinterMod")
	public static PrinterMod instance;

	@SidedProxy(clientSide = "deathrat.mods.printermod.proxy.ClientProxy", serverSide = "deathrat.mods.printermod.proxy.CommonProxy")
	public static CommonProxy proxy;

	@PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		proxy.preInit();

		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();

		initializeConfigs(config);

		config.save();
	}

	@Init
	public void Init(FMLInitializationEvent event)
	{
		instance = this;

		initializeBlocks();
		initializeItems();
		initializeGui();

		initializeCreativeTab();
		proxy.init();
	}

	private void initializeGui()
	{
		NetworkRegistry.instance().registerGuiHandler(this, new GUIHandler());
	}

	@PostInit
	public void postInit(FMLPostInitializationEvent event)
	{
		proxy.postInit();
	}

	public void initializeConfigs(Configuration config)
	{
		initializeBlockConfig(config);
		initializeItemConfig(config);
	}

	private void initializeItemConfig(Configuration config)
	{

	}

	private void initializeBlockConfig(Configuration config)
	{
		fabricatorId = config.getBlock("blockFabricator", 3121).getInt();
		decorativeId = config.getBlock("blockDecorative", 3122).getInt();
	}

	public void initializeBlocks()
	{
		blockFabricator = new BlockBlockFabricator(fabricatorId).setUnlocalizedName("printer.blockFab");

		GameRegistry.registerBlock(blockFabricator, blockFabricator.getUnlocalizedName());
		GameRegistry.registerTileEntity(TileBlockFabricator.class, "Block Fabricator");

		LanguageRegistry.addName(blockFabricator, "Block Fabricator");

		blockDecorative = new BlockDecorative(decorativeId);

		GameRegistry.registerBlock(blockDecorative, ItemBlockDecorative.class, "printerDecorativeBlock");

		for (int i = 0; i < blockDecorative.blockNames.length; i++)
		{
			ItemStack is = new ItemStack(blockDecorative, 1, i);
			LanguageRegistry.addName(is, blockDecorative.blockNames[i]);
		}

	}

	public void initializeItems()
	{
	}

	private void initializeCreativeTab()
	{
		printerTab = new PrinterCreativeTab();
		blockDecorative.setCreativeTab(printerTab);
		blockFabricator.setCreativeTab(printerTab);
	}

	public static CreativeTabs getCustomCreativeTab()
	{
		return printerTab;
	}

	private static int fabricatorId;
	private static int decorativeId;

	public static Block blockFabricator;
	public static BlockDecorative blockDecorative;

	private static CreativeTabs printerTab;

}
