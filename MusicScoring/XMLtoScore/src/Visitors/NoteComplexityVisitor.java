package Visitors;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import DifficultyLevels.DifficultyLevel;
import Main.DifficultyReader;
import Main.Main;
import MusicalElements.Articulation;
import MusicalElements.Dynamic;
import Nodes.*;
import Utilities.*;

public class NoteComplexityVisitor implements IElementVisitor {
	
	private static final int SCORE_REQUIRED = 2;
	
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
	
	private Articulation oldArticulations;
	private Articulation articulations;
	private int articulationChanges;
	
	private double currentScore;
	private DifficultyReader diffs;
	
	private boolean tied;
	private double tieDuration;
	
	//private IElementVisitor parser;

 	public NoteComplexityVisitor(File xmlFile, DifficultyLevel difficulty) {
		initializeValues(difficulty);
		NodeList topNodes = setupXmlFile(xmlFile);
		if (topNodes != null) {
			start(topNodes);
		}
	}
	
	public void initializeValues(DifficultyLevel difficulty) {
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
		
		oldArticulations = null;
		articulations = null;
		articulationChanges = 0;
		
		currentScore = 0;

		diffs = new DifficultyReader(difficulty);
		
		tied = false;
		tieDuration = 0;
		
		//parser = new ParserElementVisitor();
	}
	
	public NodeList setupXmlFile(File xmlFile) {
		xmlToParse = xmlFile;
		DocumentBuilder builder;
		Document toRead;
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			toRead = builder.parse(xmlToParse);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			System.out.println("ERROR: Couldn't open the xml file to read. Possibly an incorrect name.");
			e.printStackTrace();
			return null;
		}		
		
