import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

/**
 * @author Oscar Han, Ben Zeng
 * Revision History:
 * - Jun 8, 2020: ~Ben Zeng Adds the private Inventory class within Player
 * - Jun 6, 2020: Created ~Oscar Han.
 * The class representing player.
 * @version 1
 */
public class Player extends ScreenComponent
{
    /**
     * Constants
     */
    private final static int PLAYER_WIDTH = 300, PLAYER_HEIGHT = 300, PLAYER_HITBOX_WIDTH = 60, PLAYER_HITBOX_HEIGHT = 15;
    public final static int INVENTORY_SIZE = 4;
    public final static int HITBOX_OFFSET = (PLAYER_HEIGHT * 7 / 8 - PLAYER_HITBOX_HEIGHT / 2);
    /**
     * X and Y position of player
     */
    private int x, y;
    /**
     * Current state (direction) of the player
     */
    private char state;
    private char lastState;
    private int motion;
    private boolean currentlyMoving;
    private Image img;
    private Image[][] sprites;
    private int accelerate;
    /**
     * A class that represents the Player inventory.
     */
    private static class Inventory extends InventoryGUI
    {
        @Override
        public Position[] getItemLocations()
        {
            Position[] ret = new Position[INVENTORY_SIZE];
            final int START = (int) (Game.GAME_WIDTH / 2 - (ITEM_SPACING * (INVENTORY_SIZE / 2 - 0.5)));
            for(int i = 0; i < 4; i++)
                ret[i] = new Position(START + ITEM_SPACING * i, Game.GAME_HEIGHT / 2);
            return ret;
        }

        @Override
        public Item get(int index)
        {
            Item[] originalInventory = ((Game) (getParentScreen().getParent())).getInventory();
            return originalInventory[index];
        }

        @Override
        public void set(int index, Item item)
        {
            Item[] originalInventory = ((Game) (getParentScreen().getParent())).getInventory();
            originalInventory[index] = item;
        }

        @Override
        public int size()
        {
            Item[] originalInventory = ((Game) (getParentScreen().getParent())).getInventory();
            return originalInventory.length;
        }
    }
    public static int playerHitboxMiddleOffset()
    {
        return HITBOX_OFFSET - PLAYER_HEIGHT / 2 + PLAYER_HITBOX_HEIGHT / 2;
    }
    /**
     * Constructor for Player- loads all player images and initializes intial position
     * @param x x position of Player
     * @param y y position of Player
     */
    public Player(int x, int y)
    {
        this.x = x;
        this.y = y;
        state = 'd';
        lastState= 'd';
        motion= 0;
        accelerate = 2;
        // intializes Player position and state
        try
        {
            sprites = new Image[][]
            {
                {
                        ImageIO.read(new File("Sprites_Humans/Alice_W.png")),
                        ImageIO.read(new File("Sprites_Humans/Alice_W_1.png")),
                        ImageIO.read(new File("Sprites_Humans/Alice_W.png")),
                        ImageIO.read(new File("Sprites_Humans/Alice_W_2.png"))
                },
                {
                        ImageIO.read(new File("Sprites_Humans/Alice_A.png")),
                        ImageIO.read(new File("Sprites_Humans/Alice_A_1.png")),
                        ImageIO.read(new File("Sprites_Humans/Alice_A.png")),
                        ImageIO.read(new File("Sprites_Humans/Alice_A_2.png")),
                },
                {
                        ImageIO.read(new File("Sprites_Humans/Alice_S.png")),
                        ImageIO.read(new File("Sprites_Humans/Alice_S_1.png")),
                        ImageIO.read(new File("Sprites_Humans/Alice_S.png")),
                        ImageIO.read(new File("Sprites_Humans/Alice_S_2.png"))
                },
                {
                        ImageIO.read(new File("Sprites_Humans/Alice_D.png")),
                        ImageIO.read(new File("Sprites_Humans/Alice_D_1.png")),
                        ImageIO.read(new File("Sprites_Humans/Alice_D.png")),
                        ImageIO.read(new File("Sprites_Humans/Alice_D_2.png"))
                }
            };
            // each possible direction a Player can face also has four separate images that resemble a walking motion when put together
        }
        catch(IOException e)
        {

        }
        img = sprites[0][3];
    }

