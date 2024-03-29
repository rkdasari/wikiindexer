/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.parsers;

import java.awt.List;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;


import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;

import edu.buffalo.cse.ir.wikiindexer.wikipedia.WikipediaDocument;
import edu.buffalo.cse.ir.wikiindexer.wikipedia.WikipediaParser;

/**
 * @author nikhillo
 *
 */
public class Parser extends DefaultHandler{
	/* */
	private final Properties props;

	Collection<WikipediaDocument> documents=new ArrayList<WikipediaDocument>();
	WikipediaParser WP;
	ArrayList<String> textstr = new ArrayList<String>();
	String thisXMLTag;
	String thisXMLText;
	String thisdate;
	String thisAuthor;
	int thisID;
	String thisTitle;
	StringBuffer onlytext = new StringBuffer();
	String textval;

	/**
	 * 
	 * @param idxConfig
	 * @param parser
	 */
	public Parser(Properties idxProps) {
		props = idxProps;
	}

	/* TODO: Implement this method */
	/**
	 * 
	 * @param filename
	 * @param docs
	 */
	public void parse(String filename, Collection<WikipediaDocument> docs) {
		documents=docs;
		//System.out.println(docs.toString());
		SAXParserFactory sp = SAXParserFactory.newInstance();
		SAXParser sparser= null;
		try {
			sparser = sp.newSAXParser();
			//System.out.println("this happened");
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Parser handler = new Parser(props);
		//System.out.println("this happened");
		try {
			sparser.parse(filename,handler);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void characters (char[] buffer, int start, int length ){

		thisXMLText = String.copyValueOf(buffer, start, length).trim();
		//System.out.println(thisXMLText);

		if(thisXMLTag.equalsIgnoreCase("text")){
			//System.out.println("text goes on");
			//System.out.println(thisXMLText);
			if(!thisXMLText.equals("")){
				onlytext.append(thisXMLText);
			}

		}

	}

	public void startElement( String uri, String localName, String qName, Attributes attributes) {

		thisXMLTag=qName;
		if(thisXMLTag.equalsIgnoreCase("page")){
			onlytext.delete(0, onlytext.length());
			System.out.println("start element here ------------------------------ "+thisXMLTag);

		}
	}

	public void endElement(String uri, String localName, String qName){

		if(qName.equalsIgnoreCase("page")){
			try {
				System.out.println("end element here ------------------------------ "+qName);
				//System.out.println(onlytext.toString());
				WikipediaParser WP = new WikipediaParser(thisID,thisdate,thisAuthor,thisTitle);
				String str= WP.parseListItem(onlytext.toString());
				//System.out.println(str);
				textstr.add(onlytext.toString());
			
				//System.out.println("OBJECT WORKED ----- "+ WD.getAuthor());

			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(thisXMLTag.equals("id")){
			if(!thisXMLText.equals(""))
				thisID= Integer.parseInt(thisXMLText);
			//System.out.println(thisID);
		}
		else if(thisXMLTag.equals("timestamp")){
			thisdate=thisXMLText;
			//System.out.println(thisdate);
		}
		else if(thisXMLTag.equals("username")){
			thisAuthor=thisXMLText;
			//	System.out.println("user" +thisAuthor);
		}
		else if(thisXMLTag.equals("ip") && qName.equals("ip")){
			thisAuthor=thisXMLText;
			//	System.out.println("ip"+thisAuthor);
		}

		else if(thisXMLTag.equals("title")){
			thisTitle=thisXMLText;
			//	System.out.println(thisTitle);
		}


	}

	/**
	 * Method to add the given document to the collection.
	 * PLEASE USE THIS METHOD TO POPULATE THE COLLECTION AS YOU PARSE DOCUMENTS
	 * For better performance, add the document to the collection only after
	 * you have completely populated it, i.e., parsing is complete for that document.
	 * @param doc: The WikipediaDocument to be added
	 * @param documents: The collection of WikipediaDocuments to be added to
	 */


	private synchronized void add(WikipediaDocument doc, Collection<WikipediaDocument> documents) {
		documents.add(doc);

	}

}

