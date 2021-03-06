package net.humanity_game.server.packets;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import net.humanity_game.server.HumanityServer;
import net.humanity_game.server.client.ClientConnection;
import net.humanity_game.server.handlers.Handler;
import net.humanity_game.server.handlers.HandlerSnapshot;
import net.humanity_game.server.handlers.Listenable;
import net.humanity_game.server.packets.core.Packet01KeepAlive;
import net.humanity_game.server.packets.core.Packet02Handshake;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PacketHandler {

    private Map<Class<? extends Packet>, List<HandlerSnapshot>> handlers;
    private HumanityServer server;

    public PacketHandler(HumanityServer server) {
        this.handlers = new HashMap<>();
        this.server = Preconditions.checkNotNull(server, "server");

        this.allowPackets();
    }

    private void allowPackets() {
        this.handlers.put(Packet01KeepAlive.class, new ArrayList<HandlerSnapshot>());
        this.handlers.put(Packet02Handshake.class, new ArrayList<HandlerSnapshot>());
    }

    public void handlePacket(PacketSnapshot snapshot) {
        Packet packet = snapshot.getPacket();

        for (HandlerSnapshot handler : this.handlers.get(packet.getClass())) { // TODO sorting
            if (handler.getHandlingType().equals(packet.getClass())) {
                try {
                    this.server.getLogger().debug("(C->S) received: {}", snapshot.getPacket().getClass().getSimpleName());
                    handler.getMethod().invoke(handler.getInstance(), packet, snapshot.getOwner());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.getCause().printStackTrace();
                }
            }
        }
    }

    public void registerHandlers(Listenable listenable) {
        for (Method method : listenable.getClass().getMethods()) {
            if (method.getAnnotation(Handler.class) != null) {
                Handler annotation = method.getAnnotation(Handler.class);
                if (method.getParameterTypes().length == 2) {
                    Class<?> clazz = method.getParameterTypes()[0];
                    if (Packet.class.isAssignableFrom(clazz) && method.getParameterTypes()[1].equals(ClientConnection.class)) {
                        HandlerSnapshot snapshot = new HandlerSnapshot(listenable, method, (Class<? extends Packet>) clazz, annotation);
                        this.registerPacketHandler(snapshot, snapshot.getHandlingType());
                    } else {
                        throw new IllegalArgumentException("The first argument is not a packet");
                    }
                } else {
                    throw new IllegalArgumentException("No extra parameters may be provided other then the packet type");
                }
            }
        }
    }

    private boolean registerPacketHandler(HandlerSnapshot snapshot, Class<? extends Packet> handlingType) {
        if (!this.handlers.containsKey(handlingType)) {
            throw new IllegalArgumentException("cannot handle this type of packet");
        } else {
            List<HandlerSnapshot> handlers = this.handlers.get(handlingType);

            this.server.getLogger().debug("Registered handler {}.{}", snapshot.getInstance().getClass().getSimpleName(), snapshot.getMethod().getName());
            return handlers.add(snapshot);
        }
    }

    private void sendPacketTo(Packet packet, ClientConnection... clients) {
        for (ClientConnection client : clients) {
            client.sendPacket(packet);
        }
    }

    public List<HandlerSnapshot> getHandler(Class<? extends Packet> packet) {
        return ImmutableList.copyOf(this.handlers.get(packet));
    }

}
