
package org.usfirst.frc.team3504.robot;

import org.usfirst.frc.team3504.robot.commands.TunePID;
import org.usfirst.frc.team3504.robot.subsystems.PIDGearBox;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {

	public static OI oi;
	
	public static NetworkTable table;
    Command autonomousCommand;
    SendableChooser chooser;
	public static PIDGearBox driveTrain;
	Command pidCommand;

    public void robotInit() {
    	System.out.println("robot init");
        driveTrain = new PIDGearBox();
		oi = new OI();
        chooser = new SendableChooser();
        SmartDashboard.putData("Auto mode", chooser);
    	table = NetworkTable.getTable("PID");
    	table.putBoolean("startCommand", false);
    }
	
    public void disabledInit(){

    }
	
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

    public void autonomousInit() {
        autonomousCommand = (Command) chooser.getSelected();
        
        if (autonomousCommand != null) autonomousCommand.start();
    }

    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {

        if (autonomousCommand != null) autonomousCommand.cancel();
    }

    public void teleopPeriodic() {
    	boolean startCommand = table.getBoolean("startCommand", false);
    	
    	if(startCommand) {
    		pidCommand = new TunePID<PIDGearBox>(Robot.driveTrain);
    		table.putBoolean("startCommand", false);
    		pidCommand.start();
    	}
    	
        Scheduler.getInstance().run();
    }
 
    public void testPeriodic() {
        LiveWindow.run();
    }
}
