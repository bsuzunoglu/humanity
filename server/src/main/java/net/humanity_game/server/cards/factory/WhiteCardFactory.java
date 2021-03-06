package net.humanity_game.server.cards.factory;

import com.google.gson.JsonElement;
import net.humanity_game.server.HumanityServer;
import net.humanity_game.server.cards.card.WhiteCard;
import net.humanity_game.server.cards.deck.WhiteCardDeck;

import java.io.File;
import java.io.FileNotFoundException;

public class WhiteCardFactory extends CardFactory<WhiteCard> {

    public WhiteCardFactory(File f, HumanityServer server) throws FileNotFoundException {
        super(f, server);
    }

    @Override
    public WhiteCardFactory parse() {
        for (JsonElement element : this.doc.getAsJsonArray()) {
            if (element.getAsJsonObject().get(CardFactory.CARD_TYPE).getAsCharacter() == 'A') {
                this.cards.add(this.gson.fromJson(element, WhiteCard.class));
            }
        }
        return this;
    }

    @Override
    public WhiteCardDeck build() {
        return new WhiteCardDeck(this.getCards());
    }

}
