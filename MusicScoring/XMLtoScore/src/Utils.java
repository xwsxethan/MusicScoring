import Dynamics.*;


public class Utils {
	private static int alteration;
	
	public static final int NOTES_IN_OCTAVE = 12;
	private static final int DIFFERENCE_BETWEEN_SCALE_STARTS = 7;
	private static final int SECONDS_IN_MINUTE = 60;
	
	private static final String SHARP = "#";
	private static final String FLAT = "b";
	
	private static final String WHOLE = "whole";
	private static final String HALF = "half";
	private static final String QUARTER = "quarter";
	private static final String EIGHTH = "eighth";
	private static final String SIXTEENTH = "16th";
	private static final String THIRTYSECOND = "32nd";
	
	private static final int REGULAR = 4;
	private static final int COMPOUND = 8;
	
	public static int noteToNum(String noteLetterAccidentalOctave) {
		if (noteLetterAccidentalOctave == null || noteLetterAccidentalOctave.isEmpty()) {
			return 0;
		}
		
		if (noteLetterAccidentalOctave.length() < 2 || noteLetterAccidentalOctave.length() > 4) {
			return 0;
		}
		
		String noteName = noteLetterAccidentalOctave.substring(0, 1);
		String accidental = noteLetterAccidentalOctave.substring(1, 2);
		String alter = "0";
		String octave = "0";
		if (accidental.equals(SHARP)) {
			alter = "1";
			octave = noteLetterAccidentalOctave.substring(2);
		}
		else if (accidental.equals(FLAT)) {
			alter = "-1";
			octave = noteLetterAccidentalOctave.substring(2);
		}
		else {
			octave = noteLetterAccidentalOctave.substring(1);
		}
		
		return noteToNum(noteName, octave, alter);
		
	}
	
	public static int noteToNum(String noteName, String octave, String alter) {
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
		alteration = 0;

		if (alter != null && !alter.isEmpty()) {
			try {
				int changeAmount = Integer.parseInt(alter);
				base = base + changeAmount;
				alteration = changeAmount;
			} catch (NumberFormatException e) {
				System.out.println("ERROR: Note alter not formatted correctly.");
			}
		}
		
		return base;
	}
	
	private static int noteLetterToNum(String noteLetter) {
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
	
	public static int getAlteration() {
		return alteration;
	}
	
	public static String numToNote(int note) {
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

	public static double typeAndTempoToDuration(String noteType, int beatsPerMinute, int beatsPerMeasure) {
		if (noteType == null || noteType.isEmpty()) {
			return 0;
		}
		
		double beats;
		switch(noteType.trim().toLowerCase()) {
		case (WHOLE) : {
			beats = 4;
			break;
		}
		case (HALF) : {
			beats = 2;
			break;
		}
		case (QUARTER) : {
			beats = 1;
			break;
		}
		case (EIGHTH) : {
			beats = 0.5;
			break;
		}
		case (SIXTEENTH) : {
			beats = 0.25;
			break;
		}
		case (THIRTYSECOND) : {
			beats = 0.125;
			break;
		}
		default : {
			if (beatsPerMeasure == COMPOUND) {
				beats = 0.5;
			}
			else if (beatsPerMeasure == REGULAR) {
				beats = 1;
			}
			else {
				beats = 1;
			}
		}			
		}
		
		if (beatsPerMeasure == COMPOUND) {
			beats = beats * 2;
		}
		
		double duration = beats / ((double)beatsPerMinute / SECONDS_IN_MINUTE);
		return duration;
	}

	public static dynamic stringToDynamic(String dynamicName) {
		if (dynamicName == null || dynamicName.isEmpty()) {
			return dynamic.ERROR;
		}
		
		dynamic[] dynamics = dynamic.values();
		for (int j = 0; j < dynamics.length; j++) {
			if (dynamics[j].name().equalsIgnoreCase(dynamicName.trim())) {
				return dynamics[j];
			}
		}
		
		return dynamic.ERROR;
	}

	public static String dynamicToString(dynamic dynam) {
		return dynam.name();
	}

	/**
	 * Determines the chromatic start position of the scale for this key based on the integer
	 * notation being used.
	 * @param key specifies how many sharps (positive) or flats (negative) there are.
	 * @return is the starting note for a scale of this key mod 12.
	 */
	public static int keyToScaleStart(int key) {
		int c20 = NOTES_IN_OCTAVE*20; //Making a really high number that is still 0 (mod 12).
		int transition = key*DIFFERENCE_BETWEEN_SCALE_STARTS; //Could be positive or negative
		int position = (c20 + transition) % NOTES_IN_OCTAVE; //This ensures a positive is modded, resulting in positive.
		return position;
	}

	public static boolean onKey(int key, int note) {
		int scaleStart = keyToScaleStart(key);
		int diff = ((note + NOTES_IN_OCTAVE) - scaleStart) % NOTES_IN_OCTAVE; //Force this to positive
		switch (diff) {
		case 0 :
		case 2 :
		case 4 :
		case 5 :
		case 7 :
		case 9 :
		case 11 :
		case 12 :
			return true;
		default :
			return false;
		}
	}

	public static int nextScaledNote(int key, int note) {
		if (!onKey(key, note)) {
			System.out.println("ERROR: Note not on key. Unclear how to proceed. Returning original note.");
			return note;
		}
		
		int scaleStart = keyToScaleStart(key);
		int diff = (NOTES_IN_OCTAVE + (note - scaleStart)) % NOTES_IN_OCTAVE;
		
		switch (diff) {
		case 0 :
		case 2 :
		case 5 :
		case 7 :
		case 9 :
		case 12 :
			return note + 2;
		case 4 :
		case 11 :
			return note + 1;
		default :
			System.out.println("ERROR: Note not on key. Unclear how to proceed. Returning original note.");
			return note;
		}
	}
}
