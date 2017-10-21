package server.lobby.state;

import io.netty.channel.Channel;
import proto.Command;
import server.game.Contract;
import server.game.Player;
import server.lobby.Lobby;

public class ContractState extends AState {

    private int playerTurn;
    private Contract contract;

    public ContractState(Lobby lobby) {
        super("Contract", lobby);
    }

    @Override
    public AState initialize() {
        this.getLobby().broadcast("You need to put contracts.", null);
        this.playerTurn = 0;
        this.displayTurnMessage();
        return this;
    }

    @Override
    public boolean isFinished() {
        return this.playerTurn >= 4;
    }

    @Override
    public AState getNextState() {
        if (this.contract != null) {
            return new TurnState(this.getLobby(), this.contract);
        } else {
            this.getLobby().broadcast("Anyone put a contract. Replay the round.", null);
            return new DrawState(this.getLobby());
        }
    }

    @Override
    public void handleAction(Channel channel, Command.LobbyCmd cmd) {
        if (cmd.getCmdType().equals(Command.LobbyCmd.CmdType.CONTRACT)) {
            Player player = this.getLobby().getPlayer(channel);
            if (this.getLobby().getPlayers().toArray()[this.playerTurn].equals(player)) {
                if (cmd.getContract().getType().equals(Command.Contract.Type.PASS)) {
                    this.getLobby().broadcast(player.getName() + " passed.", null);
                } else {
                    this.getLobby().broadcast(player.getName() + " put a contract: " + cmd.getContract().getType()
                            + " " + cmd.getContract().getValue() + ".", null);
                    this.contract = new Contract(player.getTeam(), cmd.getContract().getType(), cmd.getContract().getValue());
                }

                this.playerTurn++;
                this.displayTurnMessage();
            } else {
                this.getLobby().sendMsg("[SERVER] This is not your turn.", channel);
            }
        }
    }

    private void displayTurnMessage() {
        Player player = (Player) this.getLobby().getPlayers().toArray()[this.playerTurn];
        this.getLobby().broadcast("This is the turn of " + player.getName() + " to put a contract.", null);
        this.getLobby().sendMsg("[SERVER] This is your turn to choose a contract.",
                this.getLobby().getChannelByPlayer(player));
    }

}
