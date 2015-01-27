package Visitors;

import DifficultyNodes.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import DifficultyLevels.DifficultyLevel;
import MusicalElements.*;
import Utilities.*;

public class DifficultyReaderVisitor implements IDifficultyElementVisitor {
	
	private DifficultyLevel level;
	
	private static final int DEFAULT_NOTE_DIFFICULTY = 1;
	private static final int DEFAULT_INTERVAL_DIFFICULTY = 10;
	private static final double DEFAULT_DYNAMIC_DIFFICULTY = 1.5;
	private static final double DEFAULT_TEMPO_DIFFICULTY = 1;
	private static final double DEFAULT_KEY_DIFFICULTY = 1;
	private static final double DEFAULT_ARTICULATION_DIFFICULTY = 1;
	
	private HashMap<Integer, Integer> noteDifficulties;
	private List<Interval> intervalDifficulties;
	private HashMap<Dynamic, Double> dynamicDifficulties;
	private Double tempoDifficulty;
	private HashMap<Integer, Double> keyDifficulties;
	private HashMap<Articulation, Double> articulationDifficulties;
	
	private int difference;
	private int location1Low;
	private int location1High;
	private int location2Low;
	private int location2High;
	private double tempDifficulty;
	private int tempKey;

	private boolean foundDifference;
	private boolean foundLocation1;
	private boolean foundLocation2;
	private boolean foundDifficulty;
	private boolean foundKey;
	
	private DefaultOutputTypes aType;
	
	
 	public DifficultyReaderVisitor(DifficultyLevel lev) {
		resetIntervalBooleans();
		level = lev;
		aType = DefaultOutputTypes.note;

		noteDifficulties = new HashMap<Integer, Integer>();
		intervalDifficulties = new ArrayList<Interval>();
		dynamicDifficulties = new HashMap<Dynamic, Double>();
		tempoDifficulty = (double) 1;
		keyDifficulties = new HashMap<Integer, Double>();
		articulationDifficulties = new HashMap<Articulation, Double>();

		start();
	}
	
	private void resetIntervalBooleans() {
		foundDifference = false;
		foundLocation1 = false;
		foundLocation2 = false;
		foundDifficulty = false;
		foundKey = false;
	}
	
	private void start() {
		DocumentBuilder builder;
		Document toRead;
		NodeList list;
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			toRead = builder.parse(level.getXMLFile());
		} catch (IllegalArgumentException | ParserConfigurationException | SAXException | IOException e) {
			System.out.println("ERROR: Couldn't open the xml file to read. Possibly an incorrect name.");
			System.out.println("Current name: " + level.getXMLFileLocation());
			e.printStackTrace();
			return;
		}
		
		list = toRead.getChildNodes();		
		Node elem = null;
		
		for (int i = 0; i < list.getLength(); i++) {
			elem = list.item(i);
			
			String name = elem.getNodeName().trim();
			if (name.equalsIgnoreCase(Constants.DIFFICULTY_LEVELS_NAME)) {
				break;
			}
		}
		
		if (elem == null || !Constants.DIFFICULTY_LEVELS_NAME.equalsIgnoreCase(elem.getNodeName().trim())) {
			return;
		}
		
