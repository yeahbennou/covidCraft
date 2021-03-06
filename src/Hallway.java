import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @author Ben Zeng, Oscar Han, Nathan Lu
 * Revision History:
 * Jun 16 2020: Added static final IDs ~Ben Zeng, 2 mins
 * Jun 11 2020: Added IDs to all storage components for the save systemNathan Lu, 5 mins
 * Jun 9 2020: Created ~Nathan Lu, 20 mins
 * Class representing a single room within the levels. A "room" is a section of the house, such as the living room or bedroom, and contains a variety of interactable components.
 * @version 1
 */

public class Hallway extends GameplayRoom
{
    public Hallway(boolean createTutorial)
    {
        addHitBox(new HitBox(0, 10, 0, 1060, 430)); // Wall
        addHitBox(new HitBox(0, -500, 0, 30, 660)); // Left Border
        addHitBox(new HitBox(0, 0, 590, 1060, 1000)); // Bottom Border
        addHitBox(new HitBox(0, 1060, 0, 2600, 670)); // Right Border
        addHitBox(new HitBox(0, 480, 190, 610, 460)); // Statue

        addHitBox(new SpecialDoor(0, 1060, 430, 1080, 595)); //Door at the right
        addHitBox(new Door(0, 470, 600, 635, 620, "Carrie's Room")); //Door at the bottom
        addHitBox(new Door(0, 0, 425, 20, 590, "Dining Room")); //Door at the left
        addHitBox(new Door(0, 300, 200, 430, 430, "Ava's Room")); //Door at top left
        addHitBox(new Door(0, 665, 200, 795, 430, "Alice's Room")); //Door at top right

        if(createTutorial)
        {
            TutorialDialogue dialogue = new TutorialDialogue(
                false,
                "This is the yellow door that you can enter in order to complete a level. Make sure to complete your tasks first before leaving the door!"
            );
            dialogue.setHighlight(new Position(1067, 512));
            addComponent(dialogue);
        }
    }

    @Override
    public int getStartX()
    {
        return 725;
    }

    @Override
    public int getStartY()
    {
        return 400;
    }
    @Override
    public Image getRoomBackground()
    {
        try
        {
            return ImageIO.read(new File("House_CovidCraft/FINAL_Hallway.png"));
        }
        catch(IOException e)
        {
            return null;
        }
    }
}
