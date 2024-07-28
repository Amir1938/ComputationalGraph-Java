package configs;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

import graph.Agent;
import graph.ParallelAgent;

/**
 * The GenericConfig class implements the Config interface and is responsible for reading configuration files
 * and creating agents for the graph.
 */
public class GenericConfig implements Config {

	private String filePath;
	private ArrayList<ParallelAgent> agents = new ArrayList<>();

	/**
	 * Sets the configuration file path.
	 *
	 * @param string the file path to the configuration file
	 */
	public void setConfFile(String string) {
		this.filePath = string;
	}

	/**
	 * Reads the configuration from the file path and creates the agents for the graph.
	 */
	public void create() {
		List<String> lines = new ArrayList<>();

		try {
			File myObj = new File(filePath);
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				lines.add(data); // Add each line to the ArrayList
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + e.getMessage());
		}

		if (lines.size() % 3 != 0) {
			return;
		}

		// Process each group of 3 lines
		for (int i = 0; i < lines.size(); i += 3) {
			String agentType = lines.get(i);
			String[] subs = lines.get(i + 1).split(",");
			String[] pubs = lines.get(i + 2).split(",");

			Class<?> myClass;
			try {
				myClass = Class.forName(agentType);
				@SuppressWarnings("rawtypes")
				Class[] parameterType = {String[].class, String[].class};
				try {
					Object agentInstance = myClass.getConstructor(parameterType).newInstance(subs, pubs);
					ParallelAgent pa = new ParallelAgent((Agent) agentInstance, 10);
					agents.add(pa);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Returns the name of the configuration.
	 *
	 * @return the name of the configuration
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Returns the version of the configuration.
	 *
	 * @return the version of the configuration
	 */
	@Override
	public int getVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Closes the configuration and releases any resources held by it.
	 */
	@Override
	public void close() {
		for (ParallelAgent pa : this.agents) {
			pa.close();
		}
	}
}
