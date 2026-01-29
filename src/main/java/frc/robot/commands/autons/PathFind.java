package frc.robot.commands.autons;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathConstraints;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Vision;

public class PathFind extends Command {

    private CommandSwerveDrivetrain drivetrain;
    private Vision vision; // at the end of each path finding, if theres an april
                           // tag, use vision to update pose

    private Pose2d startPose;

    private Pose2d endPose;
    @SuppressWarnings("unused")
    private Pose2d targetPose; // so im thinking i do like a target pose for the robot to end the path at and
                               // an actual end pose so then I can do smth to correct it

    private PathConstraints constraints;

    // ig this constructor would be just pathfinding with no pose updates
    public PathFind(CommandSwerveDrivetrain drivetrain, Pose2d endPose, PathConstraints constraints) {
        this.drivetrain = drivetrain;
        this.endPose = endPose;
        this.constraints = constraints;
        addRequirements(drivetrain);
    }

    // this constructor would run pathfinding but prior to doing the path it would
    // update the pose so the robot doesnt explode while pathfinding
    public PathFind(CommandSwerveDrivetrain drivetrain, Pose2d startPose, Pose2d endPose, PathConstraints constraints,
            Vision vision) {
        this.drivetrain = drivetrain;
        this.startPose = drivetrain.getPose();
        this.endPose = endPose;
        this.constraints = constraints;
        addRequirements(drivetrain, vision);
    }
    
    // public PathFind(CommandSwerveDrivetrain drivetrain, Pose2d startPose, Pose2d endPose, PathConstraints constraints,
    //         Vision vision) {
    //     this.drivetrain = drivetrain;
    //     this.startPose = drivetrain.getPose();
    //     this.endPose = endPose;
    //     this.constraints = constraints;
    //     addRequirements(drivetrain, vision);
    // }  This constructor will use linear vel;ocity will prob get rid of other one 
    
    //PSUEDOCODE:
    /* when this command runs, get the initial pose of the robot and then run the path generation based on given values, 
    for the target pose.  Then get the robots ACTUAL end pose and correct it in the GUI by running some random update command.
    (IDK if im going to literally correct the endPOse to TargetPose OR just update the true pose and NOT the targetPose but the robots 
    Actual POSE) */

    public Command pathFindMethod(){
        Command pathfindingCommand = AutoBuilder.pathfindToPose(this.targetPose,this.constraints,0.0); //for now it is zero
        return pathfindingCommand;
    }
    @Override
    public void initialize(){
        
    }

    @Override
    public void execute(){

    }
    
    @Override
    public void end(boolean interrupted) {
        // when the path that was generated on the fly ends, update the robot pose with
        // camera and stop the robot from moving

        if (vision.hasTag()) {
            vision.getRobotPosition();
        }
        drivetrain.drive(0, 0, 0); // this might change if after the path that was generated on the fly was
                                   // followed, it follows another pre-determined path
        // PathPlannerPath pathPostFly = PathPlannerPath.fromPathFile(null); //giving an
        // error because navgrid.json file doesnt exist
        // AutoBuilder.followPath(pathPostFly); //TODO number 3
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    public Command pathFindToTrench() {
        return null; // null for now until values are set
        // the point of this is for the robot to path find to the trench and then follow
        // a
        // predetermined path under the trench so it goes through extremely fast
        // should go to the nearest trench (cut field pose in half to determine)
        // should be interupted if any driveController input is done
    }

    public Command pathFindToHub() {
        return null; // null for now until values are set
        // the point of this is for the robot to path find to the hub and then get the
        // hub
        // april tag so the turret can lock onto the hub to score
        // should be interupted if any driveController input is done
    }

}
