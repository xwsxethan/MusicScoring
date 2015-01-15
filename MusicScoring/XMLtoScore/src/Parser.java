import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import DifficultyLevels.DifficultyLevel;
import MusicalElements.Dynamic;
import Utilities.Utils;


public class Parser {
	private static final String SCORE_NODE = "score-partwise";
	private static final String PART_NODE = "part";
	private static final int SCORE_REQUIRED = 2;
	
	private static final String MEASURE_NODE = "measure";
	private static final String NOTE_NODE = "note";
	private static final String ATTRIBUTES_NODE = "attributes";
	private static final String DIRECTION_NODE = "direction";
	
	private static final String DIRECTION_TYPE_NODE = "direction-type";
	private static final String DYNAMICS_NODE = "dynamics";
	
	private static final String KEY_NODE = "key";
	private static final String FIFTHS_NODE = "fifths";
	
	private static final String TIME_NODE = "time";
	private static final String BEAT_TYPE_NODE = "beat-type";
	
	private static final String SOUND_NODE = "sound";
	private static final String TEMPO_NODE = "tempo";
	
	private static final String PITCH_NODE = "pitch";
	private static final String REST_NODE = "rest";
	//private static final String DURATION_NODE = "duration";
	private static final String TYPE_NODE = "type";
	private static final String DOTTED_NODE = "dot";
	private static final String TIED_NODE = "tie";
	private static final String START_NODE = "start";
	private static final String STOP_NODE = "stop";
	
	private static final String STEP_NODE = "step";
	private static final String OCTAVE_NODE = "octave";
	private static final String ALTER_NODE = "alter";
	
	private static final int INITIAL_LAST_NOTE_VALUE = -1;
	
	private File xmlToParse;
	private NodeList measures;
	private int measureCount;
	private int realMeasures;
	private int currentMeasure;
	
	private int noteCount;
	private int lowNote;
	private int highNote;
	
	private double lowDuration;
	private double highDuration;
	private double totalDuration;
	
	private int lastNote;
	private int lowInterval;
	private int highInterval;
	private int totalInterval;
	
	private int totalNoteChanges;
	private int currentKey;
	private int keyChanges;
	
	private int beatsPerMeasure;
	private int timeChanges;
	
	private int tempo;
	private int tempoChanges;
	
	private Dynamic dynamics;
	private int dynamicChanges;
	
	private double currentScore;
	private DifficultyReader diffs;
	
	private boolean tied;
	private double tieDuration;

	public Parser(File xmlFile, DifficultyLevel difficulty) {
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
		
		lastNote = INITIAL_LAST_NOTE_VALUE;
		lowInterval = Integer.MAX_VALUE;
		highInterval = 0;
		totalInterval = 0;
		
		totalNoteChanges = 0;
		currentKey = Integer.MAX_VALUE;
		keyChanges = 0;
		
		beatsPerMeasure = Integer.MAX_VALUE;
		timeChanges = 0;
		
		tempo = 0;
		tempoChanges = 0;
		
		dynamics = null;
		dynamicChanges = 0;
		
		currentScore = 0;

		diffs = new DifficultyReader(difficulty.getXMLFile());
		
		tied = false;
		tieDuration = 0;
		
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
		
		for (int i = 0; i < list.getLength(); i++) {
			elem = list.item(i);
			String name = elem.getNodeName().trim();
			if (name.equalsIgnoreCase(NOTE_NODE)) {
				noteCount++;
				parseNote(elem);
			}
			else if (name.equalsIgnoreCase(ATTRIBUTES_NODE)) {
				parseAttributes(elem);
			}
			else if (name.equalsIgnoreCase(DIRECTION_NODE)) {
				parseDirection(elem);
			}
		}
		
		currentMeasure++;
		
		return;
	}
	
