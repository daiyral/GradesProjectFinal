package CountryParser;


import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class CountryXMLParser {
	final static String KEY_COUNTRY="country";
	final static String KEY_NAME="name";
	final static String KEY_FLAG="flag";
	final static String KEY_ANTHEM="anthem";
	final static String KEY_SHORT="short";
	final static String KEY_DETAILS="details";

	public static ArrayList<Country> parseCountries(String filename, Context context){
		ArrayList<Country> data = null;
		InputStream in = openCountriesFile(context, filename);
		XmlPullParserFactory xmlFactoryObject;
		try {
				xmlFactoryObject = XmlPullParserFactory.newInstance();
				XmlPullParser parser = xmlFactoryObject.newPullParser();
		
				parser.setInput(in, null);
		        int eventType = parser.getEventType();
		        Country currentCountry = null;
		        String inTag = "";
		        String strTagText = null;
		
		        while (eventType != XmlPullParser.END_DOCUMENT){
		        	inTag = parser.getName();
		            switch (eventType){
		                case XmlPullParser.START_DOCUMENT:
		                	data = new ArrayList<Country>();
		                    break;
		                case XmlPullParser.START_TAG:
		                	if (inTag.equalsIgnoreCase(KEY_COUNTRY))
		                    	currentCountry = new Country();
		                    break;
		                case XmlPullParser.TEXT:
		                	strTagText = parser.getText();
		                	break;
		                case XmlPullParser.END_TAG:
		                	if (inTag.equalsIgnoreCase(KEY_COUNTRY))
		                    	data.add(currentCountry);
		                	else if (inTag.equalsIgnoreCase(KEY_NAME))
		                    	currentCountry.name = strTagText;
		                	else if (inTag.equalsIgnoreCase(KEY_SHORT))
		                    	currentCountry.shorty =strTagText;
		                	else if (inTag.equalsIgnoreCase(KEY_FLAG))
		                    	currentCountry.flag =strTagText;
		                	else if (inTag.equalsIgnoreCase(KEY_ANTHEM))
		                    	currentCountry.anthem =strTagText;
		                	else if (inTag.equalsIgnoreCase(KEY_DETAILS))
		                    	currentCountry.setDetails(strTagText);
		            		inTag ="";
		                	break;	                    
		            }//switch
		            eventType = parser.next();
		        }//while
			} catch (Exception e) {e.printStackTrace();}
		return data;
	}

	public static void exportCountries(Context context, ArrayList<Country> countries){
		try {
			FileOutputStream fileos = context.openFileOutput("countries_after_removed.xml", Context.MODE_PRIVATE);
			XmlSerializer xmlSerializer = Xml.newSerializer();
			StringWriter writer = new StringWriter();
			xmlSerializer.setOutput(writer);
			xmlSerializer.startDocument("UTF-8", true);
			xmlSerializer.startTag(null, "countries");
			for (Country country: countries) {
				xmlSerializer.startTag(null, "country");
				xmlSerializer.startTag(null, "name");
				xmlSerializer.text(country.getName());
				xmlSerializer.endTag(null, "name");
				xmlSerializer.startTag(null, "flag");
				xmlSerializer.text(country.getFlag());
				xmlSerializer.endTag(null, "flag");
				xmlSerializer.startTag(null, "anthem");
				xmlSerializer.text(country.getAnthem());
				xmlSerializer.endTag(null, "anthem");
				xmlSerializer.startTag(null, "short");
				xmlSerializer.text(country.getShorty());
				xmlSerializer.endTag(null, "short");
				xmlSerializer.startTag(null, "details");
				xmlSerializer.text(country.getDetails());
				xmlSerializer.endTag(null, "details");
				xmlSerializer.endTag(null, "country");
			}
			xmlSerializer.endTag(null, "countries");
			xmlSerializer.endDocument();
			xmlSerializer.flush();
			String dataWrite = writer.toString();
			fileos.write(dataWrite.getBytes());
			fileos.close();
		} catch (IOException | IllegalStateException | IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static ArrayList<Country> importCountries(Context context, String filename){
		ArrayList<Country> data = null;
		try{
			FileInputStream fis = context.openFileInput(filename);
			InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
			XmlPullParserFactory xmlFactoryObject;
			try {
				xmlFactoryObject = XmlPullParserFactory.newInstance();
				XmlPullParser parser = xmlFactoryObject.newPullParser();
				parser.setInput(inputStreamReader);
				int eventType = parser.getEventType();
				Country currentCountry = null;
				String inTag = "";
				String strTagText = null;

				while (eventType != XmlPullParser.END_DOCUMENT){
					inTag = parser.getName();
					switch (eventType){
						case XmlPullParser.START_DOCUMENT:
							data = new ArrayList<Country>();
							break;
						case XmlPullParser.START_TAG:
							if (inTag.equalsIgnoreCase(KEY_COUNTRY))
								currentCountry = new Country();
							break;
						case XmlPullParser.TEXT:
							strTagText = parser.getText();
							break;
						case XmlPullParser.END_TAG:
							if (inTag.equalsIgnoreCase(KEY_COUNTRY))
								data.add(currentCountry);
							else if (inTag.equalsIgnoreCase(KEY_NAME))
								currentCountry.name = strTagText;
							else if (inTag.equalsIgnoreCase(KEY_SHORT))
								currentCountry.shorty =strTagText;
							else if (inTag.equalsIgnoreCase(KEY_FLAG))
								currentCountry.flag =strTagText;
							else if (inTag.equalsIgnoreCase(KEY_ANTHEM))
								currentCountry.anthem =strTagText;
							else if (inTag.equalsIgnoreCase(KEY_DETAILS))
								currentCountry.setDetails(strTagText);
							inTag ="";
							break;
					}//switch
					eventType = parser.next();
				}//while
			} catch (Exception e) {e.printStackTrace();}
			return data;
		}catch (FileNotFoundException e){
			e.printStackTrace();
		}
		return data;
	}

	private static InputStream openCountriesFile(Context context, String filename){
		AssetManager assetManager = context.getAssets();
		InputStream in =null;
		try {
			in = assetManager.open(filename);
		} catch (IOException e) {e.printStackTrace();}
		return in;
	}
}
