package frc.robot.commands.autons.timed;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.actions.Elevate;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class Taxi extends SequentialCommandGroup {
    public Taxi() {
        addCommands(
                new SequentialCommandGroup(
                        new TimedDrive(3, 0.5, 0, 0)));
    }
}
