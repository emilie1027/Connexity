package application.model;

import java.io.*;
import java.util.*;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
//import org.w3c.dom.Node;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//import com.common.object.SmsSendResponseObject;

public class AmazonOffer {
	public String ASIN = null;
	public URL url = null;
	public String manufacturer = null;
	public String title = null;
	public String description = null;
	public List<URL> images = null;

	public AmazonOffer(Element element) throws MalformedURLException, NullPointerException {
	    ASIN = element.selectSingleNode("ASIN").getText();
	    title = element.selectSingleNode("Title").getText();
	    try {
            url =  new URL(element.selectSingleNode("DetailPageURL").getText());
        }
        catch (NullPointerException e) {
            url = null;
        }
	    try {
            manufacturer = element.selectSingleNode("Manufacturer").getText();
        }
        catch (NullPointerException e) {
            manufacturer = null;
        }
	}

	static public List<AmazonOffer> parseString(String input) throws IOException {
		Document doc = null;
        try {
        		List<AmazonOffer> result = new ArrayList<>();
        		doc = DocumentHelper.parseText(input);
        		//Element rootElt = doc.getRootElement();//get the root element
        		//Element Items = doc.getElementsByTagName("Items");
        		List list = doc.selectNodes("/Items/Item" );
        		Iterator iter=list.iterator();

        		while(iter.hasNext()){
        			Element element=(Element)iter.next(); 
        			result.add(new AmazonOffer(element));
        		}
        	
        		return result;
        }
        catch (DocumentException e) {
            throw new RuntimeException(e.getMessage());
        }
	}
}