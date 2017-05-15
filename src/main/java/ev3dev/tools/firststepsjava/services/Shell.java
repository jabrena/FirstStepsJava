package ev3dev.tools.firststepsjava.services;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Command line wrapper
 *
 * @author Juan Antonio Breña Moral
 */
public @Slf4j class Shell {
	
	public static String execute(final String command) {

        log.debug("Command: {}", command);
		StringBuilder output = new StringBuilder();

		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			
			String line;
			while ((line = reader.readLine())!= null) {
				output.append(line).append("\n");
			}
			reader.close();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			e.printStackTrace();
		}

		return output.toString();
	}
	
	public static String execute(final String[] command) {

        for (String cmd: command) {
            log.info("Command chunk: {}", cmd);
        }
        StringBuilder output = new StringBuilder();

		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			
			String line;
			while ((line = reader.readLine())!= null) {
				output.append(line).append("\n");
			}
			reader.close();
		} catch (Exception e) {
            log.error(e.getMessage());
			e.printStackTrace();
		}

		return output.toString();
	}

}