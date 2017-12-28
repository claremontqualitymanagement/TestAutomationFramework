package se.claremont.autotest.common.gui.teststructure.codegeneration;

import se.claremont.autotest.common.gui.teststructure.SubProcedureTestStep;
import se.claremont.autotest.common.gui.teststructure.TestStep;

import java.util.HashSet;
import java.util.Set;

public class TestSetTemplate{

    Set<String> imports = new HashSet<>();
    Set<String> classVariableDeclarations = new HashSet<>();
    Set<String> beginTestSectionDeclarations = new HashSet<>();
    Set<String> classInitializeDeclarations = new HashSet<>();
    Set<String> classTeardownDeclarations = new HashSet<>();
    Set<String> testTeardownDeclarations = new HashSet<>();
    Set<String> testCases = new HashSet<>();
    String testClassName = "";

    public TestSetTemplate(String testClassName){
        this.testClassName = testClassName;
        imports.add("import org.junit.Test;");
        imports.add("import se.claremont.autotest.common.testset.TestSet;");
    }

    public void makeSureRequiredImportIsAdded(String importString){
        imports.add(importString);
    }

    public void makeSureClassVariableIsDeclared(String variableDeclaration){
        classVariableDeclarations.add(variableDeclaration);
    }

    public void makeSureBeginTestSectionDeclarationExist(String declaration){
        beginTestSectionDeclarations.add(declaration);
    }

    public void makeSureClassInitializeSectionDeclarationExist(String declaration){
        classInitializeDeclarations.add(declaration);
    }

    public void makeSureClassTeardownSectionDeclarationExist(String declaration){
        classTeardownDeclarations.add(declaration);
    }

    public void makeSureTestTeardownSectionDeclarationExist(String declaration){
        testTeardownDeclarations.add(declaration);
    }


    public void addTestCodeFromTestSteps(SubProcedureTestStep testStepList){
        StringBuilder sb = new StringBuilder();
        sb.append("@Test").append(System.lineSeparator());
        sb.append("   public void " + testStepList.getName().replace(" ", "")).append("() {").append(System.lineSeparator());
        for(TestStep testStep : testStepList.testSteps){
            sb.append("      ").append(testStep.asCode()).append(System.lineSeparator());
        }
        sb.append("   }").append(System.lineSeparator()).append(System.lineSeparator());
        testCases.add(sb.toString());
    }

    public String asCode(){
        StringBuilder sb = new StringBuilder();
        sb.append(String.join(System.lineSeparator(), imports)).append(System.lineSeparator()).append(System.lineSeparator());
        sb.append("public class ").append(testClassName).append(" extends TestSet {").append(System.lineSeparator()).append(System.lineSeparator());

        if(classVariableDeclarations.size() > 0){
            sb.append("   ");
            sb.append(String.join(System.lineSeparator() + "   ", classVariableDeclarations));
            sb.append(System.lineSeparator()).append(System.lineSeparator());
        }

        if(beginTestSectionDeclarations.size() > 0){
            sb.append("   @Begin").append(System.lineSeparator());
            sb.append("   public void startup(){").append(System.lineSeparator()).append("      ");
            sb.append(String.join(System.lineSeparator() + "      ", beginTestSectionDeclarations));
            sb.append(System.lineSeparator()).append("   }").append(System.lineSeparator()).append(System.lineSeparator());
        }

        if(classInitializeDeclarations.size() > 0){
            sb.append("   @BeginClass").append(System.lineSeparator());
            sb.append("   public static void classStartUp(){").append(System.lineSeparator()).append("      ");
            sb.append(String.join(System.lineSeparator() + "      ", classInitializeDeclarations));
            sb.append(System.lineSeparator()).append("   }").append(System.lineSeparator()).append(System.lineSeparator());
        }

        if(classTeardownDeclarations.size() > 0){
            sb.append("   @EndClass").append(System.lineSeparator());
            sb.append("   public static void classTearDown(){").append(System.lineSeparator()).append("      ");
            sb.append(String.join(System.lineSeparator() + "      ", classTeardownDeclarations));
            sb.append(System.lineSeparator()).append("   }").append(System.lineSeparator()).append(System.lineSeparator());
        }

        if(testTeardownDeclarations.size() > 0){
            sb.append("   @EndTest").append(System.lineSeparator());
            sb.append("   public void tearWodn(){").append(System.lineSeparator()).append("      ");
            sb.append(String.join(System.lineSeparator() + "      ", testTeardownDeclarations));
            sb.append(System.lineSeparator()).append("   }").append(System.lineSeparator()).append(System.lineSeparator());
        }

        if(testCases.size() > 0){
            sb.append("      ");
            sb.append(String.join(System.lineSeparator() + "      ", testCases));
            sb.append(System.lineSeparator()).append(System.lineSeparator());
        }

        sb.append("}");

        return sb.toString();

    }
}
