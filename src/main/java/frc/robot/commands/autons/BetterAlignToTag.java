package frc.robot.commands.autons;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Vision;

public class BetterAlignToTag extends Command{
    private Vision vision;
    private CommandSwerveDrivetrain drivetrain;
    private int tagID;

    
    public BetterAlignToTag(){

    }
    
}
