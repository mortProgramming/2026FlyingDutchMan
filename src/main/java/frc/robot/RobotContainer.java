// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecond;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.AlignToTag;
import frc.robot.commands.Elevate;
import frc.robot.commands.GoToAprilTag;
import frc.robot.commands.HuntTag;
import frc.robot.commands.moveElevator;
import frc.robot.commands.autons.BasicCommands;
import frc.robot.commands.autons.LimelightTest;
import frc.robot.commands.autons.Taxi;
import frc.robot.commands.autons.TimedDrive;
import frc.robot.commands.autons.TimedDriveField;
import frc.robot.configs.constants.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Vision;

public class RobotContainer {
    private double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity

    /* Setting up bindings for necessary control of the swerve drive platform */
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
        .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1)
        .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();

    private final Telemetry logger = new Telemetry(MaxSpeed);

    private final CommandXboxController xbox = new CommandXboxController(1);
    private final CommandJoystick joystick = new CommandJoystick(0);
    private final CommandXboxController wController = new CommandXboxController(2);
    private final Vision vision = Vision.getInstance();
    private static SendableChooser<Command> autoChooser;
    public static final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();

    public RobotContainer() {
        configureBindings();
        // configureAutoBuilder();
        // configureAuto();
    }

    private void configureBindings() {
        // Note that X is defined as forward according to WPILib convention,
        // and Y is defined as to the left according to WPILib convention.
        drivetrain.setDefaultCommand(
            // Drivetrain will execute this command periodically
            drivetrain.applyRequest(() ->
                drive.withVelocityX(-xbox.getLeftY() * MaxSpeed) // Drive forward with negative Y (forward)
            .withVelocityY(-xbox.getLeftX() * MaxSpeed) // Drive left with negative X (left)
            .withRotationalRate(-xbox.getRightX() * MaxAngularRate) // Drive counterclockwise with negative X (left)
            )
        );
        System.out.println(-xbox.getLeftY()*MaxSpeed);
        xbox.b().whileTrue(drivetrain.run(() ->
            drivetrain.applyRequest(() ->
                drive.withVelocityX(-xbox.getLeftY() * MaxSpeed)
                .withVelocityY(0)
                .withRotationalDeadband(0)
            )
        )
    );

        // joystick.a().whileTrue(drivetrain.applyRequest(() -> brake));
        // joystick.b().whileTrue(drivetrain.applyRequest(() ->
            point.withModuleDirection(new Rotation2d(-joystick.getY(), -joystick.getX()));
        // ));

        // Run SysId routines when holding back/start and X/Y.
        // Note that each routine should be run exactly once in a single log.

        // reset the field-centric heading on left bumper press
        xbox.a().onTrue(drivetrain.runOnce(() -> drivetrain.seedFieldCentric()));

        drivetrain.registerTelemetry(logger::telemeterize);

        // Elevator controls
        Elevator elevator = Elevator.getInstance();

        wController.pov(90).onTrue(Elevate.rest());
        wController.pov(270).onTrue(Elevate.l2());
        wController.pov(180).onTrue(Elevate.l3());
        wController.pov(0).onTrue(Elevate.l4());

        //Drive Stop Command
        xbox.y().toggleOnTrue(drivetrain.driveLockCommand(0,0,0));

        //Safe control incase bad things happen
        new Trigger(() -> wController.getLeftY() > 0.05).whileTrue(new moveElevator(wController));
        new Trigger(() -> wController.getLeftY() < -0.05).whileTrue(new moveElevator(wController));

        wController.a().whileTrue(new moveElevator(0.2));
        // Hunt Tag - Teleop - while holding button 3 on joystick should be able to angle and 
        // align toward the april tag to move toward it and away from it
        
        // xbox.x().whileTrue(new HuntTag(drivetrain, vision));

        // Go To April Tag - Auton - when a is pressed go to april tag within distance set to score 
        // (set to 10cm and 5 degrees currently)
        // xbox.a().onTrue(new GoToAprilTag(drivetrain, vision, 0));

        //Another Take on "Go To April Tag", lets see how this plays out
        xbox.b().onTrue(new AlignToTag(drivetrain, vision, 0));

        xbox.x().onTrue(new TimedDriveField(2,0,0,1.5));

    }
    public void configureAuto() {
        autoChooser = new SendableChooser<Command>();
		autoChooser.setDefaultOption("nothing", null);
		autoChooser.addOption("Timed Taxi", new Taxi());
        autoChooser.addOption("Limelight Test", new LimelightTest(drivetrain, vision, 0));

        //Pathplanner autos WIP
        autoChooser.addOption("LimelightTest", new PathPlannerAuto("Please Work"));

		SmartDashboard.putData("Auton Chooser", autoChooser);
    }

    //I'm lazy so... Make this method "configureAutoBuilder()" work with methods and varibles utilized correctly, check how this team configured
    //There autos since they used ctre swerve like us: https://github.com/HuskieRobotics/frc-software-2025
    //So getpose (needs odomentry object and stuff), setrobotposition (needs robot pose and odometry) 
    // and overall make sure each method and varible is accounted for
    //This is important for pathplanner and making the get pose method is helpful to not run into problems with Limelight
    //So GOOD LUCK!

    // public static void configureAutoBuilder() {
    //     AutoBuilder.configure(
    //         () -> drivetrain.getPose(), // get current robot pose
    //         (Pose2d startPose) -> drivetrain.setRobotPosition(startPose), 
    //         () -> {
    //             // Convert your drivetrain's current velocity to something AutoBuilder can understand
    //             // We'll just use requested speeds as a "fake" chassis speed
    //             return new ChassisSpeeds(
    //                 drivetrain.drive.getVelocityX().in(MetersPerSecond),
    //                 drivetrain.drive.getVelocityY().in(MetersPerSecond),
    //                 drivetrain.drive.getRotationalRate().in(RadiansPerSecond)
    //             );
    //         },
    //         (ChassisSpeeds robotRelativeOutput, DriveFeedforwards feedForwards) -> {
    //             // Convert ChassisSpeeds back into your FieldCentric request
    //             drivetrain.applyRequest(() -> drivetrain.drive
    //                 .withVelocityX(robotRelativeOutput.vxMetersPerSecond)
    //                 .withVelocityY(robotRelativeOutput.vyMetersPerSecond)
    //                 .withRotationalRate(robotRelativeOutput.omegaRadiansPerSecond)
    //             );
    //         },
    //         new PPHolonomicDriveController(
    //             new PIDConstants(AUTON_POS_KP, AUTON_POS_KI, AUTON_POS_KD),
    //             new PIDConstants(AUTON_ROTATION_KP, AUTON_ROTATION_KI, AUTON_ROTATION_KD)
    //         ),
    //         new RobotConfig(
    //             ROBOT_MASS,
    //             ROBOT_MOMENT_OF_INERTIA,
    //             new ModuleConfig(
    //                 WHEEL_DIAMETER / 2,
    //                 MaxSpeed, // use your CTRE max speed
    //                 WHEEL_COEFFICIENT_OF_FRICTION,
    //                 DCMotor.getKrakenX60(1).withReduction(1 / DRIVE_REDUCTION),
    //                 DRIVE_MOTOR_CURRENT_LIMIT,
    //                 1
    //             ),
    //             DRIVETRAIN_WHEELBASE_METERS
    //         ),
    //         () -> false, // alliance flipping
    //         drivetrain
    //     );
    // }
    
// SmartDashboard.putString("AutoBuilderConfigured", "true");


    public Command getAutonomousCommand() {
        return Commands.print("No autonomous command configured");
    }

    public static CommandSwerveDrivetrain getSwerveDrivetrain() {
        return drivetrain;
    }
    	public static Command getPlanned(String plan) {
		BasicCommands.setCommands();
		return new PathPlannerAuto(plan);
	}
}
