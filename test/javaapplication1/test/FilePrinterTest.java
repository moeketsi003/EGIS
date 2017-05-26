/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1.test;

import javaapplication1.FilePrinter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Mega
 */
public class FilePrinterTest {
    private FilePrinter filePrinter;
    private String url;
    private String json;
    
    @Before
    public void setUp() {
        filePrinter = new FilePrinter();
        url = "https://raw.githubusercontent.com/egis/handbook/master/Tech-Stack.md";
        json = filePrinter.getFileContents(url);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testFilePrinter(){
        assertNotNull("File Printer is null", filePrinter);
        assertNotNull("JSON not built", json);
        
        System.out.println(json);
    }
}
