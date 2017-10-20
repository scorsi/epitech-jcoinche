package server.lobby.state;

import io.netty.channel.Channel;
import proto.Command;
import server.lobby.Lobby;

public class ContractState extends AState {

    public ContractState(Lobby lobby) {
        super("Contract", lobby);
    }

    @Override
    public AState initialize() {
        return this;
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public AState getNextState() {
        return new TurnState(this.getLobby());
    }

    @Override
    public void handleAction(Channel channel, Command.LobbyCmd cmd) {

    }

}
