package com.ttaylorr.dev.humanity.server.client.definition;

import com.google.common.base.Preconditions;
import com.ttaylorr.dev.humanity.server.HumanityServer;
import com.ttaylorr.dev.humanity.server.cards.card.BlackCard;
import com.ttaylorr.dev.humanity.server.cards.hand.ServerHumanityHand;
import com.ttaylorr.dev.humanity.server.cards.hand.IHumanityHand;
import com.ttaylorr.dev.humanity.server.cards.card.WhiteCard;
import com.ttaylorr.dev.humanity.server.client.ClientConnection;
import com.ttaylorr.dev.humanity.server.client.player.PlayerState;
import com.ttaylorr.dev.humanity.server.packets.core.Packet05PlayerStateChange;
import com.ttaylorr.dev.humanity.server.packets.core.Packet06HandUpdate;

import java.util.List;

/**
 * The server's perception on what the current state of the client is.
 *
 * Fallback to the definitions given in this class.
 */
public class ServerClientDefinition implements IClientDefinition {

    private PlayerState state;
    private final ClientConnection owner;
    private final HumanityServer server;
    private final ServerHumanityHand hand;
    private int score;

    public ServerClientDefinition(ClientConnection owner, HumanityServer server) {
        this.owner = Preconditions.checkNotNull(owner, "client connection");
        this.server = Preconditions.checkNotNull(server, "server");
        this.hand = new ServerHumanityHand(owner);
    }

    @Override
    public PlayerState getPlayerState() {
        return this.state;
    }

    public PlayerState setPlayerState(PlayerState newPlayerState) {
        this.state = newPlayerState;
        this.owner.sendPacket(new Packet05PlayerStateChange(newPlayerState));
        return this.state;
    }

    @Override
    public ServerHumanityHand getPlayerHand() {
        return this.hand;
    }

    public void updatePlayerHand(IHumanityHand hand) {
        this.updatePlayerHand(hand.getCards());
    }

    public List<WhiteCard> updatePlayerHand(List<WhiteCard> cards) {
        Packet06HandUpdate packet = new Packet06HandUpdate(cards);
        this.owner.sendPacket(packet);
        return packet.getCards();
    }

    @Override
    public int getScore() {
        return score;
    }

    public void incrementScore(BlackCard card) {
        this.score++;
    }

    public void setScore(int score) {
        this.score = score;
    }
}