package org.usfirst.frc.team3504.robot.subsystems;

import org.usfirst.frc.team3504.robot.Robot;
import org.usfirst.frc.team3504.robot.RobotMap;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;

public class DriveGearBox extends Subsystem {
    
	private CANTalon driveMaster;
	private CANTalon driveSlave;
	
	public DriveGearBox() {
		driveMaster = new CANTalon(RobotMap.DRIVE_MASTER);
		driveSlave = new CANTalon(RobotMap.DRIVE_SLAVE);
		
		driveSlave.changeControlMode(CANTalon.TalonControlMode.Follower);
		driveSlave.set(driveMaster.getDeviceID());
	}
	
	// spins the motor forward
	public void forward() {
		driveMaster.set(0.4);
	}
	
	// spins the motor backward
	public void backward() {
		driveMaster.set(-0.4);
	}
	
	// stops spinning the motor
	public void stop() {
		driveMaster.set(0);
	}
	
    public void initDefaultCommand() {
    }
}

