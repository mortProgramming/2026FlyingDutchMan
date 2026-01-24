package frc.robot.commands.autons;

import java.util.List;

import com.pathplanner.lib.path.GoalEndState;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.path.Waypoint;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Vision;

public class OnTheFly extends Command{
    
    private CommandSwerveDrivetrain drivetrain;
    
    private Vision vision;
    
    private Pose2d currentPose;
    
    private Pose2d endPose;
    
    //on the fly initialization
    private List<Waypoint> waypoints = PathPlannerPath.waypointsFromPoses(
        new Pose2d(1.0, 1.0, Rotation2d.fromDegrees(0)),
        new Pose2d(3.0, 1.0, Rotation2d.fromDegrees(0)),
        new Pose2d(5.0, 3.0, Rotation2d.fromDegrees(90))
);
    
    //random values for now when we test it will be "safer values"
    private PathConstraints constraints = new PathConstraints(2.0, 1.0, 2 * Math.PI, 4 * Math.PI); // The constraints for this path.


    public OnTheFly(CommandSwerveDrivetrain drivetrain, Vision vision, Pose2d currentPose, Pose2d endPose, List<Waypoint> waypoints, PathConstraints constraints){
        this.drivetrain = drivetrain;
        this.vision = vision;
        this.currentPose = drivetrain.getPose();
        this.endPose = null; //idk what to do for now
    }

    @Override
    public void initialize(){
        
    }

    @Override 
    public void execute(){}

    @Override
    public void end(boolean interrupted){}

    @Override
    public boolean isFinished() {
        return false;
    }

    

}
