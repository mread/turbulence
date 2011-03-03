package com.github.mread.turbulence4j.git;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class GitAdapter {

	public boolean isRepo(File directory)  {
		BufferedReader errorStream = errorStreamForGitStatus(directory);
		return !containsText(errorStream, "Not a git repository");
	}

	private boolean containsText(BufferedReader input, String textToMatch) {
		String line=null;
        try {
			while((line=input.readLine()) != null) {
				if (line.contains(textToMatch)) {
			    	return true;
			    }
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		return false;
	}

	private BufferedReader errorStreamForGitStatus(File directory) {
		Runtime runtime = Runtime.getRuntime();
		Process process = null;
		try {
			process = runtime.exec("git status", null, directory);
			process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return new BufferedReader(new InputStreamReader(process.getErrorStream()));
	}

}
