package com.example.week9;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner theaterdrop, datedrop, clockdrop, moviedrop;
    String valToSet, theaterid, date;
    //String [] movies = {"007 James Bond"};
    String [] dates = {"25.03.2021"};
    String [] clocks = {"18.00"};
    ArrayList<theater> theaters;
    List<String> movielist = new LinkedList<String>();
    List<String> theaterlist = new LinkedList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        theaters = new ArrayList<theater>();
        theaters = readXML(theaters);

        for (int i =0; i < theaters.size(); i++) {
            theaterlist.add(theaters.get(i).getName());
        }

        theaterdrop = (Spinner) findViewById(R.id.theatermenu);
        theaterdrop.setOnItemSelectedListener(this);

        datedrop = (Spinner) findViewById(R.id.datemenu);
        clockdrop = (Spinner) findViewById(R.id.timemenu);
        moviedrop = (Spinner) findViewById(R.id.moviemenu);

        ArrayAdapter<String> theateradapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, theaterlist);
        theateradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        theaterdrop.setAdapter(theateradapter);

        ArrayAdapter<String> dateadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dates);
        dateadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        datedrop.setAdapter(dateadapter);

        ArrayAdapter<String> clockadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, clocks);
        clockadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        clockdrop.setAdapter(clockadapter);

        ArrayAdapter<String> movieadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, movielist);
        movieadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        moviedrop.setAdapter(movieadapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        valToSet = parent.getItemAtPosition(position).toString();
        date = datedrop.getSelectedItem().toString();
        for (int i = 0; i < theaters.size(); i++) {
            if (theaters.get(i).getName() == valToSet){
                theaterid = theaters.get(i).getId();
            }
        }
        movielist = readXMLMovies(movielist, theaterid, date);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public ArrayList<theater> readXML (ArrayList<theater> theaters) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            String urlString = "https://www.finnkino.fi/xml/TheatreAreas/";
            Document doc = builder.parse(urlString);
            doc.getDocumentElement().normalize();
            System.out.println("Root directory: " + doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getDocumentElement().getElementsByTagName("TheatreArea");
            for (int i = 0; i<nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    theater temp = new theater(element.getElementsByTagName("ID").item(0).getTextContent(), element.getElementsByTagName("Name").item(0).getTextContent());
                    theaters.add(temp);
                    System.out.println("Nimi " + theaters.get(i).getName());
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } finally {
            return theaters;
        }
    }

    public List<String> readXMLMovies (List<String> movies, String id, String date) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            String urlString = "https://www.finnkino.fi/xml/Schedule/?area="+ id + "&dt=" + date;
            Document doc = builder.parse(urlString);
            doc.getDocumentElement().normalize();
            System.out.println("Root directory: " + doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getDocumentElement().getElementsByTagName("Show");
            for (int i = 0; i<nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    movies.add(element.getElementsByTagName("Title").item(0).getTextContent());
                    System.out.println("Elokuva " + movies.get(i));
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } finally {
            return movies;
        }
    }
}