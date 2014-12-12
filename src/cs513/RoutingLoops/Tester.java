package cs513.RoutingLoops;

import java.io.File;
import java.io.IOException;

import cs513.parser.OutputParser;

public class Tester {

	public static void main(String[] args) {

		TooManyHops tooManyHop = new TooManyHops();
		ErroneousRouting errRouting = new ErroneousRouting();
		RoutingLoops routeLoop = new RoutingLoops();
		
		File[] files = new File("output_trace_1").listFiles();

		OutputParser parser = new OutputParser();
		try {
			parser.showFiles(files);
			tooManyHop.process(parser.m_hostmap);
			errRouting.process(parser.m_hostmap);
			routeLoop.process(parser.m_hostmap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
