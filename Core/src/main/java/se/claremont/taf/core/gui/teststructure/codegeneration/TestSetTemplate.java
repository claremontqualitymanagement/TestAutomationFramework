package se.claremont.taf.core.gui.teststructure.codegeneration;

import se.claremont.taf.core.gui.guistyle.*;
import se.claremont.taf.core.gui.teststructure.SubProcedureTestStep;
import se.claremont.taf.core.gui.teststructure.TestStep;
import se.claremont.taf.core.support.SupportMethods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

public class TestSetTemplate {

    Set<String> imports = new HashSet<>();
    Set<String> classVariableDeclarations = new HashSet<>();
    Set<String> beginTestSectionDeclarations = new HashSet<>();
    Set<String> classInitializeDeclarations = new HashSet<>();
    Set<String> classTeardownDeclarations = new HashSet<>();
    Set<String> testTeardownDeclarations = new HashSet<>();
    Set<String> testCases = new HashSet<>();
    String testClassName = "";

    public TestSetTemplate(String testClassName) {
        this.testClassName = testClassName;
        imports.add("import org.junit.Test;");
        imports.add("import se.claremont.autotest.common.testset.TestSet;");
    }

    public void makeSureRequiredImportIsAdded(String importString) {
        imports.add(importString);
    }

    public void makeSureClassVariableIsDeclared(String variableDeclaration) {
        classVariableDeclarations.add(variableDeclaration);
    }

    public void makeSureBeginTestSectionDeclarationExist(String declaration) {
        beginTestSectionDeclarations.add(declaration);
    }

    public void makeSureClassInitializeSectionDeclarationExist(String declaration) {
        classInitializeDeclarations.add(declaration);
    }

    public void makeSureClassTeardownSectionDeclarationExist(String declaration) {
        classTeardownDeclarations.add(declaration);
    }

    public void makeSureTestTeardownSectionDeclarationExist(String declaration) {
        testTeardownDeclarations.add(declaration);
    }


    public void addTestCodeFromTestSteps(SubProcedureTestStep testStepList) {
        StringBuilder sb = new StringBuilder();
        sb.append("@Test").append(System.lineSeparator());
        sb.append("   public void " + testStepList.getName().replace(" ", "")).append("() {").append(System.lineSeparator());
        for (TestStep testStep : testStepList.testSteps) {
            sb.append("      ").append(testStep.asCode()).append(System.lineSeparator());
        }
        sb.append("   }").append(System.lineSeparator()).append(System.lineSeparator());
        testCases.add(sb.toString());
    }

    public void displayInFrame() {
        TafFrame frame = new TafFrame("TAF - Test Set code suggestion");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        TafButton saveButton = new TafButton("Save");
        TafCloseButton closeButton = new TafCloseButton(frame);
        TafTextArea textArea = new TafTextArea("CodeTeztArea");
        JScrollPane scrollPane = new JScrollPane(textArea);
        TafPanel buttonPanel = new TafPanel("ButtonPanel");
        textArea.setText(asCode());
        textArea.setFont(new Font("monospaced", Font.PLAIN, AppFont.getInstance().getSize()));
        textArea.setBackground(Color.white);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                int retrival = chooser.showSaveDialog(null);
                if (retrival == JFileChooser.APPROVE_OPTION) {
                    String fileName = chooser.getSelectedFile().getAbsolutePath();
                    if (!fileName.endsWith(".java")) fileName = fileName + ".java";
                    SupportMethods.saveToFile(textArea.getText(), fileName);
                }
                frame.setVisible(false);
                frame.dispose();
            }
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(closeButton);
        frame.getContentPane().add(scrollPane);
        frame.getContentPane().add(buttonPanel);
        GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
        frame.getContentPane().setLayout(groupLayout);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setHorizontalGroup(
                groupLayout.createSequentialGroup()
                        .addGroup(groupLayout.createParallelGroup()
                                .addComponent(scrollPane)
                                .addComponent(buttonPanel)
                        )
        );
        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                        .addComponent(scrollPane)
                        .addComponent(buttonPanel)
        );

        frame.pack();
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize().width * 2/3, Toolkit.getDefaultToolkit().getScreenSize().height * 2/3);
        if (frame.getSize().width >= Toolkit.getDefaultToolkit().getScreenSize().width) {
            frame.setSize(Toolkit.getDefaultToolkit().getScreenSize().width * 3 / 4, frame.getHeight());
            frame.revalidate();
        }
        if (frame.getSize().height >= Toolkit.getDefaultToolkit().getScreenSize().height) {
            frame.setSize(frame.getWidth(), Toolkit.getDefaultToolkit().getScreenSize().height * 2 / 3);
            frame.revalidate();
        }
        frame.setVisible(true);
    }

    public String asCode() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.join(System.lineSeparator(), imports)).append(System.lineSeparator()).append(System.lineSeparator());
        sb.append("public class ").append(testClassName).append(" extends TestSet {").append(System.lineSeparator()).append(System.lineSeparator());

        if (classVariableDeclarations.size() > 0) {
            sb.append("   ");
            sb.append(String.join(System.lineSeparator() + "   ", classVariableDeclarations));
            sb.append(System.lineSeparator()).append(System.lineSeparator());
        }

        if (beginTestSectionDeclarations.size() > 0) {
            sb.append("   @Begin").append(System.lineSeparator());
            sb.append("   public void startup(){").append(System.lineSeparator()).append("      ");
            sb.append(String.join(System.lineSeparator() + "      ", beginTestSectionDeclarations));
            sb.append(System.lineSeparator()).append("   }").append(System.lineSeparator()).append(System.lineSeparator());
        }

        if (classInitializeDeclarations.size() > 0) {
            sb.append("   @BeginClass").append(System.lineSeparator());
            sb.append("   public static void classStartUp(){").append(System.lineSeparator()).append("      ");
            sb.append(String.join(System.lineSeparator() + "      ", classInitializeDeclarations));
            sb.append(System.lineSeparator()).append("   }").append(System.lineSeparator()).append(System.lineSeparator());
        }

        if (classTeardownDeclarations.size() > 0) {
            sb.append("   @EndClass").append(System.lineSeparator());
            sb.append("   public static void classTearDown(){").append(System.lineSeparator()).append("      ");
            sb.append(String.join(System.lineSeparator() + "      ", classTeardownDeclarations));
            sb.append(System.lineSeparator()).append("   }").append(System.lineSeparator()).append(System.lineSeparator());
        }

        if (testTeardownDeclarations.size() > 0) {
            sb.append("   @EndTest").append(System.lineSeparator());
            sb.append("   public void tearWodn(){").append(System.lineSeparator()).append("      ");
            sb.append(String.join(System.lineSeparator() + "      ", testTeardownDeclarations));
            sb.append(System.lineSeparator()).append("   }").append(System.lineSeparator()).append(System.lineSeparator());
        }

        if (testCases.size() > 0) {
            sb.append(String.join(System.lineSeparator() + "      ", testCases));
            sb.append(System.lineSeparator()).append(System.lineSeparator());
        }

        sb.append("}");

        return sb.toString();

    }
}
