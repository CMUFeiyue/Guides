package org.usfirst.frc.team3504.robot.commands;

import org.usfirst.frc.team3504.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class DriveUntilLimitSwitchIsPressed extends Command {
    
	public DriveUntilLimitSwitchIsPressed() {
		requires(Robot.driveMotor);
	}
	
   	protected void initialize() {
   	
   	}

    protected void execute() {
    	Robot.driveMotor.forward();
    }
    
    protected boolean isFinished() {
    	 return Robot.driveMotor.isBumped();
  	}
     
    protected void end() {
    	 Robot.driveMotor.stop();
 	}

  	protected void interrupted() {
    	end();
    }
}