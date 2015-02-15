package Main;

public class ValidationResults {
	private String noteResults;
	private String intervalResults;
	
	public ValidationResults(String notes, String intervals) {
		noteResults = notes;
		intervalResults = intervals;
	}
	
	public String getNoteResults() {
		return noteResults;
	}
	
	public void setNoteResults(String notes) {
		noteResults = notes;
	}
	
	public String getIntervalResults() {
		return intervalResults;
	}
	
	public void setIntervalResults(String intervals) {
		intervalResults = intervals;
	}
}
