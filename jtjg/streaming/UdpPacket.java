package streaming;
/*
UdpPacket.java by Geist Alexander 

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.  

*/ 
import java.net.DatagramPacket;

/**
 * @author Geist Alexander
 *
 */
class UdpPacket
{
	byte[] buffer = new byte[1472];
	DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
	int dataOffset = 8;
	int UsedLength = 1472;
	
	public int length() {
	    return buffer.length;
	}
	
	public int getPacketPos() {
		return buffer[0];
	}
	 
	public int getPacketStatus() {
		return buffer[1];
	}
	
	public int getSPktBuf() {
		return buffer[2];
	}
		
	public int getStream() {
		return buffer[3];
	}
	
	public int getStreamPacket() {
		return (((buffer[4] * 256 + buffer[5]) * 256 + 
				       buffer[6]) * 256) + buffer[7];
	}
	
	public byte[] getUsedData() {
	    byte[] writeBuffer = new byte[1464];
	    System.arraycopy(buffer, dataOffset, writeBuffer, 0, length()-dataOffset);
	    return writeBuffer;
	}
}

