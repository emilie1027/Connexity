package application.model;

import java.io.*;
import java.util.*;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;



import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class AmazonOffer {
	private static Map uris = new HashMap();
	public String ASIN = null;
	public URL url = null;
	public String manufacturer = null;
	public String title = null;
	public List<URL> images = null;
	public String sku = null;
    public String upc = null;
    public String description = "";
    public String price = null;

	public AmazonOffer() throws MalformedURLException, NullPointerException {
		
	}

	static public List<AmazonOffer> parseString(String input) throws IOException, NullPointerException {
		if(input == null){
			return null;
		}
		Document doc = null;
		List<Node> nodes = new ArrayList();
		List<AmazonOffer> result = null;
        try {
        		result = new ArrayList<>();
        		doc = DocumentHelper.parseText(input);

        		//setting namespaces
        		uris.put("ns", "http://webservices.amazon.com/AWSECommerceService/2011-08-01");
        		XPath xpath = doc.createXPath("//ns:Items/ns:Item");
        		xpath.setNamespaceURIs(uris);
        		nodes = xpath.selectNodes(doc);
        }
        	catch (Exception e) {
        		nodes = null;
        	}
        if (nodes != null) {
        		for (Node node : nodes){
        			AmazonOffer thisOffer = new AmazonOffer();
        			//ASIN
        			XPath ASINXpath = node.createXPath("./ns:ASIN");
        			ASINXpath.setNamespaceURIs(uris);
        			Node ASINNode = ASINXpath.selectSingleNode(node);
        			thisOffer.ASIN = ASINNode.getText();
        			//url
        			XPath urlXpath = node.createXPath("./ns:DetailPageURL");
        			urlXpath.setNamespaceURIs(uris);
        			Node urlNode = urlXpath.selectSingleNode(node);
        			thisOffer.url = new URL(urlNode.getText());
        			//manufacturer
        			try {
        				XPath manuXpath = node.createXPath("./ns:ItemAttributes/ns:Manufacturer");
        				manuXpath.setNamespaceURIs(uris);
        				Node manuNode = manuXpath.selectSingleNode(node);
        				thisOffer.manufacturer = manuNode.getText();
        			}
        			catch (Exception e) {
        				thisOffer.manufacturer = null;
        			}
        			//title
        			try{
        				XPath titleXpath = node.createXPath("./ns:ItemAttributes/ns:Title");
        				titleXpath.setNamespaceURIs(uris);
        				Node titleNode = titleXpath.selectSingleNode(node);
        				thisOffer.title = titleNode.getText();
        			}
        			catch (Exception e) {
        				thisOffer.title = null;
        			}
        			//SmallImage
        			try {
        				XPath siXpath = node.createXPath("./ns:SmallImage");
        				siXpath.setNamespaceURIs(uris);
        				Node siNode = siXpath.selectSingleNode(node);
        				thisOffer.images.add(new URL(siNode.getText()));
        			}
        			catch (Exception e) {
        				
        			}
        			//MediumImage
        			try {
        				XPath miXpath = node.createXPath("./ns:MediumImage");
        				miXpath.setNamespaceURIs(uris);
        				Node miNode = miXpath.selectSingleNode(node);
        				thisOffer.images.add(new URL(miNode.getText()));
        			}
        			catch (Exception e) {
        				
        			}
        			//LargeImage
        			try {
        				XPath liXpath = node.createXPath("./ns:LargeImage");
        				liXpath.setNamespaceURIs(uris);
        				Node liNode = liXpath.selectSingleNode(node);
        				thisOffer.images.add(new URL(liNode.getText()));
        			}
        			catch (Exception e) {
        				
        			}
        			//SKU
        			try {
        				XPath skuXpath = node.createXPath("./ns:ItemAttributes/ns:SKU");
        				skuXpath.setNamespaceURIs(uris);
        				Node skuNode = skuXpath.selectSingleNode(node);
        				thisOffer.sku = skuNode.getText();
        			}
        			catch (Exception e) {
        				thisOffer.sku = null;
        			}
        			//UPC
        			try {
        				XPath upcXpath = node.createXPath("./ns:ItemAttributes/ns:UPC");
        				upcXpath.setNamespaceURIs(uris);
        				Node upcNode = upcXpath.selectSingleNode(node);
        				thisOffer.upc = upcNode.getText();
        			}
        			catch (Exception e) {
        				thisOffer.upc = null;
        			}
        			//description
        			try {
        				XPath desXpath = node.createXPath("./ns:ItemAttributes/ns:Feature");
        				desXpath.setNamespaceURIs(uris);
        				List<Node> desNodes = desXpath.selectNodes(node);//!!!multiple nodes!!!
        				Iterator desIt = desNodes.iterator();
        				while (desIt.hasNext()) {
        					Node oneDes = (Node) desIt.next();
        					thisOffer.description = thisOffer.description + oneDes.getText() + ". ";
        				}
        			}
        			catch (Exception e) {
        				thisOffer.description = null;
        			}
        			//price
        			try {
        				XPath priceXpath = node.createXPath("./ns:ItemAttributes/ns:ListPrice/ns:FormattedPrice");
        				priceXpath.setNamespaceURIs(uris);
        				Node priceNode = priceXpath.selectSingleNode(node);
        				thisOffer.price = priceNode.getText();
        			}
        			catch (Exception e) {
        				thisOffer.price = null;
        			}
        			result.add(thisOffer);
        		}
        }
        		return result;
        }
        
	}
