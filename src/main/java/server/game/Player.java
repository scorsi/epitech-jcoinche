package server.game;

public class Player {

    private Team team;
    private String name;
    private Deck deck;

    public Player(String name) {
        this.name = name;
        this.deck = null;
    }

    public String getName() {
        return this.name;
    }

    public Player setName(String name) {
        this.name = name;
        return this;
    }

    public Deck getDeck() {
        return deck;
    }

    public Player setDeck(Deck deck) {
        this.deck = deck;
        return this;
    }

    public Team getTeam() {
        return team;
    }

    public Player setTeam(Team team) {
        this.team = team;
        return this;
    }

}
