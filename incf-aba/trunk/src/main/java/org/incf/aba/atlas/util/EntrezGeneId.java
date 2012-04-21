package org.incf.aba.atlas.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class EntrezGeneId {
	
	/*
"entrezgeneid"  "genesymbol"
11287   "Pzp"
11298   "Aanat"
11302   "Aatk"
11303   "Abca1"
	 */
	private static final String FILE_1 = "/home/dave/aba/entrezToSymbol.csv";

	private static final String FILE_2 = "/home/dave/aba/entrezToSymbol2.csv";

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		File file1 = new File(FILE_1);
		File file2 = new File(FILE_2);
		BufferedReader in = new BufferedReader(new FileReader(file1));
		PrintWriter out = new PrintWriter(new FileWriter(file2));
		String line = null;
		String previousEntrezGeneId = null;
		String thisEntrezGeneId = null;
		while ((line = in.readLine()) != null) {
			if (line.startsWith("\"entrezgeneid\"")) {
				out.println("# EntrezGeneId,ABAGeneSymbol");
				continue;
			}
			
			// replace tab with comma
			line = line.replace('\t', ',');
			
			// remove double quotes
			line = line.replaceAll("\"", "");
			
			thisEntrezGeneId = line.split(",")[0];
			if (thisEntrezGeneId.equals(previousEntrezGeneId)) {
				continue;		// ignore repeated entrezGeneIds
			}
			
			previousEntrezGeneId = thisEntrezGeneId;
			out.println(line);
		}
		in.close();
		out.close();
	}

}
