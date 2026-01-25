package frc.robot.commands.autons.apriltag;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Vision;

public class AlignToTag extends Command {

    private final CommandSwerveDrivetrain drivetrain;
    private final Vision vision;
    private final int tagID;

    private final double stopDistance = 0.10;
    private boolean finished = false;

    public AlignToTag(CommandSwerveDrivetrain drivetrain, Vision vision, int tagID) {
        this.drivetrain = drivetrain;
        this.vision = vision;
        this.tagID = vision.getTagId();
        addRequirements(drivetrain, vision);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        if (!vision.hasTag()) {
            System.out.println("yes");
        }

        Pose2d tagPose = vision.getFieldTagPose(tagID);
        if (tagPose == null) {
            // stop robot so it doesn't drift
            drivetrain.drive(0, 0, 0);
            return; // keep running until tag is found
        }

        Pose2d pose = drivetrain.getPose();

        double dx = tagPose.getX() - pose.getX();
        double dy = tagPose.getY() - pose.getY();

        // Desired angle: face the tag
        double desiredAngle = Math.atan2(dy, dx);
        double currentAngle = pose.getRotation().getRadians();
        double angleError = Math.atan2(
                Math.sin(desiredAngle - currentAngle),
                Math.cos(desiredAngle - currentAngle));

        // p controls
        double kP_xy = 1.3;
        double kP_rot = 2.0;

        double vx = kP_xy * dx; // forward/back
        double vy = kP_xy * dy; // strafe left/right
        double rot = kP_rot * angleError;

        double maxSpeed = 2.0;
        vx = Math.max(-maxSpeed, Math.min(maxSpeed, vx));
        vy = Math.max(-maxSpeed, Math.min(maxSpeed, vy));
        rot = Math.max(-2.0, Math.min(2.0, rot));

        drivetrain.drive(vx, vy, rot);

        // Finish
        boolean closePosition = Math.hypot(dx, dy) < stopDistance;
        boolean closeAngle = Math.abs(angleError) < 0.05;

        if (closePosition && closeAngle) {
            finished = true;
        }
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.drive(0, 0, 0);
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
}
