package org.usfirst.frc.team3504.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import org.usfirst.frc.team3504.robot.subsystems.DriveMotor;

public class Robot extends IterativeRobot {

    public static OI oi;
    public static final DriveMotor driveMotor = new DriveMotor();
   Command autonomousCommand;

	public void robotInit() {
   	 oi = new OI();
	}
    
    public void disabledPeriodic() {
   	 Scheduler.getInstance().run();
    }

	public void autonomousInit() {
    	// schedule the autonomous command (example)
    	if (autonomousCommand != null) autonomousCommand.start();
	}

	public void autonomousPeriodic() {
    	Scheduler.getInstance().run();
	}

	public void teleopInit() {
       	if (autonomousCommand != null) autonomousCommand.cancel();
	}

	public void disabledInit(){

	}

	public void teleopPeriodic() {
    	Scheduler.getInstance().run();
	}
	public void testPeriodic() {
    	LiveWindow.run();
	}
}