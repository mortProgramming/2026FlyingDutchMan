package frc.robot.commands.autons.apriltag;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Vision;

public class GoToAprilTag extends Command {

    private final CommandSwerveDrivetrain drivetrain;
    private final Vision vision;
    private final int tagID;

    private final double kP = 1.0;
    private final double kProt = 0.03;

    public GoToAprilTag(CommandSwerveDrivetrain drivetrain, Vision vision, int tagID) {
        this.drivetrain = drivetrain;
        this.vision = vision;
        this.tagID = vision.getTagId();
        addRequirements(drivetrain);
    }

    @Override
    public void execute() {
        Pose2d tagPose = null;
        if (vision.hasTag()) {
            tagPose = vision.getFieldTagPose(tagID);
        }

        Pose2d robotPose = drivetrain.getPose();

        if (tagPose != null) {
            double dx = tagPose.getX() - robotPose.getX();
            double dy = tagPose.getY() - robotPose.getY();

            double vx = kP * dx;
            double vy = kP * dy;

            vx = clamp(vx, -2, 2);
            vy = clamp(vy, -2, 2);

            double desiredAngle = Math.atan2(dy, dx);
            double currentAngle = robotPose.getRotation().getRadians();

            double angleError = Math.atan2(Math.sin(desiredAngle - currentAngle),
                    Math.cos(desiredAngle - currentAngle));

            double rot = kProt * angleError;
            rot = clamp(rot, -2.5, 2.5);

            drivetrain.drive(vx, vy, rot);
        }
    }

    @Override
    public boolean isFinished() {
        Pose2d tagPose = vision.getFieldTagPose(tagID);

        if (!vision.hasTag() || tagPose == null)
            return false;

        Pose2d robotPose = drivetrain.getPose();

        double dx = tagPose.getX() - robotPose.getX();
        double dy = tagPose.getY() - robotPose.getY();

        boolean positionGood = Math.hypot(dx, dy) < 0.10; // within 10 cm
        boolean rotationGood = Math.abs(robotPose.getRotation().getRadians()) < 0.08;

        return positionGood && rotationGood;
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.drive(0, 0, 0);
    }

    private double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }
}
