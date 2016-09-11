package tuner;

import java.awt.*;
import javax.swing.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class PIDTuner extends JFrame
{
	//Size of the ui window
	private static final int WINDOW_WIDTH = 1000;
	private static final int WINDOW_HEIGHT = 1000;
	
	//Determines how this program connects to a roborio
	private static final int TEAM_NUMBER = 9999;
	
	//Extra time to wait before aborting runMotor and assuming error (in seconds)
	private static final int TIME_BUFFER = 1;
	
	private static final Font font = new Font("Verdana", Font.BOLD, 20);

	private static NetworkTable table;
	
	//UI components
	private HashMap<String, JTextField> values;
	private JPanel buttonPane;
	private Container contentPane;
	private ChartPanel graphPane;
	
	private JFreeChart graph;
	
	public PIDTuner() {
		
		setTitle("PID Tuner");
		
		/*
		 * Create the button pane to display all the selections
		 */
		buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    	buttonPane.add(Box.createHorizontalGlue());
    	buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		
		addButton("Run Motor", new MotorButtonHandler());
		addButton("Save", new SaveButtonHandler());
		addButton("Exit", new ExitButtonHandler());

		
		/*
		 * Create the graph pane to contain the plot of data
		 */
		graph = createGraph();
		graphPane = new ChartPanel(graph);
        
		
		/*
		 * Create panel to receive user input for variables such as kP, kI and kD
		 * These variables match the ones a TunablePIDSubsystem will use as input
		 */
		JPanel valuePane = new JPanel();
		valuePane.setLayout(new BoxLayout(valuePane, BoxLayout.LINE_AXIS));
		valuePane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		values = new HashMap<String, JTextField>();		
		String [] valueNames = {"kP", "kI", "kD", "f", "timeout"};
		JLabel tempLable;
		JTextField tempField;
		
		for(String name : valueNames) {
			tempLable = new JLabel(name + ": ", SwingConstants.RIGHT);
			tempField = new JTextField(10);
			values.put(name, tempField);
			valuePane.add(tempLable);
			valuePane.add(tempField);
		}
			

		/*
		 * Combine all the separate panels into a single window
		 */
		contentPane = getContentPane();
		contentPane.add(valuePane, BorderLayout.PAGE_START);
		contentPane.add(graphPane, BorderLayout.CENTER);
		contentPane.add(buttonPane, BorderLayout.PAGE_END);
		
		// Set the font of all components to the desired one, otherwise text is tiny
		setPanelFont(contentPane.getComponents(), font);
		
        contentPane.validate();
        
        // Display the window
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT );
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	/*
	 * Returns a graph based on the data from createDataset();
	 */
	private JFreeChart createGraph() {
		String graphTitle = "Error vs Time";
		String xLable = "Time";
		String yLable = "Error";
		
		return ChartFactory.createLineChart(
				graphTitle, xLable, yLable,
				createDataset(), 
				PlotOrientation.VERTICAL,
				true,true,false);
	}
	
	/*
	 * Facilitates adding buttons to the bottom of the window
	 * Requires there be a handler for the button that will perform the desired action
	 */
	private void addButton(String name, ActionListener handler) {
		JButton button = new JButton(name);
		button.addActionListener(handler);
		buttonPane.add(button);
	}
	
	/*
	 * Handles a click of the "Run Motor" button
	 * Reads off values of controllable variables and pass them on to the roborio
	 * Reads off resulting measurements taken by the roborio after the motor is run
	 * Regraphs the data in the center of the window and stores the graph
	 */
	private class MotorButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//Replace original graph with a loading screen to prevent confusion
			contentPane.remove(graphPane);
			JLabel loading = new JLabel("Loading...");
			contentPane.add(loading, BorderLayout.CENTER);
			
			runMotor();
			
			double startTime = System.currentTimeMillis();
			double runTime = (table.getNumber("timeout", 0) + TIME_BUFFER) * 1000;
			double endTime = startTime + runTime;
			
			//While the robot is running, with a max time to prevent infinite loops
			while(!table.getBoolean("doneCommand", false) && 
					System.currentTimeMillis() < endTime) {
				//do nothing
			}
			
			//If loop ended because the robot actually finished, graph new data
			if(table.getBoolean("doneCommand", false)) {	
				//Make a new graph based on the data collected from roborio
				graph = createGraph();
				ChartPanel graphPane = new ChartPanel(graph);
				contentPane.add(graphPane, BorderLayout.CENTER);
		        contentPane.validate();
		        
		        //Reset the value of doneCommand for next time
		        table.putBoolean("doneCommand", false);
			}
		}
		
		/* 
		 * Sets the flag in the table that indicates to the motor it needs
		 * to create a new TunePID command.
		 */
		public void runMotor() {
			for(String name : values.keySet()) {
				String textBox = values.get(name).getText();
				double value;
				
				if(! textBox.equals("")) {
					//If the value has been changed in the text box, update it
					value = Double.parseDouble(textBox);
				} else {
					//Otherwise take the old value from the roborio table
					value = table.getNumber(name, 0);
				}
				
				table.putNumber(name, value);
			}
			
			table.putBoolean("startCommand", true);
		}
	}
	
	/*
	 * Opens a filechooser, letting the user select where to save the file
	 * as well as what to call it. Remembers previous location if this is not
	 * the first file saved.
	 * Saves the stored graph at that location
	 */
	public class SaveButtonHandler implements ActionListener {
		private File chartPath = new File("");
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser(chartPath);
			fileChooser.setPreferredSize(new Dimension(1000, 500));
			setPanelFont(fileChooser.getComponents(), font);	    

			int returnVal = fileChooser.showSaveDialog(fileChooser);
			
			String path = "";
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				path = fileChooser.getSelectedFile().getAbsolutePath();
			}

			int width = 640; 
			int height = 480;
			chartPath = new File(path + ".jpeg"); 
			
			try {
				ChartUtilities.saveChartAsJPEG(chartPath, graph, width ,height);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/*
	 * Exits the program
	 */
	public class ExitButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	
	/* 
	 * Sets up the connection to the robot
	 * Starts the window and button listeners
	 */
	public static void main(String[] args) {
		NetworkTable.setClientMode();
		NetworkTable.setIPAddress("roborio-" + TEAM_NUMBER + "-frc.local");
		table = NetworkTable.getTable("PID");
		PIDTuner rectObj = new PIDTuner();
	}
	
	/*
	 * Reads the data from the roborio and creates a graphable dataset from it
	 */
	public static DefaultCategoryDataset createDataset() {	
		double[] tempArray = {0};
		double[] values = table.getNumberArray("values", tempArray);
		double[] times = table.getNumberArray("times", tempArray);
		
		double temp = 0;
		double kP = table.getNumber("kP", temp);
		double kD = table.getNumber("kD", temp);
		double kI = table.getNumber("kI", temp);
		double f = table.getNumber("f", temp);
		String PIDInfo = ("kP: " + kP + ", kD: " + kD + ", kI: " + kI + ", f: " + f);
		

		DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
		for(int i = 0; i < times.length; i++ ) {
			double time = times[i];
			double value = values[i];
			dataset.addValue( value , PIDInfo , time + "" );
		}
		
		return dataset;
	}
	
	/*
	 * Recursively updates the font of each component to be the desired one
	 */
	public static void setPanelFont(Component[] comp, Font font) {
		for(int x = 0; x < comp.length; x++) {
			if(comp[x] instanceof Container) {
				setPanelFont(((Container)comp[x]).getComponents(), font);
			}
			
			try {
				comp[x].setFont(font);
			}
			catch(Exception e){ }
		}
	}
}