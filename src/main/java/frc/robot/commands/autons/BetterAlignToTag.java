package frc.robot.commands.autons;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Vision;

public class BetterAlignToTag extends Command{
    
    private Vision vision;
    private CommandSwerveDrivetrain drivetrain;
    private int tagID;

    private boolean tagSeen;
    
    public BetterAlignToTag(){
        vision = vision.getInstance();

        drivetrain = RobotContainer.getSwerveDrivetrain();

        tagID = vision.getTagId();

        tagSeen = false; //when  the camera sees the tag, this will be changed to true
        addRequirements(vision, drivetrain);
    }

    //local method 
    public void tagSeen(){
        if(vision.hasTag()){
            tagSeen = true;
        }
    }
    
}
