package org.usfirst.frc.team3504.robot;

public class RobotMap {

	//TODO: Fix ports for motors
	public static final int MASTER_LEFT = 1;
	public static final int SLAVE_LEFT = 2;
	
	public static final int MASTER_RIGHT= 11;
	public static final int SLAVE_RIGHT= 12;
	
	public static final int JOYSTICK_PORT = 0;
	public static final int LIMITSWITCH_PORT = 0; 
	
	// Encoder-to-distance constants
	// How many ticks are there on the encoder wheel?
	private static final double pulsePerRevolution = 360;

	// How far to we travel when the encoder turns one full revolution?
	// Gear ratio is turns of the wheel per turns of the encoder
	private static final double wheelSize = 8.0;
	private static final double gearRatio = (1/27.21);
		
	private static final double distPerRevolution = 
			wheelSize * Math.PI * gearRatio; //(9.07)
	
	// Given our set of wheels and gear box, how many inches do we travel per pulse?
	public static final double DIST_PER_PULSE = 
			distPerRevolution / pulsePerRevolution;
	
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
