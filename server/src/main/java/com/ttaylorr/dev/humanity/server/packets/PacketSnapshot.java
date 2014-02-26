package com.ttaylorr.dev.humanity.server.packets;

import com.google.common.base.Preconditions;
import com.ttaylorr.dev.humanity.server.client.Client;
import org.joda.time.Instant;

public class PacketSnapshot {

    private final Packet packet;
    private final Client owner;
    private Instant creation;

    public PacketSnapshot(final Packet packet, final Client owner) {
        this.packet = Preconditions.checkNotNull(packet, "packet");
        this.owner = Preconditions.checkNotNull(owner, "owner");
        this.creation = Instant.now();
    }

    public Packet getPacket() {
        return this.packet;
    }

    public Client getOwner() {
        return this.owner;
    }

    public Instant getCreationInstant() {
        return this.creation;
    }
}