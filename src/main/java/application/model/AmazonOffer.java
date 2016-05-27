package application.model;

import java.util.*;

import org.dom4j.*;


import java.util.ArrayList;
import java.util.List;


public class AmazonOffer {
	private static Map uris = new HashMap();
	public String ASIN = null;
	public String url = null;
	public String manufacturer = null;
	public String title = null;
	public List<String> images = new ArrayList<>();
	public String sku = null;
    public String upc = null;
    public String description = "";
    public String price = null;

	public AmazonOffer() {}

	static public List<AmazonOffer> parseString(String input) {
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
		catch (NullPointerException e) {
			nodes = null;
		}
		catch (DocumentException e) {
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
        			thisOffer.url = urlNode.getStringValue();
        			//manufacturer
        			try {
        				XPath manuXpath = node.createXPath("./ns:ItemAttributes/ns:Manufacturer");
        				manuXpath.setNamespaceURIs(uris);
        				Node manuNode = manuXpath.selectSingleNode(node);
        				thisOffer.manufacturer = manuNode.getText();
        			}
        			catch (NullPointerException e) {
        				thisOffer.manufacturer = null;
        			}
        			//title
        			try{
        				XPath titleXpath = node.createXPath("./ns:ItemAttributes/ns:Title");
        				titleXpath.setNamespaceURIs(uris);
        				Node titleNode = titleXpath.selectSingleNode(node);
        				thisOffer.title = titleNode.getText();
        			}
        			catch (NullPointerException e) {
        				thisOffer.title = null;
        			}
        			//SmallImage
                    try {
					   XPath siXpath = node.createXPath("./ns:SmallImage/ns:URL");
					   siXpath.setNamespaceURIs(uris);
					   Node siNode = siXpath.selectSingleNode(node);
					   thisOffer.images.add(siNode.getStringValue());
                    }
                    catch (NullPointerException e) {
                        
                    }

        			//MediumImage
        			try {
        				XPath miXpath = node.createXPath("./ns:MediumImage/ns:URL");
        				miXpath.setNamespaceURIs(uris);
        				Node miNode = miXpath.selectSingleNode(node);
        				thisOffer.images.add(miNode.getStringValue());
        			}
        			catch (NullPointerException e) {
        				
        			}
        			//LargeImage
        			try {
        				XPath liXpath = node.createXPath("./ns:LargeImage/ns:URL");
        				liXpath.setNamespaceURIs(uris);
        				Node laNode = liXpath.selectSingleNode(node);
        				thisOffer.images.add(laNode.getStringValue());
        			}
        			catch (NullPointerException e) {
        				
        			}
        			//SKU
        			try {
        				XPath skuXpath = node.createXPath("./ns:ItemAttributes/ns:SKU");
        				skuXpath.setNamespaceURIs(uris);
        				Node skuNode = skuXpath.selectSingleNode(node);
        				thisOffer.sku = skuNode.getText();
        			}
        			catch (NullPointerException e) {
        				thisOffer.sku = null;
        			}
        			//UPC
        			try {
        				XPath upcXpath = node.createXPath("./ns:ItemAttributes/ns:UPC");
        				upcXpath.setNamespaceURIs(uris);
        				Node upcNode = upcXpath.selectSingleNode(node);
        				thisOffer.upc = upcNode.getText();
        			}
        			catch (NullPointerException e) {
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
        			catch (NullPointerException e) {
        				thisOffer.description = null;
        			}
        			//price
        			try {
        				XPath priceXpath = node.createXPath("./ns:ItemAttributes/ns:ListPrice/ns:FormattedPrice");
        				priceXpath.setNamespaceURIs(uris);
        				Node priceNode = priceXpath.selectSingleNode(node);
        				thisOffer.price = priceNode.getText();
        			}
        			catch (NullPointerException e) {
        				thisOffer.price = null;
        			}
        			result.add(thisOffer);
        		}
        }
        		return result;
        }
        
	}
