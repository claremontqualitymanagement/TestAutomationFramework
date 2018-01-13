package se.claremont.autotest;

import se.claremont.autotest.common.gui.guistyle.*;
import se.claremont.autotest.common.support.SupportMethods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class MavenVersionUpdater {

    static String newVersion = null;
    static String baseFolder = System.getProperty("user.dir");

    public static void main(String[] args){

        if(args.length > 0) {
            newVersion = args[0];
            updateVersion();
        } else {
            String currentVersion = getCurrentVersion();
            TafFrame window = new TafFrame("TAF - Set new version in pom files");
            window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            TafLabel currentVersionLabel = new TafLabel("Version: ");
            TafLabel currentVersionNumberLabel =  new TafLabel(getCurrentVersion());
            TafLabel newVersionLabel = new TafLabel("New version:");
            TafTextField newVersionTextField = new TafTextField(" < new version >");
            TafButton updateButton = new TafButton("Update");
            updateButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(newVersionTextField.isChangedFromDefault() && newVersionTextField.getText().length() > 0){
                        newVersion = newVersionTextField.getText();
                        window.setVisible(false);
                        window.dispose();
                        updateVersion();
                    } else {
                        System.out.println("No action");
                    }
                }
            });
            TafCloseButton cancelButton = new TafCloseButton(window);
            window.getContentPane().setLayout(new GridLayout(3, 2));
            window.getContentPane().add(currentVersionLabel);
            window.getContentPane().add(currentVersionNumberLabel);
            window.getContentPane().add(newVersionLabel);
            window.getContentPane().add(newVersionTextField);
            window.getContentPane().add(cancelButton);
            window.getContentPane().add(updateButton);
            window.pack();
            window.setVisible(true);
        }
        System.out.println("No action performed.");
    }

    private static void updateVersion(){
        if(newVersion == null || newVersion.length() == 0) return;
        System.out.println("New version: '" + newVersion + "'.");
        String pathToMainPomFile = baseFolder + File.separator + "pom.xml";
        replaceLineInFile(pathToMainPomFile, " *<artifactId>TestAutomationFramework</artifactId>\n" +
                " *<version>.*</version>\n" +
                " *<packaging>pom</packaging>\n",
                "    <artifactId>TestAutomationFramework</artifactId>\n" +
                "    <version>" + newVersion + "</version>\n" +
                "    <packaging>pom</packaging>\n");
        replaceLineInFile(pathToMainPomFile, "<global.version>.*</global.version>", "<global.version>" + newVersion + "</global.version>");

    }

    private static void replaceLineInFile(String path, String regexPatternOfFile, String replacement){
        File file = new File(path);
        if(file == null || !file.exists())return;
        java.util.List<String> lines = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                if(line.matches(regexPatternOfFile)) {
                    line = replacement;
                }
                lines.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        SupportMethods.saveToFile(String.join(System.lineSeparator(), lines), path);
    }

    private static String getCurrentVersion() {
        File mainPom = new File(baseFolder + File.separator + "pom.xml");
        System.out.println("Folder:" + mainPom.getAbsolutePath());
        String versionRow = findRow(mainPom, ".*<global.version>.*</global.version>.*");
        versionRow = versionRow.trim().replace("<global.version>", "").replace("</global.version>", "");
        return versionRow;
    }

    private static String findRow(File file, String regex) {
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                if(line.matches(regex)) return line;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
