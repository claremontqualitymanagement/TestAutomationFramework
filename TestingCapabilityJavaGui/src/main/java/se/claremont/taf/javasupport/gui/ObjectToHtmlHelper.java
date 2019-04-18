package se.claremont.taf.javasupport.gui;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ObjectToHtmlHelper {

    static Set<Class<?>> primitiveClassesSet = new HashSet<>(Arrays.asList(
            String.class,
            Integer.class,
            int.class,
            boolean.class,
            Boolean.class,
            Date.class));

    private static String invokeDeclaredToHtmlMethodIfExist(Object object) {
        Class c = object.getClass();
        for (Method method : c.getDeclaredMethods()) {
            if (method.getName().equals("toHtml") && method.getReturnType().equals(String.class)) {
                try {
                    return (String) method.invoke(object);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private static boolean returnAsPureText(Object object) {
        return primitiveClassesSet.contains(object.getClass());
    }

    public static String styleInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("<style>").append(System.lineSeparator());
        sb.append("  div.data   { color: darkgrey; }").append(System.lineSeparator());
        sb.append("  span.classname   { font-weight: bold; }").append(System.lineSeparator());
        sb.append("</style>").append(System.lineSeparator());
        return sb.toString();
    }

    public static String toHtmlPage(Object object) {
        return "<html>" + System.lineSeparator()
                + "   <head>"
                + "<link rel=\"stylesheet\" href=\"https://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.css\">" + System.lineSeparator()
                + "<script src=\"https://code.jquery.com/jquery-1.11.3.min.js\"></script>" + System.lineSeparator()
                + "<script src=\"https://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.js\"></script>" + System.lineSeparator()
                + styleInfo()
                + "   </head>" + System.lineSeparator()
                + "   <body>" + System.lineSeparator()
                + "      <div data-role=\"main\" class=\"ui-content\">" + System.lineSeparator()
                + toHtml(object, 1)
                + "      </div>" + System.lineSeparator()
                + "   </body>" + System.lineSeparator()
                + "</html>" + System.lineSeparator();
    }

    private static String tab(int depth) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            sb.append("   ");
        }
        return sb.toString();
    }

    public static String toHtml(Object object, int depth) {
        if (object == null) return "";
        if (depth > 2) return "";
        if (returnAsPureText(object)) {
            String returnString = "";
            try {
                returnString = tab(depth) + (String) object.toString().replace(System.lineSeparator(), "<br>" + System.lineSeparator());
            } catch (Exception e) {
                returnString = tab(depth) + e.getMessage().replace(System.lineSeparator(), "<br>") + System.lineSeparator();
            }
            return returnString;
        }
        String html = invokeDeclaredToHtmlMethodIfExist(object); //If the element type has a toHtml() method, use this.
        if (html != null) return html;
        StringBuilder sb = new StringBuilder();
        if (Collection.class.isAssignableFrom(object.getClass())) { //If the object is a collection
            for (Object o : (Collection) object) {
                sb.append(toHtml(o, depth)).append(System.lineSeparator());
            }
        } else if (Map.class.isAssignableFrom(object.getClass())) { //If the object is a map
            try{
                for (java.lang.Object o : ((Map) object).keySet()) {
                    sb.append(toHtml(((Map) object).get(o), depth)).append(System.lineSeparator());
                }
            }catch (Exception e){
                sb.append(e.toString());
            }
        } else if (object instanceof Object[]) {
            for (Object o : (Object[])object) {
                sb.append(toHtml(o, depth)).append(System.lineSeparator());
            }
        }
        sb.append(tab(depth)).append("<div data-role='collapsible' class='").append(object.getClass().getName().toLowerCase()).append("'>").append(System.lineSeparator());
        sb.append(tab(depth + 1)).append("<h" + depth + ">").append(System.lineSeparator()).append("<span class='classname'>").append(object.getClass().getName()).append("</span></h" + depth + ">").append(System.lineSeparator());
        sb.append(tab(depth + 1)).append("<p>").append(System.lineSeparator());
        sb.append("<table>").append(System.lineSeparator());
        for (Field field : getAllFields(new ArrayList<Field>(), object.getClass())) {
            field.setAccessible(true);
            Object value = null;
            try {
                value = field.get(object);
                if (Collection.class.isAssignableFrom(field.getClass())) { //If the object is a collection
                    for (Object o : (Collection) value) {
                        sb.append(toHtml(o, depth + 1)).append(System.lineSeparator());
                    }
                } else if (Map.class.isAssignableFrom(field.getClass())) { //If the object is a map
                    for (java.lang.Object o : ((Map) value).keySet()) {
                        sb.append(toHtml(((Map) object).get(o), depth + 1)).append(System.lineSeparator());
                    }
                } else if (field.getClass().isArray()) {
                    Object[] array = (Object[]) value;
                    for (Object o : array) {
                        sb.append(toHtml(o, depth + 1)).append(System.lineSeparator());
                    }
                } else {
                    sb.append(tab(depth + 1)).append("<tr><td>").append(field.getName()).append("</td><td>").append(toHtml(value, depth + 1)).append("</td></tr>").append(System.lineSeparator());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        sb.append("</table>").append(System.lineSeparator());
        sb.append(tab(depth + 1)).append("</p>").append(System.lineSeparator());
        sb.append(tab(depth)).append("</div>").append(System.lineSeparator());
        return sb.toString();
    }


    public static List<Field> getAllFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));

        if (type.getSuperclass() != null) {
            getAllFields(fields, type.getSuperclass());
        }

        return fields;
    }
}