    public int getPlayerX()
    {
        return x;
    } // getter method- player X

    public int getPlayerY()
    {
        return y;
    } // getter method- player Y
    /**
     * Checks if any Hitboxes in the current gameplay room would overlap with Player
     * @param x x position of Player
     * @param y y position of Player
     */
    private boolean canMoveTo(int x, int y)
    {
        GameplayRoom room = (GameplayRoom) getParentScreen();
        for(HitBox hitbox: room.getHitBoxes())
        {
            if(hitbox.isColliding(x - PLAYER_HITBOX_WIDTH / 2, y - PLAYER_HEIGHT / 2 + HITBOX_OFFSET, PLAYER_HITBOX_WIDTH, PLAYER_HITBOX_HEIGHT))
                return false;
        }
        return true;
    }
    /**
     * Draws Player and changes status when Player is moving
     */
    public void draw(Graphics g)
    {
        if(currentlyMoving)
            changePlayer(state);
        g.drawImage(img, x - PLAYER_WIDTH / 2, y - PLAYER_HEIGHT / 2, PLAYER_WIDTH, PLAYER_HEIGHT, null);
    }
    /**
     * Changes Player lcoation and state
     * @param direction The new state of Player- determined by keyPressed
     */
    public void changePlayer(char direction)
    {
        final int FRAME_DELAY = 5; // Delay between sprite transitions
        if(direction >= 'A' && direction <= 'Z') direction += 'a' - 'A'; // Convert to lower case
        if(direction == lastState) motion++;
        if(motion >= (FRAME_DELAY * 4)) motion = 0;
        switch(direction)
        {
            case 'w':
                if(canMoveTo(x, y - accelerate))
                    y -= accelerate;
                img = sprites[0][motion / FRAME_DELAY];
                break;
            case 'a':
                if(canMoveTo(x - accelerate, y))
                    x -= accelerate;
                img = sprites[1][motion / FRAME_DELAY];
                break;
            case 's':
                if(canMoveTo(x, y + accelerate))
                    y += accelerate;
                img = sprites[2][motion / FRAME_DELAY];
                break;
            case 'd':
                if(canMoveTo(x + accelerate, y))
                    x += accelerate;
                img = sprites[3][motion / FRAME_DELAY];
                break;
        }
        accelerate++;
        if(accelerate > 7)
            accelerate = 7;
        // accelerate is used to simulate increased player momentum when walking
        lastState= direction;
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        Game game = (Game) getParentScreen().getParent();
        if(e.getKeyChar() == 'e' || e.getKeyChar() == 'E')
        {
            InventoryGUI inventoryGUI = new Inventory();
            if(!game.isInventoryUsed())
            {
                game.setInventoryUsed(true);
                // Tutorial dialogue box pops up if user never opened their inventory
                addComponent(new DialogueGUI("This is your player inventory! Your progress through the game will be controlled by the items in here. Feel free to drag items around to rearrange them!")
                {
                    @Override
                    public void whenExited()
                    {
                        addComponent(inventoryGUI);
                    }
                });
            }
            else addComponent(inventoryGUI);
            if(currentlyMoving)
                haltPlayer();
            denyComponents();
        }
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
            PauseMenu pm = new PauseMenu();
            addComponent(pm);
            if(currentlyMoving)
                haltPlayer();
            denyComponents();
        }
        state = e.getKeyChar();
        currentlyMoving = true;
    }
    /**
     * Returns whether Player is currently moving
     */
    public boolean isCurrentlyMoving()
    {
        return currentlyMoving;
    }
    /**
     * Simulates Player stopping by returning the Player to its standard standing sprite
     */
    public void haltPlayer()
    {
        currentlyMoving = false;
        accelerate = 2;
        motion = 0;
        switch(lastState)
        {
            case 'w':
                img = sprites[0][0];
                break;
            case 'a':
                img = sprites[1][0];
                break;
            case 's':
                img = sprites[2][0];
                break;
            case 'd':
                img = sprites[3][0];
                break;
        }
    }
    public void keyReleased(KeyEvent e)
    {
        haltPlayer();
    }
}
