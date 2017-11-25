package com.dpcraft.simplep2pchat.network;

import java.net.DatagramPacket;

/**
 * Created by dpcraft on 25/11/2017.
 */

public interface PacketHandler {
    boolean handle(DatagramPacket datagramPacket);
}
