package server.lobby.state;

import proto.Command;
import server.game.Deck;
import server.game.DeckGenerator;
import server.game.Player;
import server.lobby.Lobby;

import io.netty.channel.Channel;

import java.util.List;

public class DrawState extends AState {

    private boolean isGenerated = false;

    public DrawState(Lobby lobby) {
        super("Draw", lobby);
    }

    @Override
    public AState initialize() {
        this.getLobby().broadcast("Distribution of cards.", null);
        List<Deck> decks = new DeckGenerator().generateAllDecks();
        for (Player player : this.getLobby().getPlayers()) {
            player.setDeck(decks.get(0));
            decks.remove(0);
        }
        this.isGenerated = true;
        return this;
    }

    @Override
    public boolean isFinished() {
        return this.isGenerated;
    }

    @Override
    public AState getNextState() {
        return new ContractState(this.getLobby());
    }

    @Override
    public void handleAction(Channel channel, Command.LobbyCmd cmd) {

    }

}
