package org.usfirst.frc.team3504.robot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import org.usfirst.frc.team3504.robot.commands.DriveUntilLimitSwitchIsPressed;

public class OI {
	
	Joystick Joy = new Joystick(RobotMap.JOYSTICK_PORT); 
	Button button1 = new JoystickButton(Joy, 1); //drive forward after button release until limit bumped 
	
	public OI () {
		button1.whenPressed(new DriveUntilLimitSwitchIsPressed());
	}
}

