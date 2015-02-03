package MusicalElements;
import Main.Main;
import Utilities.Utils;



public class Interval {
	private int lowNoteRange1;
	private int highNoteRange1;
	private int lowNoteRange2;
	private int highNoteRange2;
	private int traditionalIntervalNotation;
	private int difficulty;
	public static final int ANY_NOTE = -1;
	public static final int ANY_INTERVAL = 0;
	
	public Interval (int aNote1Range1, int aNote2Range1, int aNote1Range2, int aNote2Range2, int traditionalInterval, int difficultyValue) {
		if (aNote1Range1 > aNote2Range1) {
			lowNoteRange1 = aNote2Range1;
			highNoteRange1 = aNote1Range1;
		}
		else {
			lowNoteRange1 = aNote1Range1;
			highNoteRange1 = aNote2Range1;
		}
		if (aNote1Range2 > aNote2Range2) {
			lowNoteRange2 = aNote2Range2;
			highNoteRange2 = aNote1Range2;
		}
		else {
			lowNoteRange2 = aNote1Range2;
			highNoteRange2 = aNote2Range2;
		}
		
		traditionalIntervalNotation = traditionalInterval;
		difficulty = difficultyValue;
	}
	
	public boolean matches(int note1, int note2, int key) {
		int low = (note1 < note2 ? note1 : note2);
		int high = (note1 < note2 ? note2 : note1);
		
		if ((matchRange1(low) && matchRange2(high)) || (matchRange1(high) && matchRange2(low))) {
			if (traditionalIntervalNotation == ANY_INTERVAL) {
				return true;
			}
			
			boolean onKeyLow = Utils.onKey(key, low);
			boolean onKeyHigh = Utils.onKey(key, high);
			if (onKeyLow != onKeyHigh) {
				//Both can be on or off key, but must match. Otherwise, it will be a minor interval.
				//If it is minor, then decrement the lower to make it match a wider interval as necessary.
				low--;
			}
			
			int extra = 0;
			if (!onKeyHigh) {
				extra = 1;
			}
			
			int newLow = low + extra;
			int newHigh = high + extra;
			
			if (!Utils.onKey(key, newLow) || !Utils.onKey(key, newHigh)) {
				if (Main.LOGGING) {
					System.out.println("ERROR: Something weird happen. Need to investigate.");
				}
				return false;
			}
			
			int actualDistance = traditionalIntervalNotation - 1;
			boolean greater = false;
			if (traditionalIntervalNotation < 0) {
				greater = true;
				actualDistance = Math.abs(traditionalIntervalNotation) - 1;
			}
			
			
			for (int i = 0; i < actualDistance; i++) {
				newLow = Utils.nextScaledNote(key, newLow);
			}
			
			newLow = newLow - extra;
			newHigh = newHigh - extra;
			
			return (greater ? newHigh > newLow : newLow == newHigh);
		}
		else {
			return false;
		}
	}
	
	private boolean matchRange1(int note) {
		return ((note >= lowNoteRange1 || lowNoteRange1 == ANY_NOTE) && (note <= highNoteRange1 || highNoteRange1 == ANY_NOTE));
	}
	
	private boolean matchRange2(int note) {
		return ((note >= lowNoteRange2 || lowNoteRange2 == ANY_NOTE) && (note <= highNoteRange2 || highNoteRange2 == ANY_NOTE));
	}

	public int getDifficulty() {
		return difficulty;
	}
}
