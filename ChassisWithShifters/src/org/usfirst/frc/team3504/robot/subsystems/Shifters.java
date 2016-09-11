package org.usfirst.frc.team3504.robot.subsystems;

import org.usfirst.frc.team3504.robot.RobotMap;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;


public class Shifters extends Subsystem {
    private DoubleSolenoid shifterLeft;
    private DoubleSolenoid shifterRight;
    
    public enum Speed {	kHigh, kLow  }
    
    public enum Side {	left, right  }
    
    public boolean inHighGear;
    
    public Shifters() {
		shifterLeft = new DoubleSolenoid(RobotMap.SHIFTER_LEFT_A, RobotMap.SHIFTER_LEFT_B);
		shifterRight = new DoubleSolenoid(RobotMap.SHIFTER_RIGHT_A, RobotMap.SHIFTER_RIGHT_B);
	}
	
    public void shift(Side side, Speed speed) {  
    	DoubleSolenoid shifter;
    	DoubleSolenoid.Value setTo;
    	
    	if(side == Side.left)
    		shifter = shifterLeft;
		else
			shifter = shifterRight;
    	
    	if(speed == Speed.kHigh)
    		setTo = DoubleSolenoid.Value.kReverse;
		else 
			setTo = DoubleSolenoid.Value.kForward;
	
    	shifter.set(setTo);
    	inHighGear = ! inHighGear;
    	
    	System.out.println("Shifting " + side.toString() + " side into " + speed.toString());
	}
	
	public boolean getShifterValue(Side side) {
		DoubleSolenoid shifter = (side == Side.left)? shifterLeft : shifterRight;
		return shifter.get() != DoubleSolenoid.Value.kForward;
	}
	
	public boolean inHighGear() {
		return inHighGear;
	}
    public void initDefaultCommand() {
       
    }
}
