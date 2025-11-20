package frc.robot.commands;

import frc.robot.subsystems.Elevator;


import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class moveElevator extends Command{
    private Elevator elevator;
    private CommandXboxController xboxController;
    public moveElevator(CommandXboxController xboxController){
        elevator = Elevator.getInstance();
        this.xboxController=xboxController;
        addRequirements(elevator);
    }

    public void initialize(){
    }

    public void end(boolean interrupted){
        elevator.setMotorPercent(0.075);
    }
    
    public void execute(){
        elevator.setSpeed(xboxController.getLeftY()*0.5);
    }
    
    public boolean isFinished(){
        return false;
    }
}
