package org.usfirst.frc.team3504.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team3504.robot.Robot;

public class DriveForward extends Command {

    public DriveForward() {
    	requires(Robot.driveTrain);
    }

    protected void initialize() {
    }

    protected void execute() {
    	System.out.println("going forward");
    	Robot.driveTrain.forward();
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
