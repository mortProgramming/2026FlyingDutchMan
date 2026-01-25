package frc.robot.commands.autons.apriltag;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.RobotContainer;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Vision;

public class BetterAlignToTag extends Command {

    private Vision vision;
    private CommandSwerveDrivetrain drivetrain;
    private int tagID;
    private boolean isRight = true;
    private static final double TARGET_DISTANCE_METERS = -1.0; // 1 meter away from the tag
    private boolean tagSeen;

    public BetterAlignToTag() {
        vision = Vision.getInstance();
        drivetrain = RobotContainer.getSwerveDrivetrain();
        tagID = vision.getTagId();
        tagSeen = false; // when the camera sees the tag, this will be changed to true
        double xValue = isRight ? 1.0 : -1.0; // Limelight camera offset from center of robot
        addRequirements(vision, drivetrain);
    }

    @Override
    public void initialize() {
        drivetrain.getAprilTagXController().reset();
        drivetrain.getAprilTagYController().reset();
        drivetrain.getAprilTagOmegaController().reset();
    }

    @Override
    public void execute() {
        System.out.println(vision.getRelativeRobotPosition().toString());
        tagSeen();
        if (tagSeen) {
            double xSpeed = drivetrain.getAprilTagXController().calculate(vision.getCamTranX(), TARGET_DISTANCE_METERS);
            double ySpeed = drivetrain.getAprilTagYController().calculate(vision.getCamTranY(), 0);
            double omegaSpeed = drivetrain.getAprilTagOmegaController().calculate(vision.getCamTranZ(), 0);

            drivetrain.driveRelative(xSpeed, ySpeed, omegaSpeed);

        } else {
            drivetrain.driveRelative(0, 0, 0);
        }
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.driveRelative(0, 0, 0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    // local method
    public void tagSeen() {
        if (vision.hasTag()) {
            tagSeen = true;
        }
    }
}
