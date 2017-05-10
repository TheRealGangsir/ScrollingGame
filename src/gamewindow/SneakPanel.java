package gamewindow;

import gamelogic.SneakGame;
import gamelogic.Tuning;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import libraries.ImageTools;
import libraries.Logger;
import tiles.Tile;

/**
 * The panel of the game.
 *
 * @author Noah Morton
 *
 * Date created: May 3, 2017
 *
 * Part of project: ScrollingGame
 */
public class SneakPanel extends JPanel implements MouseListener, KeyListener, Runnable {

    private BufferedImage buffer;
    BufferedImage grass, ice, mud, sand, stone, stoneBricks, water, wood, voidTile, player;
    private SneakGame game;

    public SneakPanel() {
        setSize(Tuning.SCREEN_WIDTH, Tuning.SCREEN_HEIGHT);
        buffer = new BufferedImage(Tuning.SCREEN_WIDTH, Tuning.SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);

        //load all images
        grass = ImageTools.load("resources/grass.png");
        ice = ImageTools.load("resources/ice.png");
        mud = ImageTools.load("resources/mud.png");
        sand = ImageTools.load("resources/sand.png");
        stone = ImageTools.load("resources/stone.png");
        stoneBricks = ImageTools.load("resources/stone-bricks.png");
        water = ImageTools.load("resources/water.png");
        wood = ImageTools.load("resources/wood.png");
        voidTile = ImageTools.load("resources/void-tile.png");
        player = ImageTools.load("resources/player.png");

        game = new SneakGame();

        addMouseListener(this);
        addKeyListener(this);

        Logger.logCodeMessage("Initialized panel.");

    }

    public void paint(Graphics g) {
        Graphics bg = buffer.getGraphics();
        drawMap(bg);
        if (Tuning.DEBUG) {
            drawGuidelines(bg, true);
        }

        bg.fillRect(game.getPlayer().getCurrentTile().getX(), game.getPlayer().getCurrentTile().getY(), Tuning.TILE_SIZE, Tuning.TILE_SIZE);
        drawValidMoves(bg, game.getPlayer().getCurrentTile());

        g.drawImage(buffer, 0, 0, null);
    }

    /**
     * Draws 4 rectangles out from the current tile, displaying valid move positions for that tile.
     *
     * @param g Graphics to draw to.
     * @param currentTile Current tile of reference.
     */
    private void drawValidMoves(Graphics g, Tile currentTile) {
        g.setColor(new Color(255, 255, 255, 100));
        //up from tile
        for (int i = 1; i <= currentTile.getMovementRange(); i++) {
            int y = currentTile.getY() - (i * Tuning.TILE_SIZE);
            int x = currentTile.getX();
            //stop drawing out if we hit an impassible tile
            if (y > Tuning.SCREEN_HEIGHT || x > Tuning.SCREEN_WIDTH || y < 0 || x < 0 || !game.convertCoords(x, y).isPassable()) {
                break;
            }
            g.fillRect(x, y, Tuning.TILE_SIZE, Tuning.TILE_SIZE);
        }
        //down from tile
        for (int i = 1; i <= currentTile.getMovementRange(); i++) {
            int y = currentTile.getY() + (i * Tuning.TILE_SIZE);
            int x = currentTile.getX();
            //stop drawing out if we hit an impassible tile
            if (y > Tuning.SCREEN_HEIGHT || x > Tuning.SCREEN_WIDTH || y < 0 || x < 0 || !game.convertCoords(x, y).isPassable()) {
                break;
            }
            g.fillRect(x, y, Tuning.TILE_SIZE, Tuning.TILE_SIZE);
        }
        //right from tile
        for (int i = 1; i <= currentTile.getMovementRange(); i++) {
            int y = currentTile.getY();
            int x = currentTile.getX() + (i * Tuning.TILE_SIZE);
            //stop drawing out if we hit an impassible tile
            if (y > Tuning.SCREEN_HEIGHT || x > Tuning.SCREEN_WIDTH || y < 0 || x < 0 || !game.convertCoords(x, y).isPassable()) {
                break;
            }
            g.fillRect(x, y, Tuning.TILE_SIZE, Tuning.TILE_SIZE);
        }
        //left from tile
        for (int i = 1; i <= currentTile.getMovementRange(); i++) {
            int y = currentTile.getY();
            int x = currentTile.getX() - (i * Tuning.TILE_SIZE);
            //stop drawing out if we hit an impassible tile
            if (y > Tuning.SCREEN_HEIGHT || x > Tuning.SCREEN_WIDTH || y < 0 || x < 0 || !game.convertCoords(x, y).isPassable()) {
                break;
            }
            g.fillRect(x, y, Tuning.TILE_SIZE, Tuning.TILE_SIZE);
        }
    }

