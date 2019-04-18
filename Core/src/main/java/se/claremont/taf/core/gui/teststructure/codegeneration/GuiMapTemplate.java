package se.claremont.taf.core.gui.teststructure.codegeneration;

import java.util.HashSet;
import java.util.Set;

public class GuiMapTemplate {
    private Set<String> importStrings = new HashSet<>();
    private String windowDeclaration = "";
    private Set<String> elementDeclarations = new HashSet<>();
    private String className = "";

    public GuiMapTemplate(String className){
        this.className = className;
    }

    public void addWindowDeclaration(String windowDeclaration){
        this.windowDeclaration = windowDeclaration;
    }

    public void makeSureRequiredImportIsAdded(String imports){
        importStrings.add(imports);
    }

    public void makeSureElementDeclarationIsEntered(String elementDeclaration){
        elementDeclarations.add(elementDeclaration);
    }

    public String asCode(){
        StringBuilder sb = new StringBuilder();
        sb.append(String.join(System.lineSeparator(), importStrings)).append(System.lineSeparator()).append(System.lineSeparator());
        sb.append("public class ").append(className).append(" {").append(System.lineSeparator()).append(System.lineSeparator());
        sb.append(windowDeclaration);
        for(String elementDeclaration : elementDeclarations){
            sb.append(elementDeclaration).append(System.lineSeparator());
        }
        sb.append("}");
        return sb.toString();
    }
}
