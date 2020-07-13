/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.azubiag.Massnahmenbewertung.htmlcreator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author manuel.unverdorben
 */
public class HtmlTest {
    
    
    
    public static void main(String[] args) throws IOException {
              String currentDirectory = System.getProperty("user.dir");
              ArrayList<String> liste = new ArrayList<>();
              liste.add("mich");
              liste.add("du");
              HtmlCreator htmlcreator = new HtmlCreator(liste, "mockup.html", "test.html");
              htmlcreator.createHtml();
       
       
        
        
       
       
      
      

    }
    
}
