package org.usfirst.frc.team3504.robot;

import org.usfirst.frc.team3504.robot.commands.*;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class OI {
	Joystick joystickName = new Joystick(RobotMap.JOYSTICK_PORT);
		
	Button button1 = new JoystickButton(joystickName, 1); 
	Button button2 = new JoystickButton(joystickName, 2);
	Button button3 = new JoystickButton(joystickName, 3);
	
	public OI () {
		button1.whenPressed(new DriveForward()); 
		button2.whenPressed(new DriveBackward());
		button3.whenPressed(new DriveStop());
	}

}

