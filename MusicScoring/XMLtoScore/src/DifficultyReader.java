import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class DifficultyReader {
	private File xmlToParse;
	private static final String LEVELS_NAME = "levels";
	private static final String INTERVAL_NAME = "interval";
	private static final String RANGE_NAME = "range";
	private int interval;
	private int range;
	
	public DifficultyReader(File xmlFile) {
		interval = -1;
		range = -1;
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
				try {
					interval = Integer.parseInt(elem.getTextContent());
				} catch (NumberFormatException e) {
					interval = -1;
				}				
				break;
			case RANGE_NAME:
				try {
					range = Integer.parseInt(elem.getTextContent());
				} catch (NumberFormatException e) {
					range = -1;
				}
				break;
			}
		}
	}

	public int getIntervalDifficulty() {
		return interval;
	}

	public int getRangeDifficulty() {
		return range;
	}	
	
}
