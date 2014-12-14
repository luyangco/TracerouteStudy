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

	/* Main entry for generating the data */
	public static void main(String[] args) {
		
		// compress the files to reduce the process time
		File[] files = new File("trace_1").listFiles();
		try {
			TracerouteLogParser.processFiles(files, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// perform the analysis and print out result
		TooManyHops tooManyHop = new TooManyHops();
		ErroneousRouting errRouting = new ErroneousRouting();
		RoutingLoops routeLoop = new RoutingLoops();
		TemporaryOutage outage = new TemporaryOutage();
		RoutingSymmetry routSym = new RoutingSymmetry();
		
		File[] outputFiles = new File("output_trace_1").listFiles();

		OutputParser outputParser = new OutputParser();
		
		try {
			outputParser.showFiles(outputFiles);
			routeLoop.process(outputParser.m_hostmap);
			tooManyHop.process(outputParser.m_hostmap);
			errRouting.process(outputParser.m_hostmap);
			outage.process(outputParser.m_hostmap);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		SimplifyParser simParser = new SimplifyParser();
		try {
			simParser.showFiles(outputFiles);
			routSym.process(simParser.m_hostmap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
