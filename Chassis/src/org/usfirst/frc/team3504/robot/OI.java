package org.usfirst.frc.team3504.robot;

import edu.wpi.first.wpilibj.Joystick;

public class OI {

	Joystick drivingStick = new Joystick(RobotMap.JOYSTICK_PORT);
	
	public double getDrivingJoystickY() {
		return -drivingStick.getY();
	}

	public double getDrivingJoystickX() {
		return -drivingStick.getX();
	}
}