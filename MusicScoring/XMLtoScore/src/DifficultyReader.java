import java.io.File;
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

import Dynamics.dynamic;


public class DifficultyReader {
	private File xmlToParse;
	
	private static final String LEVELS_NAME = "levels";
	private static final String INTERVAL_NAME = "interval";
	//private static final String RANGE_NAME = "range";
	private static final String NOTES_NAME = "notes";
	private static final String DASH = "-";
	
	private static final int DEFAULT_INTERVAL_DIFFICULTY = 10;
	private static final double DEFAULT_DYNAMIC_DIFFICULTY = 1.5;
	private static final int DEFAULT_TEMPO_PER_SECOND_DIFFICULTY = 60;
	
	/*private int interval;
	private int range;*/
	private HashMap<Integer, Integer> individualNotes;
	private List<Interval> intervalDifficulties;
	private HashMap<dynamic, Double> dynamicDifficulties;
	
	public DifficultyReader(File xmlFile) {
		/*interval = -1;
		range = -1;*/
		individualNotes = new HashMap<Integer, Integer>();
		//intervalDifficulties = new ArrayList<Interval>();
		//dynamicDifficulties = new HashMap<dynamic, Double>();
		setDefaultIntervals();
		setDefaultDynamics();
		setXmlFile(xmlFile);
		start();
	}
	
	private void setXmlFile(File xmlFile) {
		xmlToParse = xmlFile;
	}
	
	private void start() {
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
		
		for (int i = 0; i < list.getLength(); i++) {
			elem = list.item(i);
			
			String name = elem.getNodeName().trim();
			if (name.equalsIgnoreCase(LEVELS_NAME)) {
				break;
			}
		}
		
		if (elem == null || !LEVELS_NAME.equalsIgnoreCase(elem.getNodeName().trim())) {
			return;
		}
		list = elem.getChildNodes();
		elem = null;

		for (int i = 0; i < list.getLength(); i++) {
			elem = list.item(i);
			
			String name = elem.getNodeName().trim().toLowerCase();

			switch(name) {
			case INTERVAL_NAME:
				setDefaultIntervals();			
				break;
			/*case RANGE_NAME:
				try {
					range = Integer.parseInt(elem.getTextContent());
				} catch (NumberFormatException e) {
					range = -1;
				}
				break;*/
			case NOTES_NAME:
				parseNoteDifficulty(elem.getChildNodes());
				break;
			}
		}
	}
	
	private void parseNoteDifficulty(NodeList notes) {
		if (notes == null || notes.getLength() == 0) {
			return;
		}
		
		Node elem = null;
		for (int i = 0; i < notes.getLength(); i++) {
			elem = notes.item(i);
			if (elem == null) { continue; }
			
			String note = elem.getNodeName().trim().toLowerCase();
			String value = elem.getTextContent().trim().toLowerCase();
			int diff = 1;
			try {
				diff = Integer.parseInt(value);
			} catch (NumberFormatException e) {
				System.out.println("ERROR: Note difficulty unreadable.");
			}
			
			int index = note.indexOf(DASH);
			if (index != -1) {
				int firstNote = Utils.noteToNum(note.substring(0, index));
				int secondNote = Utils.noteToNum(note.substring(index + 1));
				for (int j = firstNote; j <= secondNote; j++) {
					individualNotes.put(j, diff);
				}
			}
			else {
				int noteNum = Utils.noteToNum(note);
				individualNotes.put(noteNum, diff);
			}
			
		}
	}

	/*public int getIntervalDifficulty() {
		return interval;
	}

	public int getRangeDifficulty() {
		return range;
	}*/
	
	public int getNoteDifficulty(int noteNum) {
		Integer output = individualNotes.get(noteNum);
		if (output == null) {
			return 1;
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
	
	public double getDynamicDifficulty(dynamic dynam) {
		Double output = dynamicDifficulties.get(dynam);
		if (output == null) {
			return DEFAULT_DYNAMIC_DIFFICULTY;
		}
		else {
			return output.doubleValue();
		}
	}

	public double getTempoDifficulty(int tempo) {
		return (double)(tempo / DEFAULT_TEMPO_PER_SECOND_DIFFICULTY);
	}

	private void setDefaultIntervals() {
		Interval unisons = new Interval(Interval.ANY_NOTE, Interval.ANY_NOTE, Interval.ANY_NOTE, Interval.ANY_NOTE, 1, 1);
		int g3 = Utils.noteToNum("G", "3", "0");
		int g4 = Utils.noteToNum("G", "4", "0");
		Interval seconds = new Interval(g3, g4, g3, g4, 2, 2);
		Interval thirds = new Interval(g3, g4, g3, g4, 3, 3);
		Interval fourths = new Interval(g3, g4, g3, g4, 4, 4);
		Interval fifths = new Interval(g3, g4, g3, g4, 5, 5);
		int b4 = Utils.noteToNum("B", "4", "0");
		int c5 = Utils.noteToNum("C", "5", "0");
		Interval jumps = new Interval(g3, g4, b4, c5, Interval.ANY_INTERVAL, 8);
		int csharp5 = Utils.noteToNum("C", "5", "1");
		Interval highs = new Interval(b4, c5, csharp5, Interval.ANY_NOTE, Interval.ANY_INTERVAL, 10);
		Interval sixths = new Interval(Interval.ANY_NOTE, Interval.ANY_NOTE, Interval.ANY_NOTE, Interval.ANY_NOTE, 6, 9);
		Interval sevenths = new Interval(Interval.ANY_NOTE, Interval.ANY_NOTE, Interval.ANY_NOTE, Interval.ANY_NOTE, 7, 9);
		Interval octaves = new Interval(Interval.ANY_NOTE, Interval.ANY_NOTE, Interval.ANY_NOTE, Interval.ANY_NOTE, 8, 9);
		Interval larges = new Interval(Interval.ANY_NOTE, Interval.ANY_NOTE, Interval.ANY_NOTE, Interval.ANY_NOTE, -8, 10);
		intervalDifficulties = new ArrayList<Interval>();
		intervalDifficulties.add(unisons);
		intervalDifficulties.add(seconds);
		intervalDifficulties.add(thirds);
		intervalDifficulties.add(fourths);
		intervalDifficulties.add(fifths);
		intervalDifficulties.add(jumps);
		intervalDifficulties.add(highs);
		intervalDifficulties.add(sixths);
		intervalDifficulties.add(sevenths);
		intervalDifficulties.add(octaves);
		intervalDifficulties.add(larges);
	}

	private void setDefaultDynamics() {
		dynamicDifficulties = new HashMap<dynamic, Double>();
		dynamicDifficulties.put(dynamic.MF, (double) 1);
		dynamicDifficulties.put(dynamic.MP, (double) 1);
		dynamicDifficulties.put(dynamic.F, 1.1);
		dynamicDifficulties.put(dynamic.FF, 1.2);
		dynamicDifficulties.put(dynamic.P, 1.3);
		dynamicDifficulties.put(dynamic.PP, 1.5);
	}
}
