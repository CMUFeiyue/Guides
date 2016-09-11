package org.usfirst.frc.team3504.robot.commands;

import java.util.logging.Logger;

import org.usfirst.frc.team3504.robot.Robot;
import org.usfirst.frc.team3504.robot.RobotMap;

import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.command.Command;

public class UpdateCam extends Command {

	public final static Logger log = Logger.getLogger(UpdateCam.class.getName());

    public UpdateCam() {
    	requires(Robot.camera);
    	requires(Robot.vision);
    }

    protected void initialize() {
    }

    protected void execute() {
     	log.info("Grabbing image");
    	Image img = Robot.camera.getImage(RobotMap.Camera.MY_CAMERA);
    	
    	if(img != null) {
    		log.info("Processing image");
    		Image toDisplay = Robot.vision.findTest(img);
//    		Image toDisplay = img;
    		CameraServer.getInstance().setImage(toDisplay);
    	}
    	else {
    		log.warning("No image was grabbed");
    	}
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}