package com.ttaylorr.dev.humanity.server.cards.hand;

import com.ttaylorr.dev.humanity.server.cards.core.BlackCard;
import com.ttaylorr.dev.humanity.server.cards.core.Card;
import com.ttaylorr.dev.humanity.server.cards.core.WhiteCard;
import com.ttaylorr.dev.humanity.server.cards.core.factory.BlackCardFactory;
import com.ttaylorr.dev.humanity.server.cards.core.factory.CardFactory;
import com.ttaylorr.dev.humanity.server.cards.core.factory.WhiteCardFactory;

import java.util.Collections;
import java.util.LinkedList;

public class Deck<C extends Card> {

    private LinkedList<C> onDeck;
    private final CardFactory<C> factory;

    public Deck(CardFactory<C> factory) {
        onDeck = (LinkedList<C>) Collections.synchronizedList(new LinkedList<C>());
        this.factory = factory;
    }

    /**
     * This method is "trusting". Optimally, this class wouldn't allow cards to be "owned" without confirmation that they are being used fairly.
     * @return
     */
    public synchronized C drawCard() {
        return onDeck.pop();
    }

    /**
     * This method is "trusting". Optimally, this class wouldn't allow cards to be "owned" without confirmation that they are being used fairly.
     * @return
     */
    public synchronized void releaseCard(C card) {
        if (!onDeck.contains(card)) {
            onDeck.offer(card);
        } else {
            // TODO there is a duplicate card, which may or may not be intentional.
        }
    }

    private static Deck<BlackCard> blackDeck;
    private static Deck<WhiteCard> whiteDeck;

    static {
        BlackCardFactory bcf = new BlackCardFactory();
        // TODO add paths to bcf's pullPaths
        WhiteCardFactory wcf = new WhiteCardFactory();
        // TODO add paths to wcf's pullPaths

        blackDeck = new Deck<>(bcf);
        whiteDeck = new Deck<>(wcf);
    }

    public static Deck<BlackCard> getBlackDeck() {
        return blackDeck;
    }

    public static Deck<WhiteCard> getWhiteDeck() {
        return whiteDeck;
    }

}