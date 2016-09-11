package org.usfirst.frc.team3504.robot.subsystems;

import org.usfirst.frc.team3504.robot.RobotMap;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

public class PIDGearBox extends TunablePIDSubsystem {

	private CANTalon driveMaster;
	private CANTalon driveSlave;
	
	public PIDGearBox(String name, double kP, double kI, double kD, double f) {
		super(name, kP, kI, kD, f);
	}
	
	public PIDGearBox() {
		this("GearBox", 0.1, 1, 0, 0);
    	
    	driveMaster = new CANTalon(RobotMap.DRIVE_MASTER);
		driveSlave = new CANTalon(RobotMap.DRIVE_SLAVE);
		
		driveSlave.changeControlMode(CANTalon.TalonControlMode.Follower);
		driveSlave.set(driveMaster.getDeviceID());
		
     	setInputRange(-1, 1);
    	setPercentTolerance(10);
    	getPIDController().setContinuous(false);
    	
    	LiveWindow.addActuator(this.toString(), "PID Controller", getPIDController());
    }
    
    public void initDefaultCommand() {
    
    }
    
    protected double returnPIDInput() {
    	return driveMaster.getSpeed();
    }
    
    protected void usePIDOutput(double output) {
    	driveMaster.pidWrite(output);
    }
    
    public void stop() {
    	driveMaster.set(0);
    }
}