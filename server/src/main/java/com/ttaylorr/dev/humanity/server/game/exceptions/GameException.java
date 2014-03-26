package com.ttaylorr.dev.humanity.server.game.exceptions;

import com.ttaylorr.dev.humanity.server.game.HumanityGame;

public class GameException extends Exception {

    private final HumanityGame game;

    public GameException(HumanityGame game) {
        this(game, "");
    }

    public GameException(HumanityGame game, String message) {
        super(message);
        this.game = game;
    }

    public GameException(HumanityGame game, Throwable throwable) {
        super(throwable);
        this.game = game;
    }

    public GameException(HumanityGame game, String message, Throwable throwable) {
        super(message, throwable);
        this.game = game;
    }

    public final HumanityGame getGame() {
        return this.game;
    }

}
