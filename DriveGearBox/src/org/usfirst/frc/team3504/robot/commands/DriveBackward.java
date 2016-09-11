package org.usfirst.frc.team3504.robot.commands;

import org.usfirst.frc.team3504.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team3504.robot.Robot;

public class DriveBackward extends Command {

    public DriveBackward() {
    	requires(Robot.driveTrain);
    }

    protected void initialize() {
    }

    protected void execute() {
    	System.out.println("going backward");
    	Robot.driveTrain.backward();
    }

    protected boolean isFinished() {
    	return false;
    }

    protected void end() {
    	Robot.driveTrain.stop();
    }

    protected void interrupted() {
    }
}
