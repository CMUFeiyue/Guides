package org.usfirst.frc.team3504.robot.commands;

import org.usfirst.frc.team3504.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveByJoystick extends Command {

    public DriveByJoystick() {
        requires(Robot.chassis);
    }

    protected void initialize() {
    	SmartDashboard.putBoolean("DriveByJoystick", true);
    }

    protected void execute() {
    	Robot.chassis.driveByJoystick
    		(Robot.oi.getDrivingJoystickY(), Robot.oi.getDrivingJoystickX());
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    	Robot.chassis.stop();
    	SmartDashboard.putBoolean("Drive by Joystick", false);
    }

    protected void interrupted() {

    }
}