		return toRead.getChildNodes();
	}
	
	private void start(NodeList list) {	
		Node elem = null;
		int scoreCount = 0;
		
		for (int i = 0; i < list.getLength(); i++) {
			if (scoreCount == SCORE_REQUIRED) {
				break;
			}
			elem = list.item(i);
			String name = elem.getNodeName().trim();
			if (name.equalsIgnoreCase(Constants.SCORE_NODE)) {
				scoreCount++;
			}
		}
		
		if (elem == null || scoreCount != SCORE_REQUIRED) {
			System.out.println("ERROR: Couldn't find the score in the xml.");
			return;
		}
		
		
		Score score = new Score(elem);
		score.accept(this);
	}
			
	@Override
	public void visit(Score score) {
		NodeList list = score.getBase().getChildNodes();
		Node elem = null;
		
		for (int i = 0; i < list.getLength(); i++) {
			elem = list.item(i);
			String name = elem.getNodeName().trim();
			if (name.equalsIgnoreCase(Constants.PART_NODE)) {
				break;
			}
		}
		
		if (elem == null || !elem.getNodeName().trim().equalsIgnoreCase(Constants.PART_NODE)) {
			System.out.println("ERROR: Couldn't find the first part in the xml.");
			return;
		}
		
		Part part = new Part(elem);
		part.accept(this);
	}

	@Override
	public void visit(Part part) {
		measures = part.getBase().getChildNodes();
		measureCount = measures.getLength();
		realMeasures = measures.getLength();
		
		if (!(measures != null && currentMeasure < measureCount)) {
			System.out.println("ERROR: No measures to parse in the xml.");
			return;
		}
		
		while (measures != null && currentMeasure < measureCount) {
			Measure measure = new Measure(measures.item(currentMeasure));
			measure.accept(this);
		}
		
		return;
	}

	@Override
	public void visit(Measure measure) {
		Node elem = measure.getBase();
		
		if (elem == null) {
			System.out.println("ERROR: A node turned up null for some reason. Stopping.");
			measures = null;
			return;
		}
		
		if (!elem.getNodeName().trim().equalsIgnoreCase(Constants.MEASURE_NODE)) {
			//System.out.println("ERROR: A random non-measure node turned up. Skipping it.");
			realMeasures--;
			currentMeasure++;
			return;
		}
		
		NodeList list = elem.getChildNodes();
		
		for (int i = 0; i < list.getLength(); i++) {
			elem = list.item(i);
			String name = elem.getNodeName().trim();
			if (name.equalsIgnoreCase(Constants.SOUND_NODE)) {
				Sound sound = new Sound(elem);
				sound.accept(this);
			}
			else if (name.equalsIgnoreCase(Constants.DIRECTION_TYPE_NODE)) {
				DirectionType directionType = new DirectionType(elem);
				directionType.accept(this);
			}
			else if (name.equalsIgnoreCase(Constants.ATTRIBUTES_NODE)) {
				new Attribute(elem).accept(this);
			}
			else if (name.equalsIgnoreCase(Constants.DIRECTION_NODE)) {
				new Direction(elem).accept(this);
			}
			else if (name.equalsIgnoreCase(Constants.NOTE_NODE)) {
				noteCount++;
				new Note(elem).accept(this);
			}
		}
		
		currentMeasure++;

	}

	@Override
	public void visit(Note note) {
		oldArticulations = articulations;
		articulations = Articulation.Normal;
		boolean foundNote = false;
		boolean dotted = false;
		boolean justUntied = false;
		Double dur = null;
		String noteName = "";
		String octave = "";
		String alter = "";
		NodeList noteVals = note.getBase().getChildNodes();
		for (int j = 0; j < noteVals.getLength(); j++) {
			Node noteVal = noteVals.item(j);
			String nodeNameForComparison = noteVal.getNodeName().trim();
			if (nodeNameForComparison.equalsIgnoreCase(Constants.PITCH_NODE)) {
				foundNote = true;
				NodeList pitch = noteVal.getChildNodes();
				for (int k = 0; k < pitch.getLength(); k++) {
					Node pitchPart = pitch.item(k);
					if (pitchPart.getNodeName().trim().equalsIgnoreCase(Constants.STEP_NODE)) {
						noteName = pitchPart.getTextContent().trim();
					}
					else if (pitchPart.getNodeName().trim().equalsIgnoreCase(Constants.OCTAVE_NODE)) {
						octave = pitchPart.getTextContent().trim();									
					}
					else if (pitchPart.getNodeName().trim().equalsIgnoreCase(Constants.ALTER_NODE)) {
						alter = pitchPart.getTextContent().trim();	
					}
				}
			}
			else if (nodeNameForComparison.equalsIgnoreCase(Constants.REST_NODE)) {
				return;
			}
			//else if (nodeNameForComparison.equalsIgnoreCase(DURATION_NODE)) {
			else if (nodeNameForComparison.equalsIgnoreCase(Constants.TYPE_NODE)) {
				String noteType = noteVal.getTextContent().trim();
				dur = Utils.typeAndTempoToDuration(noteType, tempo, beatsPerMeasure);
			}
			else if (nodeNameForComparison.equalsIgnoreCase(Constants.DOTTED_NODE)) {
				dotted = true;
			}
			else if (nodeNameForComparison.equalsIgnoreCase(Constants.TIED_NODE)) {
				if (noteVal.hasAttributes()) {
					Node startStop = noteVal.getAttributes().item(0);
					if (startStop.getNodeName().trim().equalsIgnoreCase(Constants.TYPE_NODE)) {
						switch (startStop.getNodeValue()) {
						case (Constants.START_NODE) :
							tied = true;
							break;
						case (Constants.STOP_NODE) :
							tied = false;
							justUntied = true;
							break;
						}
					}
				}
			}
			else if (nodeNameForComparison.equalsIgnoreCase(Constants.NOTATIONS_NODE)) {
				Notations notations = new Notations(noteVal);
				notations.accept(this);
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
			
			updateArticulations();
		}
	}

	@Override
	public void visit(Attribute attribute) {
		NodeList attributes = attribute.getBase().getChildNodes();
		for (int j = 0; j < attributes.getLength(); j++) {
			Node elem = attributes.item(j);
			String nodeNameForComparison = elem.getNodeName().trim();
			if (nodeNameForComparison.equalsIgnoreCase(Constants.KEY_NODE)) {
				Key key = new Key(elem);
				key.accept(this);				
			}
			else if (nodeNameForComparison.equalsIgnoreCase(Constants.TIME_NODE)) {
				Time time = new Time(elem);
				time.accept(this);
			}
		}

	}

	@Override
	public void visit(Direction direction) {
		NodeList directions = direction.getBase().getChildNodes();
		for (int j = 0; j < directions.getLength(); j++) {
			Node directionElem = directions.item(j);
			String nodeNameForComparison = directionElem.getNodeName().trim();
			if (nodeNameForComparison.equalsIgnoreCase(Constants.SOUND_NODE)) {
				Sound sound = new Sound(directionElem);
				sound.accept(this);
			}
			else if (nodeNameForComparison.equalsIgnoreCase(Constants.DIRECTION_TYPE_NODE)) {
				DirectionType directionType = new DirectionType(directionElem);
				directionType.accept(this);
			}
		}
	}

	@Override
	public void visit(DirectionType directionType) {
		NodeList directionStuff = directionType.getBase().getChildNodes();
		for (int k = 0; k < directionStuff.getLength(); k++) {
			Node dynamElem = directionStuff.item(k);
			String dynamElemName = dynamElem.getNodeName().trim();
			if (dynamElemName.equalsIgnoreCase(Constants.DYNAMICS_NODE)) {
				Dynamics dynamics = new Dynamics(dynamElem);
				dynamics.accept(this);
			}
		}	
	}

	@Override
	public void visit(Dynamics dynamics) {
		NodeList dynamElems = dynamics.getBase().getChildNodes();
		for (int f = 0; f < dynamElems.getLength(); f++) {
			Dynamic dynam = Utils.stringToDynamic(dynamElems.item(f).getNodeName());
			if (dynam == Dynamic.ERROR) {
				continue;
			}
			if (this.dynamics != dynam) {
				dynamicChanges++;
				if (Main.LOGGING) {
					System.out.println("Old Dynamics: " + this.dynamics + "\tNew Dynamics: " + dynam);
				}
				this.dynamics = dynam;
			}
			break;
		}
	}

	@Override
	public void visit(Key key) {
		NodeList keyStuff = key.getBase().getChildNodes();
		for (int k = 0; k < keyStuff.getLength(); k++) {
			Node keyElem = keyStuff.item(k);
			String keyElemName = keyElem.getNodeName().trim();
			if (keyElemName.equalsIgnoreCase(Constants.FIFTHS_NODE)) {
				Fifths fifths = new Fifths(keyElem);
				fifths.accept(this);
			}
		}
	}

	@Override
	public void visit(Fifths fifths) {
		try {
			int keyNum = Integer.parseInt(fifths.getBase().getTextContent().trim());
			//Not sure what else to do with the key yet...
			if (keyNum != currentKey) {
				keyChanges++;
				if (Main.LOGGING) {
					System.out.println("Old Key: " + currentKey + "\tNew Key: " + keyNum);
				}
				currentKey = keyNum;
			}
		} catch (NumberFormatException e) {
			return;
		}

	}

	@Override
	public void visit(Time time) {
		NodeList timeStuff = time.getBase().getChildNodes();
		for (int k = 0; k < timeStuff.getLength(); k++) {
			Node timeElem = timeStuff.item(k);
			String timeElemName = timeElem.getNodeName().trim();
			if (timeElemName.equalsIgnoreCase(Constants.BEAT_TYPE_NODE)) {
				BeatType beatType = new BeatType(timeElem);
				beatType.accept(this);
			}
		}
	}

	@Override
	public void visit(BeatType beatType) {
		try {
			int timeNum = Integer.parseInt(beatType.getBase().getTextContent().trim());
			//Not sure what else to do with the time yet...
			if (timeNum != beatsPerMeasure) {
				timeChanges++;
				if (Main.LOGGING) {
					System.out.println("Old beats per measure: " + beatsPerMeasure + "\tNew beats per measure: " + timeNum);
				}
				beatsPerMeasure = timeNum;
			}
		} catch (NumberFormatException e) {
			return;
		}

	}

	@Override
	public void visit(Sound sound) {
		if (sound.getBase().hasAttributes()) {
			Node soundElem = sound.getBase().getAttributes().item(0);
			if (soundElem.getNodeName().trim().equalsIgnoreCase(Constants.TEMPO_NODE)) {
				Tempo tempo = new Tempo(soundElem);
				tempo.accept(this);
			}
		}	
	}

	@Override
	public void visit(Tempo tempo) {
		try {
			int newTempo = Integer.parseInt(tempo.getBase().getNodeValue());
			if (this.tempo != newTempo) {
				tempoChanges++;
				if (Main.LOGGING) {
					System.out.println("Old Tempo: " + this.tempo + "\tNew Tempo: " + newTempo);
				}
				this.tempo = newTempo;
			}
		} catch (NumberFormatException e) {
			return;
		}
	}

	@Override
	public void visit(Pitch pitch) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Rest rest) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Notations notations) {
		if (notations.getBase() == null) {
			System.out.println("ERROR: Notations node was null.");
			return;
		}
		
		NodeList children = notations.getBase().getChildNodes();
		Node elem = null;
		
		for (int i = 0; i < children.getLength(); i++) {
			elem = children.item(i);
			String name = elem.getNodeName().trim();
			if (name.equalsIgnoreCase(Constants.SLUR_NODE)) {
				if (elem.hasAttributes()) {
					NamedNodeMap attributes = elem.getAttributes();
					for (int j = 0; j < attributes.getLength(); j++) {
						Node attrib = attributes.item(j);
						if (attrib.getNodeName().trim().equalsIgnoreCase(Constants.TYPE_NODE)) {
							if (!attrib.getNodeValue().trim().equalsIgnoreCase(Constants.START_NODE)) {
								articulations = Articulation.Slur;
								return;
							}
						}
					}
				}
			}
			else if (name.equalsIgnoreCase(Constants.ARTICULATIONS_NODE)) {
				NodeList artics = elem.getChildNodes();
				Node artic = null;
				for (int k = 0; k < artics.getLength(); k++) {
					artic = artics.item(k);
					String articName = artic.getNodeName().trim();
					if (articName.equalsIgnoreCase(Constants.ACCENT_NODE)) {
						articulations = Articulation.Accent;
						return;
					}
					else if (articName.equalsIgnoreCase(Constants.STACCATO_NODE)) {
						articulations = Articulation.Staccato;
						return;
					}
					else if (articName.equalsIgnoreCase(Constants.TENUTO_NODE)) {
						articulations = Articulation.Tenuto;
						return;
					}
					else if (articName.equalsIgnoreCase(Constants.STRONG_ACCENT_NODE)) {
						articulations = Articulation.StrongAccent;
						return;
					}
				}
			}
		}
	}
	
	private void updateArticulations() {
		if (oldArticulations != articulations) {
			if (Main.LOGGING) {
				System.out.println("Articulation updated to " + articulations + " in measure "
						+ (currentMeasure - (measureCount - realMeasures) + 1));
			}
			articulationChanges++;
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
		double articulationsMult = diffs.getArticulationDifficulty(articulations);
		
		double noteTotal = diffs.getNoteDifficulty(noteNum) * dynamicMult * keyMult * articulationsMult;
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

			double intervalTotal = diffs.getIntervalDifficulty(lastNote, noteNum, currentKey) * dynamicMult * keyMult * articulationsMult;
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
		System.out.println("Total articulation changes: " + ((articulationChanges - 1) == -1 ? 0 : (articulationChanges - 1)));
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
			System.out.println("Current articulations: " + articulations.name());
		}
	}
}