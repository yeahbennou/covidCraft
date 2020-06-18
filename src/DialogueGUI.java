import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

/*
- A text box that appears on the screen.
- Disappears whenever the player presses their mouse.
- Event triggered when the dialogue is over, such as giving player an item
 */
public abstract class DialogueGUI extends ScreenComponent
{
    private static final Font TEXT_FONT = new Font("monospaced", Font.BOLD, 20);
    private static final Font SMALL_FONT = new Font("monospaced", Font.PLAIN, 12);
    private static final Image DIALOGUE_BOX;
    private boolean displayAtTop;
    static // Initialize the dialogue box
    {
        Image image;
        try
        {
            image = ImageIO.read(new File("Backgrounds/DialogueBox.png"));
        }
        catch(IOException e)
        {
            image = null;
        }
        DIALOGUE_BOX = image;
    }
    private String text;
    public DialogueGUI(String text)
    {
        this(false, text);
    }
    public DialogueGUI(boolean displayAtTop, String text)
    {
        super(1005); // High layer that is slightly above InventoryGUI.
        this.text = text;
        this.displayAtTop = displayAtTop;
    }

    @Override
    public void draw(Graphics g)
    {
        if(getParentScreen() instanceof GameplayRoom)
        {
            Player player = ((GameplayRoom) getParentScreen()).getThisPlayer();
            if(player.isCurrentlyMoving())
                player.haltPlayer();
        }
        final int WIDTH = Game.GAME_WIDTH, HEIGHT = Game.GAME_HEIGHT;
        final int OFFSET = 40;
        final int Y_OFFSET = displayAtTop? -400: 0;
        final int BOX_WIDTH = WIDTH - OFFSET * 2, BOX_HEIGHT = BOX_WIDTH / 5;
        g.drawImage(DIALOGUE_BOX, OFFSET, HEIGHT - OFFSET - BOX_HEIGHT + Y_OFFSET, BOX_WIDTH, BOX_HEIGHT, null);
        g.setFont(TEXT_FONT);
        g.setColor(Color.BLACK);
        String[] words = text.split(" ");
        String currentLine = "";
        int lineCount = 0;
        for(int i = 0; i < words.length; i++)
        {
            if(currentLine.length() + words[i].length() >= 75)
            {
                final int POSITION = HEIGHT - OFFSET - BOX_HEIGHT + lineCount * 24;
                g.drawString(currentLine, OFFSET + 30, POSITION + 50 + Y_OFFSET);
                currentLine = "";
                lineCount++;
            }
            currentLine += words[i] + " ";
        }
        if(currentLine.length() > 0)
            g.drawString(currentLine,OFFSET + 30, HEIGHT - OFFSET - BOX_HEIGHT + lineCount * 24 + 50 + Y_OFFSET);

        g.setFont(SMALL_FONT);
        g.drawString("- CLICK TO CONTINUE -", OFFSET + 430, HEIGHT - OFFSET - 30 + Y_OFFSET);
    }

    @Override
    public void mousePressed(MouseEvent event)
    {
        whenExited();
        denyComponents(); // Disable interactions with any other component.
        removeComponent(this); // Close this dialogue off upon pressing the mouse
    }

    @Override
    public void keyPressed(KeyEvent event)
    {
        denyComponents(); // Make sure that key presses do NOT get triggered.
    }

    public abstract void whenExited(); // Event that gets triggered upon exiting.
}