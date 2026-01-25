package frc.robot.commands.actions;

import frc.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class MoveElevator extends Command {
    private Elevator elevator;
    private CommandXboxController xboxController;
    private double speed;

    public MoveElevator(CommandXboxController xboxController) {
        elevator = Elevator.getInstance();
        this.xboxController = xboxController;
        addRequirements(elevator);
    }

    public MoveElevator(double speed) {
        elevator = Elevator.getInstance();
        this.speed = speed;
        addRequirements(elevator);
    }

    public void initialize() {
    }

    public void end(boolean interrupted) {
        elevator.setMotorPercent(0.075);
    }

    public void execute() {
        elevator.setSpeed(xboxController.getLeftY() * 0.5);
    }

    public boolean isFinished() {
        return false;
    }
}
