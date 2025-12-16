package frc.robot.configs.constants;
import edu.wpi.first.math.util.Units;

public class PhysicalConstants {
	public final static class Drivetrain {
        // The left-to-right distance between the drivetrain wheels measured from center
		// to center.
		public static final double DRIVETRAIN_TRACKWIDTH_METERS = Units.inchesToMeters(27.75);
		// The front-to-back distance between the drivetrain wheels measured from center
		// to center.
		public static final double DRIVETRAIN_WHEELBASE_METERS = Units.inchesToMeters(23.75);

		public static final double DRIVEBASE_RADIUS_METERS = Math.hypot(
			DRIVETRAIN_TRACKWIDTH_METERS / 2.0, DRIVETRAIN_WHEELBASE_METERS / 2.0
		);

		// public static final double FRONT_LEFT_OFFSET = 293.9;
		// public static final double FRONT_RIGHT_OFFSET = 273.1;
		// public static final double BACK_LEFT_OFFSET = 223.3;
		// public static final double BACK_RIGHT_OFFSET = 255.5;

		public static final double FRONT_LEFT_OFFSET = 21.3 + 90 + 180;
		public static final double FRONT_RIGHT_OFFSET = 3.08 + 90 + 180;
		public static final double BACK_LEFT_OFFSET = 311.75 + 90 + 180;
		public static final double BACK_RIGHT_OFFSET = 346.73 + 90 + 180;

		public static final int IMU_TO_ROBOT_FRONT_ANGLE = 270;

		public static final double WHEEL_COEFFICIENT_OF_FRICTION = 1;
		public static final double ROBOT_MASS = 62.1;
		public static final double ROBOT_MOMENT_OF_INERTIA = ROBOT_MASS * 0.254 * 0.254 / 2;
		public static final double DRIVE_MOTOR_CURRENT_LIMIT = 60;
		public static final double DRIVE_MOTOR_MAX_RPM = 6000;

		public static final double DRIVE_REDUCTION = (16.0 / 50.0) * (28.0 / 16.0) * (15.0 / 45.0);
		public static final double WHEEL_DIAMETER = 0.1014;  //0.1014
		public static final double ROTATIONS_TO_METERS = WHEEL_DIAMETER * Math.PI;
		public static final double MAX_SPEED = DRIVE_REDUCTION * ROTATIONS_TO_METERS * (DRIVE_MOTOR_MAX_RPM / 60);

		public static final double ODOMETRY_MULTIPLIER = 5.67;
		// public static final double ODOMETRY_MULTIPLIER = 5.575;
    }
    public static final class Elevator {
		//all in inches of carriage
		public static final double ROBOT_VOLTAGE = 12.0; 

		//3 is elevator stage count
		public static final double ROTATIONS_TO_INCHES = 5.642 * 3;

		//The maximum inch displacement for every 0.02 sec cycle, has to be less than half of the rotations to inches		
		public static final double MAXIMUM_INCH_CHANGE = 5;
		public static final double GEAR_RATIO = 16;

		public static final double ELEVATOR_LOWER_LIMIT_SWITCH_HEIGHT = 0;
		// public static final double ELEVATOR_UPPER_LIMIT_SWITCH_HEIGHT = -71.8;
		public static final double ELEVATOR_UPPER_LIMIT_SWITCH_HEIGHT = -69.4;

		public static final double SPEED_FACTOR = 0.5;

		/*
			to fix offset, move the elevator to its lowest possible position, 
			then subtract the position value from the current offset value
			and make that the new offset
		*/
		public static final double ELEVATOR_OFFSET = -23.11;
		public static final double ELEVATOR_START_HEIGHT = 0.8; //for relative encoder

		public static final double ELEVATOR_REST_HEIGHT = 5.6;

		//coral
		public static final double ELEVATOR_L1_HEIGHT = 0;
		public static final double ELEVATOR_L2_HEIGHT = 27.6;
		public static final double ELEVATOR_L3_HEIGHT = 43.6;
		public static final double ELEVATOR_L4_HEIGHT = 68;

		public static final double ELEVATOR_AUTO_L4_HEIGHT = 69;
		public static final double ELEVATOR_INTAKE_HEIGHT = 17.25;
		public static final double ELEVATOR_AUTO_INTAKE_HEIGHT = 17.25; 

		//algae
		public static final double ELEVATOR_LOW_ALGAE_HEIGHT = 4.4;
		public static final double ELEVATOR_HIGH_ALGAE_HEIGHT = 22.3;
		public static final double ELEVATOR_PROCESSOR_HEIGHT = 0;
		public static final double ELEVATOR_BARGE_HEIGHT = 71.5;
		public static final double ELEVATOR_FLOOR_HEIGHT = 0;
		public static final double ELEVATOR_POP_HEIGHT = 0;
	}
}
