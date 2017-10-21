package server.game;

import proto.Command;

public class Contract {

    private Team team;
    private Command.Contract.Type type;
    private int value;

    public Contract(Team team, Command.Contract.Type type, int value) {
        this.team = team;
        this.type = type;
        this.value = value;
    }

    public Team getTeam() {
        return team;
    }

    public Command.Contract.Type getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

}
