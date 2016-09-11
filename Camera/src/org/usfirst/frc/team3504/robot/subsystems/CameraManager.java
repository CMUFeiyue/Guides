package org.usfirst.frc.team3504.robot.subsystems;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.usfirst.frc.team3504.robot.RobotMap;
import org.usfirst.frc.team3504.robot.commands.UpdateCam;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.command.Subsystem;

public class CameraManager extends Subsystem {
    
	private CameraServer server;
	private Image frame;
	public final static Logger log = Logger.getLogger(CameraManager.class.getName());
	
	public CameraManager() {
		log.setLevel(Level.ALL);
		log.info("Camera init");
		
		for(RobotMap.Camera camera : RobotMap.Camera.values()) {
			try {
				openCamera(camera);
			} catch (Exception ex) {
				log.severe("Failed to open the camera " + camera.toString());
			}
		}
				
		frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		
		server = CameraServer.getInstance();
		server.setQuality(50);
		log.info("Server configured");
		log.info("Done camera init");
	}
	
	public void openCamera(RobotMap.Camera camera) {
		int streamID;
		streamID = NIVision.IMAQdxOpenCamera(camera.systemName(), 
				NIVision.IMAQdxCameraControlMode.CameraControlModeController);
		camera.setStreamID(streamID);
		log.info(camera.toString() + " opened");
		
		NIVision.IMAQdxConfigureGrab(streamID);				
		NIVision.IMAQdxStartAcquisition(streamID);
		log.info(camera.toString() + " configured");
	}
	
	public Image getImage(RobotMap.Camera camName) {
		int streamID = camName.getStreamID();
		
		//If the camera does not have a valid stream ID, try again to open it
		if(streamID != RobotMap.Camera.INVALID_CAM) {
			openCamera(camName);	
		}
		
		//Make sure the robot does not crash if something goes wrong with acquiring image
		try {
			NIVision.IMAQdxGrab(streamID, frame, 1);
			return frame;
		}
		catch(Exception e) {
			log.warning(camName.toString() + " is not open");
			return null;
		}
	}

    public void initDefaultCommand() {
       	setDefaultCommand(new UpdateCam());
    }
}

