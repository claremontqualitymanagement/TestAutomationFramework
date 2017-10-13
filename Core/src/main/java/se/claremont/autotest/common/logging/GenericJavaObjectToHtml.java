package se.claremont.autotest.common.logging;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

public class GenericJavaObjectToHtml {

    public static String toHtml(Object object){
        return toHtml(object, 1);
    }

    public static String toHtml(Object object, int depth){
        if(object == null) return "<span class='nullobject'><i>null</i></span>";
        if(returnAsPureText(object)) return tab(depth) + (String)object.toString().replace(System.lineSeparator(), "<br>" + System.lineSeparator()) ;
        String html = invokeDeclaredToHtmlMethodIfExist(object); //If the element type has a toHtml() method, use this.
        if(html != null) return html;
        StringBuilder sb = new StringBuilder();
        sb.append(tab(depth)).append("<div data-role='collapsible' class='").append(object.getClass().getSimpleName().toLowerCase()).append("'>").append(System.lineSeparator());
        sb.append(tab(depth + 1)).append("<h" + depth + ">").append(System.lineSeparator()).append("<span class='classname'>").append(object.getClass().getSimpleName()).append("</span></h" + depth + ">").append(System.lineSeparator());
        sb.append(tab(depth + 1)).append("<p>").append(System.lineSeparator());
        sb.append("<table>").append(System.lineSeparator());
        for(Field field : object.getClass().getDeclaredFields()){
            field.setAccessible(true);
            Object value = null;
            try {
                value = field.get(object);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if(Collection.class.isAssignableFrom(object.getClass())){ //If the object is a collection
                for(Object o : (Collection)object){
                    sb.append(toHtml(o, depth + 1)).append(System.lineSeparator());
                }
            } else if(Map.class.isAssignableFrom(object.getClass())){ //If the object is a map
                for(java.lang.Object o : ((Map) object).keySet()){
                    sb.append(toHtml(((Map)object).get(o), depth + 1)).append(System.lineSeparator());
                }
            } else if(object.getClass().isArray()){
                Object[] array = (Object[])object;
                for(Object o : array){
                    sb.append(toHtml(o, depth + 1)).append(System.lineSeparator());
                }
            } else {
                sb.append(tab(depth + 1)).append("<tr><td>").append(field.getName()).append("</td><td>").append(toHtml(value, depth + 1)).append("</td></tr>").append(System.lineSeparator());
            }
        }
        sb.append("</table>").append(System.lineSeparator());
        sb.append(tab(depth + 1)).append("</p>").append(System.lineSeparator());
        sb.append(tab(depth)).append("</div>").append(System.lineSeparator());
        return sb.toString();
    }

    public static String toHtmlPage(Object object){
        return "<html>" + System.lineSeparator()
                + "   <head>" + System.lineSeparator()
                + "      <link rel=\"stylesheet\" href=\"https://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.css\">" + System.lineSeparator()
                + "      <script src=\"https://code.jquery.com/jquery-1.11.3.min.js\"></script>" + System.lineSeparator()
                + "      <script src=\"https://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.js\"></script>" + System.lineSeparator()
                + "   </head>" + System.lineSeparator()
                + "   <body>" + System.lineSeparator()
                + "      <div data-role=\"main\" class=\"ui-content\">" + System.lineSeparator()
                + toHtml(object, 1) + System.lineSeparator()
                + "      </div>" + System.lineSeparator()
                + "   </body>" + System.lineSeparator()
                + "</html>" + System.lineSeparator();
    }


    private static String invokeDeclaredToHtmlMethodIfExist(Object object){
        Class c = object.getClass();
        for (Method method : c.getDeclaredMethods()) {
            if(method.getName().equals("toHtml") && method.getReturnType().equals(String.class)){
                try {
                    return (String)method.invoke(object);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private static boolean returnAsPureText(Object object){
        for(String className : new String[] {
                String.class.getSimpleName(),
                Integer.class.getSimpleName(),
                int.class.getSimpleName(),
                boolean.class.getSimpleName(),
                Boolean.class.getSimpleName(),
                Date.class.getSimpleName(),
                char.class.getSimpleName(),
                float.class.getSimpleName(),
                byte.class.getSimpleName(),
                short.class.getSimpleName(),
                long.class.getSimpleName(),
                double.class.getSimpleName()
        }){
            if(object.getClass().getSimpleName().equals(className)) return true;
        }
        return false;    }


    private static String tab(int depth){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < depth ; i++){
            sb.append("   ");
        }
        return sb.toString();
    }

}