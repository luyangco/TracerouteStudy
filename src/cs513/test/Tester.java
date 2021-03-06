package cs513.test;

import java.io.File;
import java.io.IOException;

import cs513.analysis.ErroneousRouting;
import cs513.analysis.RoutingLoops;
import cs513.analysis.RoutingSymmetry;
import cs513.analysis.TemporaryOutage;
import cs513.analysis.TooManyHops;
import cs513.parser.OutputParser;
import cs513.parser.SimplifyParser;
import cs513.parser.TracerouteLogParser;

public class Tester {
	
	// select the dataset to be analysed, either "trace_1" or "trace_2"
	private static final String DATASET = "trace_2";

	/* 
	 * Main entry for generating the data 
	 * To run routing symmetry, run the main() in RoutingSymmetry.java 
	 */
	public static void main(String[] args) {
		
		// compress the files to reduce the process time
		File[] files = new File(DATASET).listFiles();
		try {
			TracerouteLogParser.processFiles("output_" + DATASET, files, null);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// perform the analysis and print out result
		TooManyHops tooManyHop = new TooManyHops();
		ErroneousRouting errRouting = new ErroneousRouting();
		RoutingLoops routeLoop = new RoutingLoops();
		TemporaryOutage outage = new TemporaryOutage();
		
		File[] outputFiles = new File("output_" + DATASET).listFiles();

		OutputParser outputParser = new OutputParser();
		
		try {
			outputParser.generateOuput(outputFiles);
			routeLoop.process(outputParser.m_hostmap);
			tooManyHop.process(outputParser.m_hostmap);
			errRouting.process(outputParser.m_hostmap);
			outage.process(outputParser.m_hostmap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
