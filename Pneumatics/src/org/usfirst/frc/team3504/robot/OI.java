package org.usfirst.frc.team3504.robot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

import org.usfirst.frc.team3504.robot.commands.ClosePneumatic;
import org.usfirst.frc.team3504.robot.commands.OpenPneumatic;

public class OI {
	
	Joystick Joy = new Joystick(RobotMap.Joystick); 
	Button button1 = new JoystickButton(Joy, 1); //Open Pneumatic 
	Button button2 = new JoystickButton(Joy, 2); //Close Pneumatic

	public OI () 
	{
		button1.whenPressed(new OpenPneumatic());
		button2.whenPressed(new ClosePneumatic());
	}
}

