package co.uk.isxander.skyclient.installer.start;

import co.uk.isxander.skyclient.installer.SkyClient;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.io.PrintStream;

public class Main {

    public static void main(String[] args) throws Exception {
        PrintStream fileOut = new PrintStream("./skyclient.log");
        System.setOut(fileOut);

        SkyClient.LOGGER.info("Setting Look and Feel...");
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }



        SkyClient.getInstance();
    }

}
