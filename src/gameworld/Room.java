package gameworld;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Room {

  public static final int ROOMSIZE = 10;

  private int row;
  private int col;
  private Tile[][] tiles;
  private List<String> doors;

  public static final Point TOP = new Point(0, 5);
  public static final Point BOTTOM = new Point(9, 5);
  public static final Point LEFT = new Point(5, 0);
  public static final Point RIGHT = new Point(5, 9);

  public Room(int row, int col, Tile[][] tiles, List<String> doors) {

    this.row = row;
    this.col = col;
    this.tiles = Arrays.copyOf(tiles, tiles.length);
    this.doors = doors;

  }

  public Room() {

    this.tiles = new Tile[ROOMSIZE][ROOMSIZE];
    this.doors = new ArrayList<>();
    setupTestRoom();

  }

  private void setupTestRoom() {

    for (int i = 0; i < ROOMSIZE; i++) {
      for (int j = 0; j < ROOMSIZE; j++) {

        if (i == 0 || j == 0 || j == ROOMSIZE - 1 || i == ROOMSIZE - 1) {
          tiles[i][j] = new InaccessibleTile(i, j);
        } else {
          tiles[i][j] = new AccessibleTile(i, j);
        }
      }
    }

  }

  public AccessibleTile findNextTile(Tile t, int dx, int dy) {

    int[] coordinates = getTileCoordinates(t);

    assert coordinates != null;

    int x = coordinates[0];
    int y = coordinates[1];

    int newX = x + dx;
    int newY = y + dy;

    if (newX < 0 || newY < 0 || newX >= 10 || newY >= 10) {
      return null;
    }

    Tile tile = tiles[newX][newY];

    if (tile instanceof AccessibleTile) {

      AccessibleTile nextTile = (AccessibleTile) tile;

      if (nextTile.checkNavigable()) {
        return nextTile;
      }

    }

    return null;

  }

  private int[] getTileCoordinates(Tile t) {

    for (int i = 0; i < ROOMSIZE; i++) {
      for (int j = 0; j < ROOMSIZE; j++) {

        if (tiles[i][j].equals(t)) {
          return new int[]{i, j};
        }

      }
    }

    return null;

  }

  // find tile in a given direction
  private Tile findTile(AccessibleTile tile, Direction direction) {

    int row = tile.getRow();
    int col = tile.getCol();

    switch (direction) {
      case NORTH:
        row -= 1;
        break;
      case SOUTH:
        row += 1;
        break;
      case EAST:
        col += 1;
        break;
      case WEST:
        col -= 1;
        break;
      default:

    }

    if (row < 0 || col < 0 || row >= 10 || col >= 10) {
      return null;
    }

    return tiles[row][col];

  }

  public ChallengeItem getAdjacentChallenge(AccessibleTile currentTile, Direction direction) {

    Tile adjacentTile = findTile(currentTile, direction);

    if (adjacentTile instanceof AccessibleTile) {

      AccessibleTile tile = (AccessibleTile) adjacentTile;

      if (tile.hasChallenge()) {
        return tile.getChallenge();
      }

    }

    return null;

  }

  public Portal getDestinationPortal(Direction dir) {

    Point point = null;

    for (String direction : doors) {

      String d = dir.toString();

      if (d.equals(direction)) {
        point = getNextPoint(dir);
      }
    }

    assert point != null;

    return (Portal) (tiles[point.x][point.y]);

  }

  public Point getNextPoint(Direction direction) {

    switch (direction) {
      case WEST:
        return LEFT;
      case EAST:
        return RIGHT;
      case SOUTH:
        return BOTTOM;
      case NORTH:
        return TOP;
      default:
        return null;
    }
  }

  public AccessibleTile getPlayerTile() {

    for (int row = 0; row < ROOMSIZE; row++) {
      for (int col = 0; col < ROOMSIZE; col++) {

        if (this.tiles[row][col] instanceof AccessibleTile) {

          AccessibleTile tile = (AccessibleTile) this.tiles[row][col];

          if (tile.hasPlayer()) {
            return tile;
          }

        }
      }
    }

    return null;

  }

  public void rotateRoomClockwise() {

    int x = ROOMSIZE / 2;
    int y = ROOMSIZE - 1;

    for (int i = 0; i < x; i++) {
      for (int j = i; j < y - i; j++) {

        final Tile value = this.tiles[i][j];
        this.tiles[i][j] = this.tiles[y - j][i];
        this.tiles[y - j][i] = this.tiles[y - i][y - j];
        this.tiles[y - i][y - j] = this.tiles[j][y - i];
        this.tiles[j][y - i] = value;

      }
    }
  }

  public void rotateRoomAnticlockwise() {

    Tile[][] tempArray = new Tile[ROOMSIZE][ROOMSIZE];

    for (int row = 0; row < ROOMSIZE; row++) {
      for (int col = 0; col < ROOMSIZE; col++) {
        tempArray[ROOMSIZE - col - 1][row] = this.tiles[row][col];
      }
    }

    this.tiles = tempArray;

  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }

  public Tile getTile(int row, int col) {
    return tiles[row][col];
  }

  public void setTile(Tile tile, int row, int col) {
    tiles[row][col] = tile;
  }

  public List<String> getDoors() {
    return doors;
  }

}
