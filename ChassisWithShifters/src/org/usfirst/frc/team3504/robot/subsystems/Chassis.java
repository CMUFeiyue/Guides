package org.usfirst.frc.team3504.robot.subsystems;

import org.usfirst.frc.team3504.robot.Robot;
import org.usfirst.frc.team3504.robot.RobotMap;
import org.usfirst.frc.team3504.robot.commands.DriveByJoystick;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Chassis extends Subsystem {
    private CANTalon masterLeft;
    private CANTalon slaveLeft;
    
    private CANTalon masterRight;
    private CANTalon slaveRight;
    
    private RobotDrive robotDrive;
    
    private double encOffsetValueRight = 0;
    private double encOffsetValueLeft = 0;
       
    public Chassis() {
    	masterLeft = new CANTalon(RobotMap.MASTER_LEFT);
		slaveLeft = new CANTalon(RobotMap.SLAVE_LEFT);
		
		masterRight = new CANTalon(RobotMap.MASTER_RIGHT);
		slaveRight = new CANTalon(RobotMap.MASTER_LEFT);
		
		
		masterLeft.enableBrakeMode(true);
		slaveLeft.enableBrakeMode(true);
		
		masterRight.enableBrakeMode(true);
		slaveRight.enableBrakeMode(true);
		
		
		slaveLeft.changeControlMode(CANTalon.TalonControlMode.Follower);
		slaveLeft.set(masterLeft.getDeviceID());
		
		slaveRight.changeControlMode(CANTalon.TalonControlMode.Follower);
		slaveRight.set(masterRight.getDeviceID());
		
		robotDrive = new RobotDrive(masterLeft, masterRight);

		// Set some safety controls for the drive system
		robotDrive.setSafetyEnabled(true);
		robotDrive.setExpiration(0.1);
		robotDrive.setSensitivity(0.5);
		robotDrive.setMaxOutput(1.0);
		
    }
    
    public void initDefaultCommand() {
    	setDefaultCommand(new DriveByJoystick());
    }
    
    public void driveByJoystick(double yDir, double xDir){
    	SmartDashboard.putString("driveByJoystick?", yDir + "," + xDir);
    	robotDrive.arcadeDrive(yDir,xDir);
    }
    
    public void printEncoderValues() {
		getEncoderDistance();
	}

	public double getEncoderRight() {
		return -masterRight.getEncPosition();
	}

	public double getEncoderLeft() {
		return masterLeft.getEncPosition();
	}

	public double getEncoderDistance() {
		double distPerPulse;
		if (Robot.shifters.inHighGear()) {
			distPerPulse = RobotMap.DIST_PER_PULSE_HIGH_GEAR;
		} else {
			distPerPulse = RobotMap.DIST_PER_PULSE_LOW_GEAR;
		}
		
		double numPulseLeft = getEncoderRight() - encOffsetValueLeft;
		double numPulseRight = getEncoderRight() - encOffsetValueRight;
			
		SmartDashboard.putNumber("Chassis Encoders Right", (numPulseRight * distPerPulse));
		SmartDashboard.putNumber("Chassis Encoders Left", (numPulseLeft * distPerPulse));
		return (numPulseRight) * RobotMap.DIST_PER_PULSE_HIGH_GEAR;
	}

	public void resetEncoderDistance() {
		encOffsetValueRight = getEncoderRight();
		encOffsetValueLeft = getEncoderLeft();
		getEncoderDistance();
	}
    public void stop() {
		robotDrive.drive(0, 0);
	}
}