	private void parseAttributes(Node elem) {
		NodeList attributes = elem.getChildNodes();
		for (int j = 0; j < attributes.getLength(); j++) {
			Node attribute = attributes.item(j);
			String nodeNameForComparison = attribute.getNodeName().trim();
			if (nodeNameForComparison.equalsIgnoreCase(KEY_NODE)) {
				NodeList keyStuff = attribute.getChildNodes();
				for (int k = 0; k < keyStuff.getLength(); k++) {
					Node keyElem = keyStuff.item(k);
					String keyElemName = keyElem.getNodeName().trim();
					if (keyElemName.equalsIgnoreCase(FIFTHS_NODE)) {
						try {
							int key = Integer.parseInt(keyElem.getTextContent().trim());
							//Not sure what else to do with the key yet...
							if (key != currentKey) {
								keyChanges++;
								if (Main.LOGGING) {
									System.out.println("Old Key: " + currentKey + "\tNew Key: " + key);
								}
								currentKey = key;
							}
						} catch (NumberFormatException e) {
							continue;
						}
					}
				}				
			}
			else if (nodeNameForComparison.equalsIgnoreCase(TIME_NODE)) {
				NodeList timeStuff = attribute.getChildNodes();
				for (int k = 0; k < timeStuff.getLength(); k++) {
					Node timeElem = timeStuff.item(k);
					String timeElemName = timeElem.getNodeName().trim();
					if (timeElemName.equalsIgnoreCase(BEAT_TYPE_NODE)) {
						try {
							int time = Integer.parseInt(timeElem.getTextContent().trim());
							//Not sure what else to do with the time yet...
							if (time != beatsPerMeasure) {
								timeChanges++;
								if (Main.LOGGING) {
									System.out.println("Old beats per measure: " + beatsPerMeasure + "\tNew beats per measure: " + time);
								}
								beatsPerMeasure = time;
							}
						} catch (NumberFormatException e) {
							continue;
						}
					}
				}				
			}
		}
	}
	
	private void parseDirection(Node elem) {
		NodeList directions = elem.getChildNodes();
		for (int j = 0; j < directions.getLength(); j++) {
			Node direction = directions.item(j);
			String nodeNameForComparison = direction.getNodeName().trim();
			if (nodeNameForComparison.equalsIgnoreCase(SOUND_NODE)) {
				if (direction.hasAttributes()) {
					Node sound = direction.getAttributes().item(0);
					if (sound.getNodeName().trim().equalsIgnoreCase(TEMPO_NODE)) {
						try {
							int newTempo = Integer.parseInt(sound.getNodeValue());
							if (tempo != newTempo) {
								tempoChanges++;
								if (Main.LOGGING) {
									System.out.println("Old Tempo: " + tempo + "\tNew Tempo: " + newTempo);
								}
								tempo = newTempo;
							}
						} catch (NumberFormatException e) {
							continue;
						}
					}
					/*else if (sound.getNodeName().trim().equalsIgnoreCase(DYNAMICS_NODE)) {
						try {
							int newDynamic = Integer.parseInt(sound.getNodeValue());
							if (dynamic != newDynamic) {
								dynamicChanges++;
								if (Main.LOGGING) {
									System.out.println("Old Dynamic: " + dynamic + "\tNew Dynamic: " + newDynamic);
								}
								dynamic = newDynamic;
							}
						} catch (NumberFormatException e) {
							continue;
						}
					}*/
				}			
			}
			else if (nodeNameForComparison.equalsIgnoreCase(DIRECTION_TYPE_NODE)) {
				NodeList directionStuff = direction.getChildNodes();
				for (int k = 0; k < directionStuff.getLength(); k++) {
					Node dynamElem = directionStuff.item(k);
					String dynamElemName = dynamElem.getNodeName().trim();
					if (dynamElemName.equalsIgnoreCase(DYNAMICS_NODE)) {
						NodeList dynamElems = dynamElem.getChildNodes();
						for (int f = 0; f < dynamElems.getLength(); f++) {
							Dynamic dynam = Utils.stringToDynamic(dynamElems.item(f).getNodeName());
							if (dynam == Dynamic.ERROR) {
								continue;
							}
							if (dynamics != dynam) {
								dynamicChanges++;
								if (Main.LOGGING) {
									System.out.println("Old Dynamics: " + dynamics + "\tNew Dynamics: " + dynam);
								}
								dynamics = dynam;
							}
							break;
						}
					}
				}			
			}
		}
	}
	
	private void parseNote(Node elem) {
		boolean foundNote = false;
		boolean dotted = false;
		boolean justUntied = false;
		Double dur = null;
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
			//else if (nodeNameForComparison.equalsIgnoreCase(DURATION_NODE)) {
			else if (nodeNameForComparison.equalsIgnoreCase(TYPE_NODE)) {
				String noteType = noteVal.getTextContent().trim();
				dur = Utils.typeAndTempoToDuration(noteType, tempo, beatsPerMeasure);
			}
			else if (nodeNameForComparison.equalsIgnoreCase(DOTTED_NODE)) {
				dotted = true;
			}
			else if (nodeNameForComparison.equalsIgnoreCase(TIED_NODE)) {
				if (noteVal.hasAttributes()) {
					Node startStop = noteVal.getAttributes().item(0);
					if (startStop.getNodeName().trim().equalsIgnoreCase(TYPE_NODE)) {
						switch (startStop.getNodeValue()) {
						case (START_NODE) :
							tied = true;
							break;
						case (STOP_NODE) :
							tied = false;
							justUntied = true;
							break;
						}
					}
				}
			}
		}
		
