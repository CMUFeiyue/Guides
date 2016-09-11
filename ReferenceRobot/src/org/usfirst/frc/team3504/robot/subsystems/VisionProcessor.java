package org.usfirst.frc.team3504.robot.subsystems;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ImageType;
import com.ni.vision.NIVision.ShapeMode;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class VisionProcessor extends Subsystem {

	//count of targets to ensure unique target ids
	static int targetCount = 0;
	ArrayList<Target> targets = new ArrayList<Target>();
	public final static Logger log = Logger.getLogger(VisionProcessor.class.getName());
	
	public VisionProcessor() {	
		CameraType camType = CameraType.AXIS_M10011;
		
		log.setLevel(Level.ALL);

		Target redBox = new Target("Red Box", 0.5, 75.0, 100, 255, 0, 80, 0, 80);
		Target yellowCard = new Target("Yellow Card", 0.5, 75.0, 0, 100, 0, 80, 0, 80);
    	targets.add(redBox);
    	targets.add(yellowCard);
	}

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
    }
    
    public Image findTest(Image inputImg) {
    	log.info("Running vision pipeline");
    	
    	Image outputImg = findTarget(inputImg, targets.get(1));
    	return outputImg;
    }
	
	/**
	 * Combines all the helper functions to find a specified target in an image
	 * TODO: explain this better
	 * @param inputImg the image to be processed
	 * @param target the target to be found in the image
	 */
	public Image findTarget(Image inputImg, Target target) {
		Image scaledImg = resizeImage(inputImg, 2, 2);
		Image thresholdedImg = thresholdImage(scaledImg, target);
		ArrayList<Particle> particles = identifyParticles(thresholdedImg, target);
		log.info("num particles: " + particles.size());
		Image boxImg = drawParticleBox(inputImg, particles);
		return boxImg;
	}
	
	/**
	 * Scales the image down in both dimensions by the factor provided.
	 * For example, resizeImage(inputImg, 2, 3) would result in an image 
	 * that is half as wide and a third as high as the original.
	 * @param inputImg the image to be resized
	 * @param xscale the factor by which to shrink the x-dimension of the image
	 * @param yscale the factor by which to shrink the y-dimension of the image
	 * @return
	 */
	public Image resizeImage(Image inputImg, int xscale, int yscale) {
		Image scaledImg = NIVision.imaqCreateImage(ImageType.IMAGE_RGB, 0);
		
		NIVision.GetImageSizeResult inputImgSize = NIVision.imaqGetImageSize(inputImg);
		NIVision.Rect imageRect = new NIVision.Rect(0, 0, inputImgSize.height, inputImgSize.width);
	
		NIVision.imaqScale(scaledImg, inputImg, xscale, yscale, NIVision.ScalingMode.SCALE_SMALLER, imageRect);
		return scaledImg;
	}
	
	/**
	 * Thresholds a given image based on the HSV ranges of the target
	 * @param inputImg the image to the thresholded
	 * @param target the target to be thresholded for (ie. the object to be isolated in the image) 
	 * @returns binary image that results in thresholding
	 */
	public Image thresholdImage(Image inputImg, Target target) {
		//binary image to store the output of the NIVision thresholding function
		Image thresholdedImg = NIVision.imaqCreateImage(ImageType.IMAGE_U8, 0);
		
		NIVision.imaqColorThreshold(thresholdedImg, inputImg, 255, 
				NIVision.ColorMode.HSV, target.hueRange, target.satRange, target.valRange);
		
		return thresholdedImg;
	}
	
	
	/**
	 * Finds all the particles that are big enough to be considered the given target
	 *  
	 * @param inputImg the binary image with all the particles that need to be filtered
	 * @param target the target against which the filtering is done. 
	 * 			This determines the sizes of the particles kept
	 * @return the number of particles found after filtering, all of which are large enough 
	 * 			to potentially be the target
	 */
	public ArrayList<Particle> identifyParticles(Image inputImg, Target target) {
		Image filteredImg = NIVision.imaqCreateImage(ImageType.IMAGE_U8, 0);
		
		NIVision.ParticleFilterCriteria2 filterCriteria[] = new NIVision.ParticleFilterCriteria2[1];
		filterCriteria[0] = new NIVision.ParticleFilterCriteria2
				(NIVision.MeasurementType.MT_AREA_BY_IMAGE_AREA, target.minPercentArea, 100.0, 0, 0);
		
		NIVision.ParticleFilterOptions2 filterOptions = new NIVision.ParticleFilterOptions2(0,0,1,1);

		int imaqError = NIVision.imaqParticleFilter4(filteredImg, inputImg, filterCriteria, 
														filterOptions, null);

		int numParticles = NIVision.imaqCountParticles(filteredImg, 1);
		
		ArrayList<Particle> particles = new ArrayList<Particle>();
		
		//properties of the particle that should be measured and stored in a Particle instance
		NIVision.MeasurementType[] imaqProperties = { 	NIVision.MeasurementType.MT_AREA_BY_IMAGE_AREA,
														NIVision.MeasurementType.MT_AREA,
														NIVision.MeasurementType.MT_FIRST_PIXEL_X,
														NIVision.MeasurementType.MT_FIRST_PIXEL_X,
														NIVision.MeasurementType.MT_BOUNDING_RECT_HEIGHT,
														NIVision.MeasurementType.MT_BOUNDING_RECT_WIDTH			};
		
		//the properties of a particle in order (areaPercent, area, x, y, width and height)
		double[] parProperties = new double[6];
		
		for(int particleIndex = 0; particleIndex < numParticles; particleIndex++){
			for(int property = 0; property < imaqProperties.length; property++) {
				parProperties[property] = 
						NIVision.imaqMeasureParticle
							(filteredImg, particleIndex, 0, imaqProperties[property]);
			}
			Particle par = new Particle(parProperties);
			particles.add(par);
		}
		
		return particles;
	}
	
	/**
	 * draws a bounding box around the largest particle found overlayed on the provided image
	 * @param inputImg image containing the particle to see the bounding box in context
	 * @param particles list of particles found in the image
	 * @return the inputImg with the largest particles bounding box overlayed
	 */
	public Image drawParticleBox(Image inputImg, ArrayList<Particle> particles) {
		if(particles.size() != 0) {
			Image boundedImg = NIVision.imaqCreateImage(ImageType.IMAGE_RGB, 0);
			
			particles.sort(null);
			Particle particle = particles.get(0);
			
			log.info("particle x: " + particle.x);
			log.info("particle y: " + particle.y);
			log.info("particle height: " + particle.height);
			log.info("particle width: " + particle.width);
	
			NIVision.Rect rect = 
					new NIVision.Rect(particle.y, particle.x, particle.height, particle.width);
			NIVision.imaqDrawShapeOnImage(boundedImg, inputImg, rect, 
											DrawMode.DRAW_VALUE, ShapeMode.SHAPE_RECT, 0.0f);		
	
			return boundedImg;
		}
		else {
			log.warning("no particles found");
			return inputImg;
		}
	}  
    
    
    /************************************************************
     * BEGINNIG OF HELPER CLASSES TO STORE AND MODIFY INFORMATION
     * **********************************************************
     */
    
    /**
	 * contains all the attributes of a particle for further processing
	 */
	public class Particle implements Comparator<Particle>, Comparable<Particle> {
		double percentArea;
		double area;
		int x;
		int y;
		int width;
		int height;
		
		/**
		 * @param percentArea the area of the particle as a percent of the whole image
		 * @param area the area of the particle
		 * @param x x-coordinate of the top left point of the bounding box of the particle
		 * @param y y-coordinate of the top left point of the bounding box of the particle
		 * @param width width of the bounding box of the particle
		 * @param height height of the bounding box of the particle
		 */
		public Particle(double percentArea, double area, int x, int y, int width, int height) {
			this.percentArea = percentArea;
			this.area = area;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}
		
		/**
		 * @param properties the properties of a particle in order 
		 * 			(areaPercent, area, x, y, width and height)
		 */
		public Particle(double[] properties) {
			this(properties[0], 
				 properties[1], 
				(int) properties[2], 
				(int) properties[3], 
				(int) properties[4], 
				(int) properties[5]);
		}
				
		public int compareTo(Particle r) {
			return (int)(r.area - this.area);
		}

		public int compare(Particle r1, Particle r2) {
			return (int)(r1.area - r2.area);
		}
	}
	
	
	/**
	 * Contains all the information for a target (like the ball or towers) so that the rest of the 
	 * functions can threshold and particle detect several different objects of interest easily.
	 */
	
	//TODO: fix the parameters for a target
	public class Target {
		int id;
		String targetName;
		double minPercentArea;
		double minScore;
		NIVision.Range hueRange, satRange, valRange;
		
		/**
		 * @param targetName identifier for the target so it's easier to keep several of them straight. 
		 * @param minPercentArea minimum area of a particle as a percentage of total image area
		 * @param minScore minimum score to be considered the target
		 * @param hueMin minimum hue considered a part of the target
		 * @param hueMax maximum hue considered a part of the target
		 * @param satMin minimum saturation considered a part of the target
		 * @param satMax maximum saturation considered a part of the target
		 * @param valMin minimum value considered a part of the target
		 * @param valMax maximum value considered a part of the target
		 */
		public Target(String targetName, double minPercentArea, double minScore, 
							int hueMin, int hueMax, int satMin, int satMax, int valMin, int valMax) {
			this.id = targetCount;
			targetCount++;
			
			this.targetName = targetName;
			
			this.minPercentArea = minPercentArea;
			this.minScore = minScore;
			
			this.hueRange = new NIVision.Range(hueMin,hueMax);
			this.satRange = new NIVision.Range(satMin,satMax);
			this.valRange = new NIVision.Range(valMin,valMax);
		}
		
		/**
		 * Creates a target with default parameters
		 */
		public Target() {
			this("Default", 0.5, 0.5, 0, 255, 0, 255, 0, 255);
		}
		
		/**
		 * @param other the Target to test equality against
		 * @returns true if the targetNames of this Target and the given Target match 
		 */
		public boolean equals(Target other) {
			return this.getDescriptor().equals(other.getDescriptor());
		}
		
		/**
		 * @return the unique descriptor of the target, composed of the id and targetName
		 */
		public String getDescriptor() {
			return (this.id + ". " + this.targetName + " ");
		}
		
		/**
		 * Publish all the values of the target HSV ranges in the SmartDashboard
		 */
		public void displayRanges() {
			SmartDashboard.putNumber(this.getDescriptor() + " hue min", hueRange.minValue);
			SmartDashboard.putNumber(this.getDescriptor() + " hue max", hueRange.maxValue);
			SmartDashboard.putNumber(this.getDescriptor() + " sat min", satRange.minValue);
			SmartDashboard.putNumber(this.getDescriptor() + " sat max", satRange.maxValue);
			SmartDashboard.putNumber(this.getDescriptor() + " val min", valRange.minValue);
			SmartDashboard.putNumber(this.getDescriptor() + " val max", valRange.maxValue);		
		}
		
		/**
		 * Update the values of the target HSV ranges from the data entered in the SmartDashboard
		 */
		public void calibrateRanges() {
			hueRange.minValue = (int)SmartDashboard.getNumber(this.getDescriptor() + " hue min", hueRange.minValue);
			hueRange.maxValue = (int)SmartDashboard.getNumber(this.getDescriptor() + " hue max", hueRange.maxValue);
			satRange.minValue = (int)SmartDashboard.getNumber(this.getDescriptor() + " sat min", satRange.minValue);
			satRange.maxValue = (int)SmartDashboard.getNumber(this.getDescriptor() + " sat max", satRange.maxValue);
			valRange.minValue = (int)SmartDashboard.getNumber(this.getDescriptor() + " val min", valRange.minValue);
			valRange.maxValue = (int)SmartDashboard.getNumber(this.getDescriptor() + " val max", valRange.maxValue);
		}
	}
	
	
    /**
     * enum structure to help modify separate parts of the processing code to work with different cameras.
     * Any attributes that change based on the camera type should be added here and can then be refereced
     * by setting the "camType" variable (found below) to a different enum value.
     */
    public enum CameraType {
		AXIS_M10011 (49.4), 
		AXIS_M1013 (64), 
		AXIS_206 (51.7), 
		HD3000_SQR (52), 
		HD3000_640X480 (60);
    	
    	//View angle of camera, changes depending on camera used.
    	protected final double viewAngle;
    	CameraType(double viewAngle) {	this.viewAngle = viewAngle;		}
    	private double viewAngle()	{	return viewAngle;	}
	}
}

