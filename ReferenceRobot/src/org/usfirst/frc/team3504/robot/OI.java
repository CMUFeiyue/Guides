package org.usfirst.frc.team3504.robot;

import org.usfirst.frc.team3504.robot.OI.DriveDirection;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
//TODO: write in ya damn getDrivingStick y/x biznazz so this works. CHeck DBJS afterwards. Pray to Jesus.
public class OI {

	public enum DriveDirection {kFWD, kREV}; 

	private Joystick drivingStickForward = new Joystick(0);
	private Joystick drivingStickBackward = new Joystick(1);
	
	Joystick joystickName = new Joystick(RobotMap.JOYSTICK_PORT);
	
	Button button1 = new JoystickButton(joystickName, 1); 
	Button button2 = new JoystickButton(joystickName, 2);
	Button button3 = new JoystickButton(joystickName, 3);
	private DriveDirection driveDirection = DriveDirection.kFWD; 
	
	public OI () {
	
	}

	
	public double getDrivingJoystickY() {
		if (driveDirection == DriveDirection.kFWD){
			return drivingStickForward.getY();
		}
		else {
			return -drivingStickBackward.getY(); 
		}
	}

	public double getDrivingJoystickX() {
		if (driveDirection == DriveDirection.kFWD){
			return drivingStickForward.getX();
		}
		else {
			return drivingStickBackward.getX(); 
		}
	}
}

