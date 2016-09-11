package org.usfirst.frc.team3504.robot.subsystems;

import org.usfirst.frc.team3504.robot.RobotMap;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Solenoid extends Subsystem {

	private static DoubleSolenoid doubleSolenoid;
				
	public Solenoid() {
		doubleSolenoid = 
				new DoubleSolenoid(RobotMap.SOLENOID_FWD_CHANNEL, RobotMap.SOLENOID_BWD_CHANNEL);
	}
	
	public void initDefaultCommand() {
	
	}
	
	public void close(){ 							
		doubleSolenoid.set(DoubleSolenoid.Value.kForward);	
	}

	public void open(){						
		doubleSolenoid.set(DoubleSolenoid.Value.kReverse); 	
	}

	public void stop()
	{
		doubleSolenoid.set(DoubleSolenoid.Value.kOff);
	}
}