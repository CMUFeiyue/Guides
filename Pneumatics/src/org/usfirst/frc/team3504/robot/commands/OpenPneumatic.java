package org.usfirst.frc.team3504.robot.commands;

import org.usfirst.frc.team3504.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class OpenPneumatic extends Command {

    public OpenPneumatic() {
    	requires(Robot.solenoid);
    }

    protected void initialize() {
    }

    protected void execute() {
    	Robot.solenoid.open();
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    	Robot.solenoid.stop();
    }

    protected void interrupted() {
    }
}
