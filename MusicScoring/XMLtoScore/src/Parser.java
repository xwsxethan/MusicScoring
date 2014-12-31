import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class Parser {
	private static final String SCORE_NODE = "score-partwise";
	private static final String PART_NODE = "part";
	private static final int SCORE_REQUIRED = 2;
	
	private static final String MEASURE_NODE = "measure";
	private static final String NOTE_NODE = "note";

	private static final String PITCH_NODE = "pitch";
	private static final String REST_NODE = "rest";
	private static final String DURATION_NODE = "duration";

	private static final String STEP_NODE = "step";
	private static final String OCTAVE_NODE = "octave";
	private static final String ALTER_NODE = "alter";
	
	private static final int NOTES_IN_OCTAVE = 12;

	private File xmlToParse;
	private NodeList measures;
	private int measureCount;
	private int realMeasures;
	private int currentMeasure;
	private int noteCount;
	private int lowNote;
	private int highNote;
	private int lowDuration;
	private int highDuration;
	private int totalDuration;
	
	public Parser(File xmlFile) {
		measures = null;
		measureCount = 0;
		realMeasures = 0;
		currentMeasure = 0;
		noteCount = 0;
		lowNote = Integer.MAX_VALUE;
		highNote = 0;
		lowDuration = Integer.MAX_VALUE;
		highDuration = 0;
		totalDuration = 0;
		
		start(xmlFile);
	}
	
	public void setXmlFile(File xmlFile) {
		xmlToParse = xmlFile;
	}

	public void start(File xmlFile) {
		setXmlFile(xmlFile);
		start();
	}
	
	public void start() {
		DocumentBuilder builder;
		Document toRead;
		NodeList list;
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			toRead = builder.parse(xmlToParse);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			System.out.println("ERROR: Couldn't open the xml file to read. Possibly an incorrect name.");
			e.printStackTrace();
			return;
		}
		
		list = toRead.getChildNodes();		
		Node elem = null;
		int scoreCount = 0;
		
		for (int i = 0; i < list.getLength(); i++) {
			if (scoreCount == SCORE_REQUIRED) {
				break;
			}
			elem = list.item(i);
			String name = elem.getNodeName().trim();
			if (name.equalsIgnoreCase(SCORE_NODE)) {
				scoreCount++;
			}
		}
		
		if (elem == null || scoreCount != SCORE_REQUIRED) {
			System.out.println("ERROR: Couldn't find the score in the xml.");
			return;
		}
		list = elem.getChildNodes();
		elem = null;
		
		for (int i = 0; i < list.getLength(); i++) {
			elem = list.item(i);
			String name = elem.getNodeName().trim();
			if (name.equalsIgnoreCase(PART_NODE)) {
				break;
			}
		}
		
		if (elem == null || !elem.getNodeName().trim().equalsIgnoreCase(PART_NODE)) {
			System.out.println("ERROR: Couldn't find the first part in the xml.");
			return;
		}
		measures = elem.getChildNodes();
		measureCount = measures.getLength();
		realMeasures = measures.getLength();
		return;
	}
	
	public boolean hasMeasure() {
		return measures != null && currentMeasure < measureCount;
	}
	
	public void parseMeasure() {
		if (!hasMeasure()) {
			System.out.println("ERROR: No measures to parse in the xml.");
			return;
		}
		
		Node elem = measures.item(currentMeasure);
		if (elem == null) {
			System.out.println("ERROR: A node turned up null for some reason. Stopping.");
			measures = null;
			return;
		}
		
		if (!elem.getNodeName().trim().equalsIgnoreCase(MEASURE_NODE)) {
			//System.out.println("ERROR: A random non-measure node turned up. Skipping it.");
			realMeasures--;
			currentMeasure++;
			return;
		}
		
		NodeList list = elem.getChildNodes();
		
		//System.out.println("Current measure: " + currentMeasure);
		
		for (int i = 0; i < list.getLength(); i++) {
			elem = list.item(i);
			String name = elem.getNodeName().trim();
			if (name.equalsIgnoreCase(NOTE_NODE)) {
				noteCount++;
				parseNote(elem);
			}
		}
		
		currentMeasure++;
		
		return;
	}
	
	private void parseNote(Node elem) {
		boolean foundNote = false;
		String noteName = "";
		String octave = "";
		String alter = "";
		NodeList noteVals = elem.getChildNodes();
		for (int j = 0; j < noteVals.getLength(); j++) {
			Node noteVal = noteVals.item(j);
			String nodeNameForComparison = noteVal.getNodeName().trim();
			if (nodeNameForComparison.equalsIgnoreCase(PITCH_NODE)) {
				foundNote = true;
				NodeList pitch = noteVal.getChildNodes();
				for (int k = 0; k < pitch.getLength(); k++) {
					Node pitchPart = pitch.item(k);
					if (pitchPart.getNodeName().trim().equalsIgnoreCase(STEP_NODE)) {
						noteName = pitchPart.getTextContent().trim();
					}
					else if (pitchPart.getNodeName().trim().equalsIgnoreCase(OCTAVE_NODE)) {
						octave = pitchPart.getTextContent().trim();									
					}
					else if (pitchPart.getNodeName().trim().equalsIgnoreCase(ALTER_NODE)) {
						alter = pitchPart.getTextContent().trim();	
					}
				}
			}
			else if (nodeNameForComparison.equalsIgnoreCase(REST_NODE)) {
				return;
			}
			else if (nodeNameForComparison.equalsIgnoreCase(DURATION_NODE)) {
				try {
					int dur = Integer.parseInt(noteVal.getTextContent().trim());
					if (dur < lowDuration) {
						if (Main.LOGGING) {
							System.out.println("Low duration updated to " + dur + " in measure "
								+ (currentMeasure - (measureCount - realMeasures) + 1));
						}
						lowDuration = dur;
					}
					if (dur > highDuration) {
						if (Main.LOGGING) {
							System.out.println("High duration updated to " + dur + " in measure "
								+ (currentMeasure - (measureCount - realMeasures) + 1));
						}
						highDuration = dur;
					}
					totalDuration += dur;
				} catch (NumberFormatException e) {
					continue;
				}
			}
		}
		
		if (foundNote) {
			int noteNum = noteToNum(noteName, octave, alter);
			if (noteNum < lowNote) {
				if (Main.LOGGING) {
					System.out.println("Low note updated to " + noteNum + " in measure "
						+ (currentMeasure - (measureCount - realMeasures) + 1));
				}
				lowNote = noteNum;
			}
			if (noteNum > highNote) {
				if (Main.LOGGING) {
					System.out.println("High note updated to " + noteNum + " in measure "
						+ (currentMeasure - (measureCount - realMeasures) + 1));
				}
				highNote = noteNum;
			}
		}
	}
	
	private int noteToNum(String noteName, String octave, String alter) {
		int base = 0;
		if (octave != null && !octave.isEmpty()) {
			try {
				base = NOTES_IN_OCTAVE*Integer.parseInt(octave);
			} catch (NumberFormatException e) {
				System.out.println("ERROR: Note octave not formatted correctly.");
				base = 0;
			}
		}
		
		base = base + noteLetterToNum(noteName);

		if (alter != null && !alter.isEmpty()) {
			try {
				base = base + Integer.parseInt(alter);
			} catch (NumberFormatException e) {
				System.out.println("ERROR: Note alter not formatted correctly.");
			}
		}
		
		return base;
	}
	
	private int noteLetterToNum(String noteLetter) {
		if (noteLetter == null || noteLetter.length() != 1) {
			return 0;
		}
		switch (noteLetter.toUpperCase()) {
		case "C":
			return 0;
		case "D":
			return 2;
		case "E":
			return 4;
		case "F":
			return 5;
		case "G":
			return 7;
		case "A":
			return 9;
		case "B":
			return 11;
		default:
			return 0;
		}
	}
	
	private String numToNote(int note) {
		int letter = note % NOTES_IN_OCTAVE;
		int octave = note / NOTES_IN_OCTAVE;
		String toReturn = "";
		switch (letter) {
		case 0:
			toReturn = "C";
			break;
		case 1:
			toReturn = "C#(Db)";
			break;
		case 2:
			toReturn = "D";
			break;
		case 3:
			toReturn = "D#(Eb)";
			break;
		case 4:
			toReturn = "E";
			break;
		case 5:
			toReturn = "F";
			break;
		case 6:
			toReturn = "F#(Gb)";
			break;
		case 7:
			toReturn = "G";
			break;
		case 8:
			toReturn = "G#(Ab)";
			break;
		case 9:
			toReturn = "A";
			break;
		case 10:
			toReturn = "A#(Bb)";
			break;
		case 11:
			toReturn = "B";
			break;
		default:
			toReturn = "C";
			break;
		}
		
		toReturn = toReturn + octave;
		return toReturn;
	}

	public void statusReport() {
		System.out.println("Total real measures: " + realMeasures + "\tTotal notes: " + noteCount);
		System.out.println("Range: " + (highNote - lowNote) + " chromatic steps");
		System.out.println("Total note duration: " + totalDuration + "\tAverage note duration: " + (totalDuration / noteCount));
		if (Main.LOGGING) {
			System.out.println("Total objects: " + measureCount);
			System.out.println("High Note: " + highNote + " or " + numToNote(highNote));
			System.out.println("Low Note: " + lowNote + " or " + numToNote(lowNote));
			System.out.println("High Duration: " + highDuration);
			System.out.println("Low Duration: " + lowDuration);
		}
	}
}
