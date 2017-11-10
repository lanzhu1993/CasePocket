/**
 * 
 */
package com.busilinq.casepocket.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class DimenTool {
	public static void gen() {
		File file = new File("./app/src/main/res/values/dimens.xml");
		BufferedReader reader = null;
		StringBuilder sw180 = new StringBuilder();
		StringBuilder sw280 = new StringBuilder();
		StringBuilder sw350 = new StringBuilder();
		StringBuilder sw490 = new StringBuilder();

		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString;
			int line = 1;
			while ((tempString = reader.readLine()) != null) {

				if (tempString.contains("</dimen>")) {
					String start = tempString.substring(0,
							tempString.indexOf(">") + 1);
					String end = tempString.substring(tempString
							.lastIndexOf("<") - 2);
					float num = Float.valueOf(tempString.substring(
							tempString.indexOf(">") + 1,
							tempString.indexOf("</dimen>") - 2));
					sw180.append(start)
					.append((double)(Math.round(num * 0.5*100)/100.0))
					.append(end).append("\n");
					sw280.append(start)
							.append((double)(Math.round(num * 0.7*100)/100.0))
							.append(end).append("\n");
					sw350.append(start)
							.append((double)(Math.round(num * 0.9*100)/100.0))
							.append(end).append("\n");
					sw490.append(start)
							.append((double)(Math.round(num * 1.2*100)/100.0))
							.append(end).append("\n");

				} else {
					sw180.append(tempString).append("\n");
					sw280.append(tempString).append("\n");
					sw350.append(tempString).append("\n");
					sw490.append(tempString).append("\n");
				}
				line++;
			}
			reader.close();
			System.out.println("<!--  sw180 -->");
			System.out.println(sw180);
			System.out.println("<!--  sw280 -->");
			System.out.println(sw280);
			System.out.println("<!--  sw350 -->");
			System.out.println(sw350);
			System.out.println("<!--  sw490 -->");
			System.out.println(sw490);
			String sw180file = "./app/src/main/res/values-sw180dp-port/dimens.xml";
			String sw280file = "./app/src/main/res/values-sw280dp-port/dimens.xml";
			String sw350file = "./app/src/main/res/values-sw350dp-port/dimens.xml";
			String sw490file = "./app/src/main/res/values-sw490dp-port/dimens.xml";
			writeFile(sw180file, sw180.toString());
			writeFile(sw280file, sw280.toString());
			writeFile(sw350file, sw350.toString());
			writeFile(sw490file, sw490.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public static void writeFile(String file, String text) {
		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			out.println(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.close();
	}

	public static void main(String[] args) {
		gen();
	}
}
