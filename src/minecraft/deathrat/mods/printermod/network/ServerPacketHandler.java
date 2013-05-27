package deathrat.mods.printermod.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import deathrat.mods.printermod.tile.TileBlockFabricator;

public class ServerPacketHandler implements IPacketHandler
{
	public static final String CHANNEL = "printermod";
	private static HashMap packetHandlers = new HashMap();

	public static void registerPacketHandler(PrinterPacket packet)
	{
		if (packetHandlers.get(Byte.valueOf(packet.getPacketType())) != null)
		{
			throw new RuntimeException("Multiple id registrations for packet type on PrinterMod channel");
		}
		packetHandlers.put(Byte.valueOf(packet.getPacketType()), packet);
	}

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
	{
		try
		{
			ByteArrayDataInput data = ByteStreams.newDataInput(packet.data);
			int x = data.readInt();
			int y = data.readInt();
			int z = data.readInt();
			int meta = data.readInt();
			World world = ((EntityPlayer) player).worldObj;

			if (world != null)
			{
				TileEntity te = world.getBlockTileEntity(x, y, z);

				if (te instanceof TileBlockFabricator)
				{
					int matterCount = data.readInt();
					boolean buttonPressed = data.readBoolean();
					int buttonNum = data.readInt();
					((TileBlockFabricator) te).handlePacketData(manager, packet, player, data, matterCount, buttonPressed, buttonNum);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void sendFabricatorUpdate(TileEntity te, Player player, boolean isButtonPress, int buttonNum)
	{
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream data = new DataOutputStream(bytes);
		try
		{
			data.writeInt(te.xCoord);
			data.writeInt(te.yCoord);
			data.writeInt(te.zCoord);
			data.writeInt(te.blockMetadata);
			data.writeInt(((TileBlockFabricator) te).getMatterCount());
			data.writeBoolean(isButtonPress);
			data.writeInt(buttonNum);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "printermod";
		packet.data = bytes.toByteArray();
		packet.length = packet.data.length;
		packet.isChunkDataPacket = true;

		PacketDispatcher.sendPacketToPlayer(packet, player);
	}

}
