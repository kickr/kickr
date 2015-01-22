package kickr.service.tournament;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

/**
 *
 * @author nikku
 * 
 * @param <T>
 */
public class ScheduleGenerator<T> {

  private final Memory<T> memory;

  public ScheduleGenerator(List<T> players) {
    this.memory = new Memory(players);
  }

  public List<Game<T>> generate(int count) {

    // we want new games !!!^11!
    memory.resetGames();

    // (1) random games with T
    // (2) calculate score for each game
    //     - based on other participants
    //     - based on match fit factor
    // (3) choose game with highest score
    // (4) put game into list of games

    for (int i = 0; i < count; i++) {
      Game<T> game = memory.computeGame(memory.choosePlayer());

      memory.addGame(game);
    }

    System.out.println(memory);
    
    return memory.getGames();
  }

  public void init(List<Game<T>> games) {
    memory.init(games);
  }

  public List<Game<T>> removePlayer(T player) {
    memory.removePlayer(player);

    System.out.println(memory);

    return memory.getGames();
  }

  private static class Pick<T> {

    public final T pick;
    public final List<T> remainder;

    public Pick(T pick, List<T> remainder) {
      this.pick = pick;
      this.remainder = remainder;
    }
  }

  private static <T> Pick<T> pick(T player, List<T> choices) {
    List<T> remainder = new ArrayList<>(choices);
    remainder.remove(player);

    return new Pick(player, remainder);
  }

  public static class Pair<T> {

    public final T player1;
    public final T player2;

    public Pair(T a, T b) {
      this.player1 = a;
      this.player2 = b;
    }

    @Override
    public String toString() {
      return "[" + player1 + ", " + player2 + "]";
    }

    @Override
    public int hashCode() {
      int hash = 5;
      hash = 79 * hash + Objects.hashCode(this.player1);
      hash = 79 * hash + Objects.hashCode(this.player2);
      return hash;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final Pair other = (Pair) obj;

      // A pair is a set of two players,
      // the actual assignment to positions does not matter
      return (Objects.equals(this.player1, other.player1) || Objects.equals(this.player1, other.player2)) &&
             (Objects.equals(this.player2, other.player2) || Objects.equals(this.player2, other.player1));
    }
  }

  public static class Game<T> {

    private final Pair<T> team1;
    private final Pair<T> team2;

    public Game(Pair<T> team1, Pair<T> team2) {
      this.team1 = team1;
      this.team2 = team2;
    }

    @Override
    public String toString() {
      return team1 + " vs " + team2;
    }
  }

  private static class Memory<T> {

    private final List<T> players;

    private final List<Game<T>> games = new ArrayList<>();

    private final Map<T, Integer> played = new HashMap<>();
    private final Map<Pair<T>, Integer> paired = new HashMap<>();

    private final Random random = new Random();

    public Memory(List<T> players) {
      this.players = new ArrayList<>(players);
    }

    public void init(List<Game<T>> games) {
      resetGames();
      games.forEach(this::addGame);
    }

    public double getScore(Game game) {
      return
          Math.pow(played.getOrDefault(game.team1.player1, 0), 2) +
          Math.pow(played.getOrDefault(game.team1.player2, 0), 2) +
          Math.pow(played.getOrDefault(game.team2.player1, 0), 2) +
          Math.pow(played.getOrDefault(game.team2.player2, 0), 2) +
          Math.pow(paired.getOrDefault(game.team1, 0), 2) +
          Math.pow(paired.getOrDefault(game.team2, 0), 2);
    }

    public T choosePlayer() {

      // IDEA: Use player that has played the least games
      //       or random
      Optional<T> player = players.stream().collect(Collectors.minBy((a, b) -> Integer.compare(played.getOrDefault(a, 0), played.getOrDefault(b, 0))));

      return player.get();
    }

    public Game<T> computeGame(T player) {

      Pick<T> playerPick = pick(player, players);

      List<Game<T>> randomGames = new ArrayList<>();

      // generate 20 random games to later choose from
      int count = 20;

      for (int i = 0; i < count; i++) {
        Pick companionPick = pickPlayer(playerPick.remainder);
        Pick otherPlayer = pickPlayer(companionPick.remainder);
        Pick otherCompanion = pickPlayer(otherPlayer.remainder);

        randomGames.add(new Game(
          new Pair(playerPick.pick, companionPick.pick),
          new Pair(otherPlayer.pick, otherCompanion.pick)
        ));
      }

      // order games by their scoring and return best (lowest ranked) match
      return randomGames
          .stream()
            .sorted((a, b) -> Double.compare(getScore(a), getScore(b)))
            .findFirst()
            .get();
    }

    public List<T> getPlayers() {
      return players;
    }

    private Pick<T> pickPlayer(List<T> choices) {
      T pick = choices.get(random.nextInt(choices.size()));
      return new Pick(pick, pick(pick, choices).remainder);
    }

    public List<Game<T>> getGames() {
      return new ArrayList<>(games);
    }

    public void resetGames() {
      games.clear();
    }

    @Override
    public String toString() {
      return String.format("pairs: %s\nplayed: %s\n\ngames: %s", paired, played, games);
    }

    private void addGame(Game game) {
      games.add(game);

      addPlayed(game.team1);
      addPlayed(game.team2);
    }

    private void addPlayed(Pair<T> pair) {
      paired.compute(pair, (k, v) -> (v == null) ? 1 : v + 1);

      addPlayed(pair.player1);
      addPlayed(pair.player2);
    }

    private void addPlayed(T player) {
      played.compute(player, (k, v) -> (v == null) ? 1 : v + 1);
    }

    public void removeGame(Game<T> game) {
      games.remove(game);

      removePlayed(game.team1);
      removePlayed(game.team2);
    }

    private void removePlayed(Pair<T> pair) {
      paired.compute(pair, (k, v) -> (v == null) ? 0 : v - 1);

      removePlayed(pair.player1);
      removePlayed(pair.player2);
    }

    private void removePlayed(T player) {
      played.compute(player, (k, v) -> (v == null) ? 0 : v - 1);
    }

    public void removePlayer(T player) {
      ArrayList<Game<T>> gamesCopy = new ArrayList<>(games);
      
      gamesCopy.stream().filter((g) -> {
        return
          player.equals(g.team1.player1) ||
          player.equals(g.team1.player2) ||
          player.equals(g.team2.player1) ||
          player.equals(g.team2.player2);
      }).forEach(this::removeGame);

      players.remove(player);
    }
  }

  ///// STATIC HELPERS ////////////////////////

  public static <T> List<Game<T>> createSchedule(List<T> players, int count) {
    ScheduleGenerator<T> generator = new ScheduleGenerator<>(players);

    return generator.generate(count);
  }
}
