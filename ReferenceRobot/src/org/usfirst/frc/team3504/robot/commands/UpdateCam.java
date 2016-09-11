package org.usfirst.frc.team3504.robot.commands;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.usfirst.frc.team3504.robot.Robot;
import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class UpdateCam extends Command {

	public final static Logger log = Logger.getLogger(UpdateCam.class.getName());

    public UpdateCam() {
    	log.setLevel(Level.WARNING);
    	requires(Robot.camera);
    	requires(Robot.vision);
    }

    protected void initialize() {
    }

    protected void execute() {
    	log.info("Grabbing image");
    	ArrayList<Image> images = Robot.camera.getImages();
    	
    	if(images.isEmpty()) {
    		log.warning("No image was grabbed");
    	}
    		
    	for(Image img : images) {
    		log.info("Processing image");
    //		Image toDisplay = Robot.vision.resizeImage(img, 2, 2);
    //		Image toDisplay = img;
    		Image toDisplay = Robot.vision.findTest(img);
    		CameraServer.getInstance().setImage(toDisplay);
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