import java.io.File;



public class Scorer {
	private File xml;
	private Parser parse;
	@SuppressWarnings("unused")
	private DifficultyLevel level;
	
	public Scorer (DifficultyLevel aLevel, File xmlFile) {
		xml = xmlFile;
		parse = new Parser(xml);
		level = aLevel;
		while (parse.parseMeasures()) {
			System.out.println("Still more measures to read.");
		}
		
		//Read difficulty level file.
		//Apply level to parsed measures.
	}
}
