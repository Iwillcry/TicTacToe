package tictactoe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.google.common.collect.MoreCollectors.onlyElement;
import static com.google.common.collect.MoreCollectors.toOptional;
import static tictactoe.Position.BOTTOM;
import static tictactoe.Position.BOTTOM_LEFT;
import static tictactoe.Position.BOTTOM_RIGHT;
import static tictactoe.Position.MIDDLE;
import static tictactoe.Position.MIDDLE_LEFT;
import static tictactoe.Position.MIDDLE_RIGHT;
import static tictactoe.Position.TOP;
import static tictactoe.Position.TOP_LEFT;
import static tictactoe.Position.TOP_RIGHT;

public class Board {

    private List<Tile> tiles = new ArrayList<>(9);
    private List<List<Position>> winningCombinations = new ArrayList<>();
    private List<Tile> winner;

    public Board() {
        for (Position value : Position.values()) {
            tiles.add(new Tile(value));
        }

        winningCombinations.add(List.of(TOP_LEFT, TOP, TOP_RIGHT));
        winningCombinations.add(List.of(MIDDLE_LEFT, MIDDLE, MIDDLE_RIGHT));
        winningCombinations.add(List.of(BOTTOM_LEFT, BOTTOM, BOTTOM_RIGHT));

        winningCombinations.add(List.of(TOP_LEFT, MIDDLE_LEFT, BOTTOM_LEFT));
        winningCombinations.add(List.of(TOP, MIDDLE, BOTTOM));
        winningCombinations.add(List.of(TOP_RIGHT, MIDDLE_RIGHT, BOTTOM_RIGHT));

        winningCombinations.add(List.of(TOP_LEFT, MIDDLE, BOTTOM_RIGHT));
        winningCombinations.add(List.of(TOP_RIGHT, MIDDLE, BOTTOM_LEFT));

    }

    public boolean hasTied() {
        return tiles.stream()
                .filter(tile -> !tile.getPlayerTurn().equals(PlayerTurn.PLAYER_NONE))
                .toList().containsAll(tiles);
    }

    public Optional<List<Tile>> hasWon() {
        return winningCombinations.stream()
                .map(positions -> positions.stream()
                        .map(this::getTile).toList())
                .map(this::getWinningCombination)
                .filter(Optional::isPresent)
                .collect(toOptional())
                .orElse(Optional.empty());
    }

    private Optional<List<Tile>> getWinningCombination(List<Tile> winningTiles) {
        PlayerTurn last = null;
        for (Tile tile : winningTiles) {
            if (last == null) {
                last = tile.getPlayerTurn();
            } else {
                if (tile.getPlayerTurn().equals(PlayerTurn.PLAYER_NONE) ||
                        !last.equals(tile.getPlayerTurn())) {
                    return Optional.empty();
                }
            }
        }
        winner = winningTiles;
        return Optional.of(winningTiles);
    }

    public Tile getTile(Position d) {
        return tiles.stream().filter(t -> t.getDirection().equals(d)).collect(onlyElement());
    }

    public void print() {
        tiles.forEach(tile -> System.out.print(tile.toString()));
    }

    public PlayerTurn getWinner() {
        return Optional.ofNullable(winner)
                .stream().flatMap(Collection::stream)
                .map(Tile::getPlayerTurn)
                .findAny()
                .orElse(PlayerTurn.PLAYER_NONE);
    }
}