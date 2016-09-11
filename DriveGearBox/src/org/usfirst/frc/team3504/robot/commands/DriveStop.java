package org.usfirst.frc.team3504.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team3504.robot.Robot;

public class DriveStop extends Command {

    public DriveStop() {
    	requires(Robot.driveTrain);
    }

    protected void initialize() {
    }

    protected void execute() {
    	System.out.println("stopping");
    	//Robot.driveTrain.stop();
    }

    protected boolean isFinished() {
        return true;
    }

    protected void end() {
    	Robot.driveTrain.stop();
    }

    protected void interrupted() {
    }
}
