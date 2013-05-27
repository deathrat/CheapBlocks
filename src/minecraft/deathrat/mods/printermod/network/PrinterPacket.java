package deathrat.mods.printermod.network;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.Player;

public abstract class PrinterPacket
{
	public abstract void handle(ByteArrayDataInput byteDataInput, Player player);

	public abstract byte getPacketType();
}