		if (foundNote) {
			if (dur != null) {
				if (dotted) {
					dur = 1.5 * dur;
				}
				
				if (!tied) {
					if (justUntied) {
						tieDuration += dur;
						dur = tieDuration;
					}
					
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
				}
				else {
					tieDuration += dur;
				}
			}
			
			if (!tied) {
				updateNoteTracking(Utils.noteToNum(noteName, octave, alter));
			}
		}
	}
	
	private void updateNoteTracking(int noteNum) {
		totalNoteChanges = totalNoteChanges + (Utils.getAlteration() != 0 ? 1 : 0);
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
		
		double dynamicMult = diffs.getDynamicDifficulty(dynamics);
		double keyMult = diffs.getKeySignatureDifficulty(currentKey);
		
		double noteTotal = diffs.getNoteDifficulty(noteNum) * dynamicMult * keyMult;
		currentScore += noteTotal;
		
		if (Main.LOGGING) {
			System.out.println("Current Score: " + currentScore + "\tNote total: " + noteTotal);
		}
		
		if (lastNote == INITIAL_LAST_NOTE_VALUE) {
			lastNote = noteNum;
		}
		else {
			int interval = Math.abs(noteNum - lastNote);
			if (interval < lowInterval) {
				if (Main.LOGGING) {
					System.out.println("Low interval updated to " + interval + " in measure "
						+ (currentMeasure - (measureCount - realMeasures) + 1));
				}
				lowInterval = interval;
			}
			if (interval > highInterval) {
				if (Main.LOGGING) {
					System.out.println("High interval updated to " + interval + " in measure "
						+ (currentMeasure - (measureCount - realMeasures) + 1));
				}
				highInterval = interval;
			}
			totalInterval += interval;

			double intervalTotal = diffs.getIntervalDifficulty(lastNote, noteNum, currentKey) * dynamicMult * keyMult;
			currentScore += intervalTotal;
			
			if (Main.LOGGING) {
				System.out.println("Current Score: " + currentScore + "\tInterval total: " + intervalTotal);
			}
			
			lastNote = noteNum;
		}		
	}
	
	public double getScore() {
		return currentScore * diffs.getTempoDifficulty(totalDuration, noteCount);		
	}
	
	public void statusReport() {
		System.out.println("Current score: " + currentScore);
		System.out.println("Total measures: " + realMeasures);
		System.out.println("Total notes: " + noteCount);
		System.out.println("Range: " + (highNote - lowNote) + " chromatic steps");
		System.out.println("Average Note Duration: " + (totalDuration / noteCount) + " seconds (I think)");
		System.out.println("Average Interval: " + (totalInterval / (noteCount - 1)) + " chromatic steps");
		System.out.println("Total altered notes (key or accidental): " + totalNoteChanges);
		System.out.println("Total key changes: " + ((keyChanges - 1) == -1 ? 0 : (keyChanges - 1)));
		System.out.println("Total beats per measure changes: " + ((timeChanges - 1) == -1 ? 0 : (timeChanges - 1)));
		System.out.println("Total tempo changes: " + ((tempoChanges - 1) == -1 ? 0 : (tempoChanges - 1)));
		System.out.println("Total dynamic changes: " + ((dynamicChanges - 1) == -1 ? 0 : (dynamicChanges - 1)));
		if (Main.LOGGING) {
			System.out.println("Total objects: " + measureCount);
			System.out.println("High Note: " + highNote + " or " + Utils.numToNote(highNote));
			System.out.println("Low Note: " + lowNote + " or " + Utils.numToNote(lowNote));
			System.out.println("Total Note Duration: " + totalDuration + " seconds (I think)");
			System.out.println("High Duration: " + highDuration + " seconds (I think)");
			System.out.println("Low Duration: " + lowDuration + " seconds (I think)");
			System.out.println("Total Interval: " + totalInterval + " chromatic steps");
			System.out.println("High Interval: " + highInterval + " chromatic steps");
			System.out.println("Low Interval: " + lowInterval + " chromatic steps");
			System.out.println("Current Key: " + currentKey);
			System.out.println("Current beats per measure: " + beatsPerMeasure);
			System.out.println("Current tempo: " + tempo);
			System.out.println("Current dynamics: " + dynamics.name());
		}
	}
}
