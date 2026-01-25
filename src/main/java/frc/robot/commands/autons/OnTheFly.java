/*TODO: WORK IN PROGRESS
1: figure out if waypoints are constants based on field pose or if they need to be corrected with vision/odometry
2: figure out how to flip from red alliance to blue alliance
3: figure out how to follow a pre-generated path after the path that was generated on the fly
4: figure out difference between pathfinding and on the fly path generation https://pathplanner.dev/pplib-pathfinding.html (I think I need to do pathfinding not onthefly)*/
package frc.robot.commands.autons;

import java.util.List;

import com.pathplanner.lib.path.GoalEndState;
import com.pathplanner.lib.path.IdealStartingState;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.path.Waypoint;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Vision;

public class OnTheFly extends Command {

    private CommandSwerveDrivetrain drivetrain;
    private Vision vision; // at the end of each path that was generated on the fly, if theres an april
                           // tag, use vision to update pose

    private Pose2d startPose;

    private Pose2d endPose;

    // on the fly declaration
    private List<Waypoint> waypoints;

    private PathConstraints constraints;

    private IdealStartingState idealStartingState;
    private GoalEndState endState;

    public OnTheFly(CommandSwerveDrivetrain drivetrain, Vision vision, Pose2d startPose, Pose2d endPose,
            List<Waypoint> waypoints, PathConstraints constraints) {
        this.drivetrain = drivetrain;
        this.vision = vision;
        this.startPose = startPose;
        this.endPose = endPose;
        this.waypoints = waypoints;
        this.constraints = constraints;

        addRequirements(drivetrain);
    }

    public OnTheFly(CommandSwerveDrivetrain drivetrain, Vision vision, Pose2d startPose, Pose2d endPose,
            List<Waypoint> waypoints) {
        this.drivetrain = drivetrain;
        this.waypoints = waypoints;
        this.vision = vision;
        this.startPose = startPose;
        this.endPose = endPose;
        // random values in this constructor
        this.constraints = new PathConstraints(2.0, 1.0, 2 * Math.PI, 4 * Math.PI); // The constraints for this path.

        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
    }

    @Override
    public void end(boolean interrupted) {
        // when the path that was generated on the fly ends, update the robot pose with
        // camera and stop the robot from moving
        vision.getRobotPosition();
        drivetrain.drive(0, 0, 0); // this might change if after the path that was generated on the fly was
                                   // followed, it follows another pre-determined path
        // PathPlannerPath pathPostFly = PathPlannerPath.fromPathFile("Example Path");
        // \nAutoBuilder.followPath(path); ????? //TODO number 3
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    public Command flyToTrench() {
        return null; // null for now until values are set
        // the point of this is for the robot to fly to the trench and then follow a
        // predetermined path under the trench so it goes through extremely fast
        // should go to the nearest trench (cut field pose in half to determine)
        // should be interupted if any driveController input is done
    }

    public Command flyToHub() {
        return null; // null for now until values are set
        // the point of this is for the robot to fly to the hub and then get the hub
        // april tag so the turret can lock onto the hub to score
        // should be interupted if any driveController input is done
    }

}
