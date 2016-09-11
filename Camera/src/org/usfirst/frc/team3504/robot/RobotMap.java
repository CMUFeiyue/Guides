package org.usfirst.frc.team3504.robot;

public class RobotMap {
	
	public enum Camera {
		//Enum: camera nickname, Value: roboRIO provided camera ID (or system name)
		//For example, MY_CAMERA("cam0");
		MY_CAMERA("cam0");
		
		public static final int INVALID_CAM = -1;
		
		//roboRIO provided camera ID
		protected final String systemName;
		
		//stream returned when camera gets opened. default is invalid (-1).
		protected int streamDescriptor;
		
    	Camera(String systemName) {	
    		this.systemName = systemName;
    		this.streamDescriptor = INVALID_CAM;
    	}
    	
    	public String systemName()	{
    		return systemName;	
    	}
    	
    	public void setStreamID(int streamNum) {
    		this.streamDescriptor = streamNum;
    	}
    	
    	public int getStreamID() {
    		return streamDescriptor;
    	}
	}
}