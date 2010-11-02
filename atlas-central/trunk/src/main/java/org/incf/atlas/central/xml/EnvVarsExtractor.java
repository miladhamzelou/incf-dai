package org.incf.atlas.central.xml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class EnvVarsExtractor {

	private ArrayList<String> envVars = new ArrayList<String>();

	public EnvVarsExtractor() throws IOException {
		update();
	}

	public static void main(String[] args) throws IOException {
		EnvVarsExtractor eve = new EnvVarsExtractor();
		String str = eve.var("MEDIATOR_HOME");
		System.out.println(str);
	}

	public ArrayList<String> envVars() {
		return this.envVars;
	}

	public String var(String key) {
		String var = null;
		for (int i = 0; i < this.envVars.size(); i++) {
			String line = this.envVars.get(i);
			String[] str2 = line.split("=");
			if ((str2.length == 2) && str2[0].equals(key)) {
				var = str2[1];
				break;
			}
		}
		return var;
	}

	private void update() throws IOException {
		// Find out what OS we are on.
		String osName = System.getProperty("os.name");
		String envCommand;
		if ("Linux".equals(osName) || "Mac OS X".equals(osName)) {
			envCommand = "env";
		} else {
			envCommand = "cmd /C set";
		}
		InputStream is = Runtime.getRuntime().exec(envCommand).getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line;
		while ((line = br.readLine()) != null) {
			this.envVars.add(line);
		}
	}
}
