package cs513.test;

import java.io.File;
import java.io.IOException;

import cs513.analysis.ErroneousRouting;
import cs513.analysis.RoutingLoops;
import cs513.analysis.RoutingSymmetry;
import cs513.analysis.TooManyHops;
import cs513.parser.OutputParser;

public class Tester {

	public static void main(String[] args) {

		TooManyHops tooManyHop = new TooManyHops();
		ErroneousRouting errRouting = new ErroneousRouting();
		RoutingLoops routeLoop = new RoutingLoops();
		
		File[] files = new File("output_trace_2").listFiles();

		OutputParser parser = new OutputParser();
		try {
			parser.showFiles(files);
			routeLoop.process(parser.m_hostmap);
			tooManyHop.process(parser.m_hostmap);
			errRouting.process(parser.m_hostmap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}