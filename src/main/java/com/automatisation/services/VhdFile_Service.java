package com.automatisation.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class VhdFile_Service {

	private final Path root = Paths.get("uploads");

	public void init() {
		try {
			Files.createDirectory(root);
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize folder for upload!");
		}
	}

	public void save(MultipartFile file) {
		try {
			// System.out.println(file.getContentType());

			String fileName = file.getOriginalFilename();
			String prefix = fileName.substring(fileName.lastIndexOf("."));
			File file1 = null;
			try {

				file1 = File.createTempFile(fileName, prefix);
				file.transferTo(file1);
				System.out.println("eeeeeeeeeeeeeeeeeee " + file1);

				/* $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ */

				try {
					// File myObj = new
					// File("C:\\Users\\dell\\Downloads\\Test_Vhdl-2\\MAINV_18002.vhd");
					File myObj = file1;
					FileWriter myWriter = new FileWriter("output.txt");
					// Blank Document
					XWPFDocument document = new XWPFDocument();
					// Write the Document in file system
					FileOutputStream out = new FileOutputStream(new File("C:\\resultat.docx"));
					// créer un paragraphe
					XWPFParagraph paragraph = document.createParagraph();
					// créer l'objet run
					XWPFRun run = paragraph.createRun();
					XWPFRun run2 = paragraph.createRun();

					String str = "";
					String str2 = "";
					Scanner myReader = new Scanner(myObj);
					String MOT = "";
					String MOT2 = "";
					String title = "";
					String version = "";
					int i = 1;
					int j = 1;
					int k = 1;

					ArrayList<String> objectives = new ArrayList<String>();
					ArrayList<String> requiremnt = new ArrayList<String>();
					ArrayList<ArrayList<String>> steps = new ArrayList<ArrayList<String>>();
					boolean Vfound = false;
					while (myReader.hasNextLine()) {
						String data = myReader.nextLine();
						////// Extract Title Name //////
						if (data.contains("TITLE")) {
							String[] tokens = data.split(" ");
							title = tokens[12];
							str += title + "/" + "	The objectives below verify requirement ";
							System.out.println(title + "/:");
						}
						//////// Extract Version //////
						if (data.contains(" VERSION") && !Vfound) {
							String[] tokens = data.split(" ");
							version = tokens[8];
							str += title + "/" + version + "		The objectives below verify requirement ";
							run.setText(
									title + "/" + version + "	            The objectives below verify requirement ");
							System.out.println(title + "/" + version);
							Vfound = true;
						}
						//////// Extract Signal Name //////
						if (data.contains("The following procedure verifies the condition ")) {
							String[] tokens = data.split(" ");
							MOT = tokens[11];
							System.out.println("The following procedure verifies the condition" + " " + MOT);
						}
//						else if (data.contains("The following procedure verifies the ADC signal ")) {
//							String[] tokens = data.split(" ");
//							MOT2 = tokens[12];
//							System.out.println("The following procedure verifies the ADC signal" + " " + MOT2);
//						}
						//////// Extract Requirement //////
						if (data.contains("to the following requirements")) {
							data = myReader.nextLine();
							System.out.print("The objectives below verify requirement ");
							while (!data.contains("#######")) {
								System.out.println("--------------- " + data.substring(7, data.length()) + ",");
								requiremnt.add(data.substring(7, data.length()));
								data = myReader.nextLine();
							}
							for (String string : requiremnt) {
								str += string + ", ";
								run.setText(string + ",");
							}
							str += "\n";
							run.addBreak();
							str += "The following procedure verifies the condition " + MOT + ".\n";
							run.setText("\r\nThe following procedure verifies the condition " + MOT + ".\n");
							run.addBreak();
							run.addBreak();
							// str += "The following procedure verifies the the ADC signal " + MOT2 + ".\n";

						}
						///// Extract objectives///////
						if (data.contains("Objective ")) {
							data = myReader.nextLine();

							str += "\nObjectif " + (i) + ":\n";
							run.setText("\nObjectif " + (i) + ":\n");
							run.addBreak();
							i++;
							while (!data.contains("******")) {
								System.out.println("+++++++++++++++++ " + data);
								objectives.add(data);
								str += data + ".";
								run.setText(data + " ");
								run.addBreak();
								data = myReader.nextLine();

							}

							Scanner newreader = myReader;
							String newdata = myReader.nextLine();
							ArrayList<String> step = new ArrayList<String>();

							newdata = newreader.nextLine();

							while (newreader.hasNextLine()) {
								if (newdata.contains("Objective ")) {
									newdata = myReader.nextLine();

									str += "\nObjectif " + (i) + ":\n";
									run.setText("\nObjectif " + (i) + ":\n");
									run.addBreak();
									str2 += "\nObjectif " + ":\n";
									i++;
									while (!newdata.contains("******")) {
										System.out.println("+++++++++++++++++ " + newdata);
										objectives.add(newdata);
										str += newdata + ".";
										run.setText(newdata + " ");
										run.addBreak();
										newdata = myReader.nextLine();
									}

								}
								//////// Extract Steps //////////
								if (newdata.contains("STEP ")) {
									newdata = newreader.nextLine();
									newdata = newreader.nextLine();
									newdata = newreader.nextLine();
									str2 += "\nStep " + (j) + ":\n\n";
									run.setText("######## Step  " + (j) + " :######## ");
									j++;
									while (!newdata.isEmpty() && !newdata.contains("Final")) {
										// System.out.println("XXXXXX " + newdata.substring(4, newdata.length()));
										String value = getEquivalent(newdata);
										System.out.println("VALUE:*** " + value);
										step.add(newdata);
										run.addBreak();
										str2 += value + "\n";
										run.setText((k) + "-   " + value);
										k++;
										run.addBreak();
										newdata = newreader.nextLine();
									}
									newdata = newreader.nextLine();
									steps.add(step);
									System.out.println(newdata);
									str2 += newdata;
									run.setText(newdata);
									run.addBreak();
								}
								newdata = newreader.nextLine();
								System.out.println(newdata);
							}
							str += "\n\n";
							run.addBreak();

						}
					}
					document.write(out);
					document.close();
					out.close();

					myReader.close();
					myWriter.write(str);
					myWriter.write(str2);
					myWriter.close();
				} catch (FileNotFoundException e) {
					System.out.println("An error occurred.");
					e.printStackTrace();
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// After operating the above files, you need to delete the temporary files
				// generated in the root directory
				File f = new File(file1.toURI());
				f.delete();
			}
		} catch (Exception e) {
			System.out.println("dddddddddddddddddddddddd");

		}
	}

	public Resource load(String filename) {
		try {
			Path file = root.resolve(filename);
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("Could not read the file!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}

	public void deleteAll() {
		FileSystemUtils.deleteRecursively(root.toFile());
	}

	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
		} catch (IOException e) {
			throw new RuntimeException("Could not load the files!");
		}
	}

	public static String getEquivalent(String step) {
		System.out.println("steeeeeps:" + step);
		try {
			File file = new File("C:\\Users\\dell\\OneDrive\\Bureau\\some stufs\\FPGA_Dict.xlsx");
			// obtaining bytes from the file
			FileInputStream fis = new FileInputStream(file);
			// creating Workbook instance that refers to .xlsx file
			XSSFWorkbook wb = new XSSFWorkbook(fis);
			// creating a Sheet object to retrieve object
			XSSFSheet sheet = wb.getSheetAt(0);
			// iterating over excel file
			Iterator<Row> itr = sheet.iterator();

			FileWriter myWriter = new FileWriter("output.txt");
			String str3 = "";


		///////// La partie De La Conversion ///////////////

			while (itr.hasNext()) {
				Row row = itr.next();
				// iterating over each column
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					// power_on_reset
					Pattern pattern0 = Pattern.compile("power(.*?)reset");
					Matcher matcher0 = pattern0.matcher(step);

					if (matcher0.find()) {
						return "Power on the reset";
					}
					// activate_reset
					Pattern pattern1 = Pattern.compile("activate_reset(.*?)");
					Matcher matcher1 = pattern1.matcher(step);

					if (matcher1.find()) {
						return "Activate the reset";
					}

					// deactivate_reset
					Pattern pattern2 = Pattern.compile("deactivate_reset(.*?)");
					Matcher matcher2 = pattern2.matcher(step);

					if (matcher2.find()) {
						return "Deactivate the reset";
					}
					// SET_SIGNAL_NAME_FALSE"
					Pattern pattern3 = Pattern.compile("_(.*?)_F");
					Matcher matcher3 = pattern3.matcher(step);
					String motfound3 = "";
					if (matcher3.find()) {
						motfound3 = matcher3.group(1);
						return "SET " + motfound3 + " TO FALSE";
					}

					// SET_SIGNAL_NAME_TRUE"
					Pattern pattern4 = Pattern.compile("_(.*?)_T");
					Matcher matcher4 = pattern4.matcher(step);
					String motfound4 = "";

					if (matcher4.find()) {
						motfound4 = matcher4.group(1);
						return "SET " + motfound4 + " TO TRUE";
					}
					// SET_PDP_LOCATION_PDP(1&2)
					Pattern pattern5 = Pattern.compile("SET_PDP_LOCATION[(](.*?),");
					Matcher matcher5 = pattern5.matcher(step);
					String motfound5 = "";

					if (matcher5.find()) {
						motfound5 = matcher5.group(1);
						return "SET the PDP LOCATION to " + motfound5;
					}

					// check_output
					Pattern pattern6 = Pattern.compile("check_output.*spy.(.*?),.*'(.*?)', s_count");
					Matcher matcher6 = pattern6.matcher(step);
					String motfound6 = "";
					// check_output with DURING
					Pattern pattern7 = Pattern.compile("check_output.*spy.(.*?),.*'(.*?)',(.*?),(.*?),");
					Matcher matcher7 = pattern7.matcher(step);
					String motfound7 = "";

					if (matcher6.find()) {
						motfound6 = matcher6.group(1);
						return "Verify by spy " + motfound6 + " is equal to " + matcher6.group(2);
					}

					else if (matcher7.find()) {
						motfound7 = matcher7.group(1);
						return "Verify by spy " + motfound7 + " is equal to " + matcher7.group(2) + ""
								+ matcher7.group(3) + "" + matcher7.group(4);
					}
					// check_input
					Pattern pattern8 = Pattern.compile("check_input.*spy.(.*?),.*'(.*?)',");
					Matcher matcher8 = pattern8.matcher(step);
					String motfound8 = "";

					if (matcher8.find()) {
						motfound8 = matcher8.group(1);
						return "Verify by spy " + motfound8 + " is equal to " + matcher8.group(2);
					}

					// check_arinc
					Pattern pattern9 = Pattern.compile("check_arinc.*[(](.*?),.*'(.*?)'");
					Matcher matcher9 = pattern9.matcher(step);
					String motfound9 = "";

					if (matcher9.find()) {
						motfound9 = matcher9.group(1);
						return "Verify through ARINC link that " + motfound9 + " is equal to " + matcher9.group(2);
					}
					// check_timing
					Pattern pattern10 = Pattern.compile("check_timing.*spy.(.*?),.*'(.*?)',(.*?),");
					Matcher matcher10 = pattern10.matcher(step);
					String motfound10 = "";

					if (matcher10.find()) {
						motfound10 = matcher10.group(1);
						return "Verify by spy " + motfound10 + " is equal to " + matcher10.group(2) + " in less than "
								+ matcher10.group(3).trim();
					}

				}

			}

			wb.close();

			System.out.println("");
			System.out.println();

		} catch (Exception e) {
			e.printStackTrace();

		}
		return " ";
	}

}
