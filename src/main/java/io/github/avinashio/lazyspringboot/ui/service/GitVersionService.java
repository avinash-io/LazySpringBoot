package io.github.avinashio.lazyspringboot.ui.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class GitVersionService {

public String version() {
	
	try {
		
		Process process =
				new ProcessBuilder(
						"git",
						"--version")
						.start();
		
		try (BufferedReader reader =
					 new BufferedReader(
							 new InputStreamReader(
									 process.getInputStream()))) {
			
			String line =
					reader.readLine();
			
			process.waitFor();
			
			return line == null
						   ? "Unknown"
						   : line;
		}
		
	} catch (IOException
			 | InterruptedException exception) {
		
		if (exception
					instanceof InterruptedException) {
			
			Thread.currentThread()
					.interrupt();
		}
		
		return "Not Installed";
	}
}
}