    /**
     * Draws the map onto the screen
     *
     * @param g Graphics to draw onto.
     */
    private void drawMap(Graphics g) {
        for (int x = 0; x < Tuning.MAP_WIDTH * Tuning.TILE_SIZE; x += Tuning.TILE_SIZE) {
            for (int y = 0; y < Tuning.MAP_HEIGHT * Tuning.TILE_SIZE; y += Tuning.TILE_SIZE) {
                Tile currentTile = game.getGrid()[y / Tuning.TILE_SIZE][x / Tuning.TILE_SIZE];
                if (currentTile.getX() > Tuning.SCREEN_WIDTH || currentTile.getX() < 0) { //don't render offscreen tiles
                    continue;
                }
                switch (currentTile.getType()) { //draw based on type
                    case 0: //grass
                        g.drawImage(grass, x, y, null);
                        break;
                    case 1: //ice
                        g.drawImage(ice, x, y, null);
                        break;
                    case 2: //mud
                        g.drawImage(mud, x, y, null);
                        break;
                    case 3: //sand
                        g.drawImage(sand, x, y, null);
                        break;
                    case 4: //stone
                        g.drawImage(stone, x, y, null);
                        break;
                    case 5: //stone bricks
                        g.drawImage(stoneBricks, x, y, null);
                        break;
                    case 6: //water
                        g.drawImage(water, x, y, null);
                        break;
                    case 7: //wood
                        g.drawImage(wood, x, y, null);
                        break;
                    case 8: //void
                        g.drawImage(voidTile, x, y, null);
                        break;
                    default:
                        System.err.println("Cannot determine image to draw from tile type: " + currentTile.getType());
                        break;
                }
            }
        }
    }

    /**
     * Draws guidelines based on tile size and screen size. Used for positioning tiles.
     *
     * @param g Graphics to paint the lines onto.
     */
    private void drawGuidelines(Graphics g, final boolean drawNums) {
        g.setColor(Color.WHITE);
        for (int x = 0; x < Tuning.SCREEN_WIDTH; x += Tuning.TILE_SIZE) {
            g.drawLine(x, 0, x, Tuning.SCREEN_HEIGHT);
        }
        for (int y = 0; y < Tuning.SCREEN_WIDTH; y += Tuning.TILE_SIZE) {
            g.drawLine(0, y, Tuning.SCREEN_WIDTH, y);
        }
        if (!drawNums) {
            return;
        }
        g.setFont(new Font("Arial", Font.BOLD, 8));
        int counter = 0;
        for (int i = 10; i < Tuning.SCREEN_HEIGHT; i += Tuning.TILE_SIZE) {
            g.drawString("" + counter, 5, i);
            counter++;
        }
        counter = 0;
        for (int i = 5; i < Tuning.SCREEN_WIDTH; i += Tuning.TILE_SIZE) {
            g.drawString("" + counter, i, 8);
            counter++;
        }
    }

    public void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            repaint();
            try {
                Thread.sleep(35);
            } catch (InterruptedException e) {
                System.err.println("Error Sleeping.");
                Logger.logErrorMessage("Error Sleeping Thread.");
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == 'n') {
            System.out.println("Making new game.");
            Logger.logCodeMessage("Making new game.");
            game = new SneakGame();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //unused
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //unused
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        //unused
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //unused
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //unused
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //unused
    }

    public SneakGame getGame() {
        return game;
    }

}