		Levels levels = new Levels(elem);
		levels.accept(this);
	}
		
	public int getNoteDifficulty(int noteNum) {
		Integer output = noteDifficulties.get(noteNum);
		if (output == null) {
			return DEFAULT_NOTE_DIFFICULTY;
		}
		else {
			return output.intValue();
		}
	}

	public int getIntervalDifficulty(int note1, int note2, int key) {
		for(Interval inter : intervalDifficulties) {
			if (inter.matches(note1, note2, key)) {
				return inter.getDifficulty();
			}
		}
		
		return DEFAULT_INTERVAL_DIFFICULTY;
	}
	
	public double getDynamicDifficulty(Dynamic dynam) {
		Double output = dynamicDifficulties.get(dynam);
		if (output == null) {
			return DEFAULT_DYNAMIC_DIFFICULTY;
		}
		else {
			return output.doubleValue();
		}
	}

	public double getTempoDifficulty(double duration, int notes) {
		double initial = (double)notes / duration;
		if (initial < 1) {
			//If 1 note for 2 seconds, initial should be the inverse.
			//Instead of 0.5, you should get 2, because that is also difficult.
			initial = 1 / initial;
		}
		
		return initial * (tempoDifficulty == null ? DEFAULT_TEMPO_DIFFICULTY : tempoDifficulty);
	}

	public double getKeySignatureDifficulty(int key) {
		Double output = keyDifficulties.get(key);
		if (output == null) {
			return DEFAULT_KEY_DIFFICULTY;
		}
		else {
			return output.doubleValue();
		}
	}
	
	public double getArticulationDifficulty(Articulation artic) {
		Double output = articulationDifficulties.get(artic);
		if (output == null) {
			return DEFAULT_ARTICULATION_DIFFICULTY;
		}
		else {
			return output.doubleValue();
		}
	}
	
	@Override
	public void visit(Levels levels) {
		NodeList list = levels.getBase().getChildNodes();
		Node elem = null;

		for (int i = 0; i < list.getLength(); i++) {
			elem = list.item(i);
			
			String name = elem.getNodeName().trim().toLowerCase();

			switch(name) {
			case Constants.DIFFICULTY_NOTES_NAME:
				NoteDifficulty noteDifficulty = new NoteDifficulty(elem);
				noteDifficulty.accept(this);
				continue;
			case Constants.DIFFICULTY_INTERVALS_NAME:
				IntervalsDifficulty intervalDifficulty = new IntervalsDifficulty(elem);
				intervalDifficulty.accept(this);
				continue;
			case Constants.DIFFICULTY_DYNAMICS_NAME:
				DynamicsDifficulty dynamicsDifficulty = new DynamicsDifficulty(elem);
				dynamicsDifficulty.accept(this);
				continue;
			case Constants.DIFFICULTY_TEMPO_NAME:
				TempoDifficulty tempoDifficulty = new TempoDifficulty(elem);
				tempoDifficulty.accept(this);
				continue;
			case Constants.DIFFICULTY_ARTICULATIONS_NAME:
				ArticulationDifficulty articulationsDifficulty = new ArticulationDifficulty(elem);
				articulationsDifficulty.accept(this);
				continue;
			case Constants.DIFFICULTY_KEY_SIGNATURE_NAME:
				KeySignatureDifficulty keyDifficulty = new KeySignatureDifficulty(elem);
				keyDifficulty.accept(this);
				continue;
			}
		}
	}

	@Override
	public void visit(NoteDifficulty noteDifficulty) {
		NodeList notes = noteDifficulty.getBase().getChildNodes();
		
		if (notes == null || notes.getLength() == 0) {
			return;
		}
		
		Node elem = null;
		for (int i = 0; i < notes.getLength(); i++) {
			elem = notes.item(i);
			if (elem == null) { continue; }
			
			String note = elem.getNodeName().trim();
			if (note.equalsIgnoreCase(Constants.DIFFICULTY_INDIVIDUAL_NOTE_NAME)) {
				IndividualNoteDifficulty indiv = new IndividualNoteDifficulty(elem);
				indiv.accept(this);
			}
			else {
				continue;
			}			
		}
	}

	public void visit(IndividualNoteDifficulty individualNoteDifficulty) {
		NodeList notes = individualNoteDifficulty.getBase().getChildNodes();
		
		if (notes == null || notes.getLength() == 0) {
			return;
		}
		
		Node elem = null;
		resetIntervalBooleans();
		aType = DefaultOutputTypes.note;
		
		for (int i = 0; i < notes.getLength(); i++) {
			elem = notes.item(i);
			if (elem == null) { continue; }
			
			String name = elem.getNodeName().trim().toLowerCase();
			
			switch (name) {
			case (Constants.DIFFICULTY_RANGE_NAME) :
				RangeDifficulty range = new RangeDifficulty(elem);
				range.accept(this);
				foundLocation1 = true;
				break;
			case (Constants.DIFFICULTY_NAME) :
				Difficulty difficulty = new Difficulty(elem);
				difficulty.accept(this);
				foundDifficulty = true;
				break;
			default :
					break;
			}		
		}

		
		if (foundLocation1 && foundDifficulty) {
			int fixedDifficulty;
			try {
				fixedDifficulty = (int)tempDifficulty;
			}
			catch (ClassCastException e) {
				System.out.println("ERROR: Note difficulty was specified incorrectly. Assuming default value: "
					+ DEFAULT_NOTE_DIFFICULTY);
				fixedDifficulty = DEFAULT_NOTE_DIFFICULTY;
			}
			for (int j = location1Low; j <= location1High; j++) {
				noteDifficulties.put(j, fixedDifficulty);
			}
		}
		else {
			System.out.println("ERROR: Not all note difficulty parameters specified. Ignoring.");
		}
	}

	public void visit(RangeDifficulty rangeDifficulty) {
		String note = rangeDifficulty.getBase().getTextContent().trim().toLowerCase();
				
		int index = note.indexOf(Constants.DIFFICULTY_DASH);
		if (index != -1) {
			location1Low = Utils.noteToNum(note.substring(0, index));
			location1High = Utils.noteToNum(note.substring(index + 1));
		}
		else {
			location1Low = Utils.noteToNum(note);
			location1High = location1Low;
		}
	}

	@Override
	public void visit(IntervalsDifficulty intervalDifficulty) {
		NodeList intervals = intervalDifficulty.getBase().getChildNodes();
		
		if (intervals == null || intervals.getLength() == 0) {
			return;
		}
		
		Node elem = null;
		for (int i = 0; i < intervals.getLength(); i++) {
			elem = intervals.item(i);
			if (elem == null || !elem.getNodeName().equalsIgnoreCase(Constants.DIFFICULTY_INDIVIDUAL_INTERVAL_NAME)) {
				continue;
			}
			
			IndividualIntervalDifficulty interval = new IndividualIntervalDifficulty(elem);
			interval.accept(this);
		}
	}

	@Override
	public void visit(IndividualIntervalDifficulty individualIntervalDifficulty) {
		NodeList elems = individualIntervalDifficulty.getBase().getChildNodes();
		
		if (elems == null || elems.getLength() == 0) {
			return;
		}
		
		Node elem = null;
		resetIntervalBooleans();
		aType = DefaultOutputTypes.interval;
		
		for (int i = 0; i < elems.getLength(); i++) {
			elem = elems.item(i);
			if (elem == null) {
				continue;
			}
			
			String name = elem.getNodeName().trim().toLowerCase();
			
			switch (name) {
			case (Constants.DIFFICULTY_DIFFERENCE_NAME) :
				DifferenceDifficulty difference = new DifferenceDifficulty(elem);
				difference.accept(this);
				foundDifference = true;
				break;
			case (Constants.DIFFICULTY_LOCATION1_NAME) :
				Location1Difficulty loc1 = new Location1Difficulty(elem);
				loc1.accept(this);
				foundLocation1 = true;
				break;
			case (Constants.DIFFICULTY_LOCATION2_NAME) :
				Location2Difficulty loc2 = new Location2Difficulty(elem);
				loc2.accept(this);
				foundLocation2 = true;
				break;
			case (Constants.DIFFICULTY_NAME) :
				Difficulty difficulty = new Difficulty(elem);
				difficulty.accept(this);
				foundDifficulty = true;
				break;
			default :
					break;
			}
		}
		
		if (foundDifference && foundLocation1 && foundLocation2 && foundDifficulty) {
			int fixedDifficulty;
			try {
				fixedDifficulty = (int)tempDifficulty;
			}
			catch (ClassCastException e) {
				System.out.println("ERROR: Interval difficulty was specified incorrectly. Assuming default value: "
					+ DEFAULT_INTERVAL_DIFFICULTY);
				fixedDifficulty = DEFAULT_INTERVAL_DIFFICULTY;
			}
			Interval toAdd = new Interval(location1Low, location1High, location2Low, location2High, difference, fixedDifficulty);
			intervalDifficulties.add(toAdd);
		}
		else {
			System.out.println("ERROR: Not all interval difficulty parameters specified. Ignoring.");
		}
	}
	
	@Override
	public void visit(DifferenceDifficulty differenceDifficulty) {
		String value = differenceDifficulty.getBase().getTextContent().trim().toLowerCase();
				
		int diff;
		try {
			diff = Integer.parseInt(value);
		} catch (NumberFormatException e) {
			System.out.println("ERROR: Note difficulty value unreadable.");
			foundDifference = false;
			return;
		}
		
		difference = diff;
	}
	
	@Override
	public void visit(Location1Difficulty location1Difficulty) {
		String notes = location1Difficulty.getBase().getTextContent().trim().toLowerCase();
		
		try {
			if (Integer.parseInt(notes) == Interval.ANY_NOTE) {
				location1Low = Interval.ANY_NOTE;
				location1High = Interval.ANY_NOTE;
			}
		}
		catch (NumberFormatException e) {
			int dashIndex = notes.indexOf(Constants.DIFFICULTY_DASH);
		
			if (dashIndex != -1) {
				location1Low = Utils.noteToNum(notes.substring(0, dashIndex));
				location1High = Utils.noteToNum(notes.substring(dashIndex + 1));
			}
			else {
				location1Low = Utils.noteToNum(notes);
				location1High = location1Low;
			}
		}
	}
	
	@Override
	public void visit(Location2Difficulty location2Difficulty) {
		String notes = location2Difficulty.getBase().getTextContent().trim().toLowerCase();
		
		try {
			if (Integer.parseInt(notes) == Interval.ANY_NOTE) {
				location2Low = Interval.ANY_NOTE;
				location2High = Interval.ANY_NOTE;
			}
		}
		catch (NumberFormatException e) {
			int dashIndex = notes.indexOf(Constants.DIFFICULTY_DASH);
		
			if (dashIndex != -1) {
				location2Low = Utils.noteToNum(notes.substring(0, dashIndex));
				location2High = Utils.noteToNum(notes.substring(dashIndex + 1));
			}
			else {
				location2Low = Utils.noteToNum(notes);
				location2High = location2Low;
			}
		}
	}
	
	@Override
	public void visit(Difficulty difficulty) {
		String diff = difficulty.getBase().getTextContent().trim().toLowerCase();
		
		try { 
			double difficultyLevel = Double.parseDouble(diff);
			if (difficultyLevel < 1) {
				tempDifficulty = 1;
			}
			else {
				tempDifficulty = difficultyLevel;
			}
		}
		catch (NumberFormatException e) {
			switch (aType) {
				case note :
					System.out.println("ERROR: Note difficulty not correctly specified. Assuming default level: "
						+ DEFAULT_NOTE_DIFFICULTY);
					tempDifficulty = DEFAULT_NOTE_DIFFICULTY;
					break;
				case interval :
					System.out.println("ERROR: Interval difficulty not correctly specified. Assuming default level: "
						+ DEFAULT_INTERVAL_DIFFICULTY);
					tempDifficulty = DEFAULT_INTERVAL_DIFFICULTY;
					break;
				case key :
					System.out.println("ERROR: Key signature difficulty not correctly specified. Assuming default level: "
						+ DEFAULT_KEY_DIFFICULTY);
					tempDifficulty = DEFAULT_KEY_DIFFICULTY;
					break;
			}
		}

	}
	
	@Override
	public void visit(DynamicsDifficulty dynamicsDifficulty) {
		NodeList children = dynamicsDifficulty.getBase().getChildNodes();
		
		if (children == null || children.getLength() == 0) {
			return;
		}
		
		Node elem = null;
		for (int i = 0; i < children.getLength(); i++) {
			elem = children.item(i);
			
			if (elem == null) {
				continue;
			}
			
			String name = elem.getNodeName().trim().toLowerCase();			
			Dynamic output = Utils.stringToDynamic(name);
			
			if (output == Dynamic.ERROR) {
				System.out.println("ERROR: Could not parse the specified dynamic level. Skipping.");
				continue;
			}
			
			String value = elem.getTextContent().trim().toLowerCase();
			
			try {
				double difficultyMultiplier = Double.parseDouble(value);
				dynamicDifficulties.put(output, difficultyMultiplier);
			}
			catch (NumberFormatException e) {
				System.out.println("ERROR: Could not parse the specified difficulty for the dynamic level. Skipping.");
			}
		}
	}

	@Override
	public void visit(TempoDifficulty tempoDifficultyNode) {
		String diff = tempoDifficultyNode.getBase().getTextContent().trim().toLowerCase();
		
		try { 
			tempoDifficulty = Double.parseDouble(diff);
		}
		catch (NumberFormatException e) {
			tempoDifficulty = DEFAULT_TEMPO_DIFFICULTY;
		}
	}

	@Override
	public void visit(KeySignatureDifficulty keySignatureDifficulty) {
		NodeList keysigs = keySignatureDifficulty.getBase().getChildNodes();
		
		if (keysigs == null || keysigs.getLength() == 0) {
			return;
		}
		
		Node elem = null;
		for (int i = 0; i < keysigs.getLength(); i++) {
			elem = keysigs.item(i);
			if (elem == null || !elem.getNodeName().equalsIgnoreCase(Constants.DIFFICULTY_INDIVIDUAL_KEY_SIGNATURE_NAME)) {
				continue;
			}
			
			IndividualKeySignatureDifficulty keysig = new IndividualKeySignatureDifficulty(elem);
			keysig.accept(this);
		}
	}

	@Override
	public void visit(IndividualKeySignatureDifficulty individualKeySignatureDifficulty) {
		NodeList elems = individualKeySignatureDifficulty.getBase().getChildNodes();
		
		if (elems == null || elems.getLength() == 0) {
			return;
		}
		
		Node elem = null;
		resetIntervalBooleans();
		aType = DefaultOutputTypes.key;
		
		for (int i = 0; i < elems.getLength(); i++) {
			elem = elems.item(i);
			if (elem == null) {
				continue;
			}
			
			String name = elem.getNodeName().trim().toLowerCase();
			
			switch (name) {
			case (Constants.DIFFICULTY_KEY_NAME) :
				KeyNameDifficulty key = new KeyNameDifficulty(elem);
				key.accept(this);
				foundKey = true;
				break;
			case (Constants.DIFFICULTY_NAME) :
				Difficulty difficulty = new Difficulty(elem);
				difficulty.accept(this);
				foundDifficulty = true;
				break;
			default :
					break;
			}
		}
		
		if (foundKey && foundDifficulty) {
			keyDifficulties.put(tempKey, tempDifficulty);
		}
		else {
			System.out.println("ERROR: Not all key difficulty parameters specified. Ignoring.");
		}
	}
	
	@Override
	public void visit(KeyNameDifficulty keyNameDifficulty) {
		String value = keyNameDifficulty.getBase().getTextContent().trim().toLowerCase();
		
		tempKey = Utils.namedKeyToNum(value);
	}

	@Override
	public void visit(ArticulationDifficulty articulationDifficulty) {
		NodeList children = articulationDifficulty.getBase().getChildNodes();
		
		if (children == null || children.getLength() == 0) {
			return;
		}
		
		Node elem = null;
		for (int i = 0; i < children.getLength(); i++) {
			elem = children.item(i);
			
			if (elem == null) {
				continue;
			}
			
			String name = elem.getNodeName().trim().toLowerCase();			
			Articulation output = Utils.stringToArticulation(name);
			
			String value = elem.getTextContent().trim().toLowerCase();
			
			try {
				double difficultyMultiplier = Double.parseDouble(value);
				articulationDifficulties.put(output, difficultyMultiplier);
			}
			catch (NumberFormatException e) {
				System.out.println("ERROR: Could not parse the specified difficulty for the articulation. Skipping.");
			}
		}
	}

	
	private enum DefaultOutputTypes {
		note, interval, key
	}
}
