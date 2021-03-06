package entities;

import gamelogic.*;

/**
 * Abstract entity.
 *
 * @author Noah Morton
 *
 * Date created: May 12, 2017
 *
 * Part of project: ScrollingGame
 */
public abstract class Entity {

    private int x, y;
    private Tile currentTile;
    private final SneakGame game;

    public Entity(int x, int y, SneakGame game) {
        this.x = x;
        this.y = y;
        this.game = game;
        currentTile = game.getGrid()[y][x];
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
        currentTile = game.getGrid()[y][x];
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
        currentTile = game.getGrid()[y][x];
    }

    public Tile getCurrentTile() {
        return currentTile;
    }

    public void setCurrentTile(Tile currentTile) {
        this.currentTile = currentTile;
    }

    public SneakGame getGame() {
        return game;
    }

    /**
     * Checks if a move is valid based on the entity's location.
     *
     * @param t Tile attempted to move to.
     * @return True if the entity can move to t.
     */
    public boolean isValidMove(Tile t) {
        if (!t.isPassable()) { //don't even check if dest isn't passable
            return false;
        }
        int tY = t.getY();
        int tX = t.getX();
        int yDiff = tY - currentTile.getY(), xDiff = tX - currentTile.getX();

        //if move is even inside the map
        if ((tX >= 0 && tX <= Tuning.MAP_WIDTH) && (tY >= 0 && tY <= Tuning.MAP_HEIGHT)) {
            //if the x is within range of the tile
            if (Math.abs(xDiff) <= currentTile.getMovementRange()) {
                if (tY == currentTile.getY()) { //if y values match
                    if (xDiff < 0) { //if going left
                        //check to ensure all tiles on the way are passable
                        for (int x = currentTile.getX(); x >= tX; x--) {
                            Tile test = game.getGrid()[tY][x];
                            if (!test.isPassable()) {
                                return false;
                            }
                        }
                        return true;
                    } else if (xDiff > 0) { //if going right
                        //check to ensure all tiles on the way are passable
                        for (int x = currentTile.getX(); x <= tX; x++) {
                            Tile test = game.getGrid()[tY][x];
                            if (!test.isPassable()) {
                                return false;
                            }
                        }
                        return true;
                    }
                }
            }
            //if the y is within range of the tile
            if (Math.abs(yDiff) <= currentTile.getMovementRange()) {
                if (tX == currentTile.getX()) { //if x values match
                    if (yDiff < 0) { //if going up
                        //check to ensure all tiles on the way are passable
                        for (int y = currentTile.getY(); y >= tY; y--) {
                            Tile test = game.getGrid()[y][tX];
                            if (!test.isPassable()) {
                                return false;
                            }
                        }
                        return true;
                    } else if (yDiff > 0) { //if going down
                        //check to ensure all tiles on the way are passable
                        for (int y = currentTile.getY(); y <= tY; y++) {
                            Tile test = game.getGrid()[y][tX];
                            if (!test.isPassable()) {
                                return false;
                            }
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
