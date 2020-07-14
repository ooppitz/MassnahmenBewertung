/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.azubiag.MassnahmenBewertung.htmlcreator;

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
              ArrayList<String> referentenListe = new ArrayList<>();
              referentenListe.add("Pfaffelhuber");
              referentenListe.add("Werner");
              HtmlCreator htmlcreator = new HtmlCreator(referentenListe, "C:\\Users\\oliveroppitz\\git\\MassnahmenBewertung\\src\\main\\resources\\de\\azubiag\\MassnahmenBewertung\\template.html", "createdtestfile.html");
              htmlcreator.createHtml();
      
    }
    
}
