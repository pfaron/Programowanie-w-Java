package uj.java.w3;

import java.util.List;
import java.util.Map;

public class DefaultJsonMapper implements JsonMapper {

    private final StringBuilder output = new StringBuilder();

    @Override
    public String toJson(Map<String, ?> map) {

        if (map != null) {
            checkTypeAndCallFunc(map);
            return output.toString();
        }

        return "{}";
    }

    private void checkTypeAndCallFunc(Object obj) {
        if (obj instanceof Map m) {
            printMap(m);
        } else if (obj instanceof List<?>) {
            printList((List<?>) obj);
        } else if (obj instanceof String) {
            printString((String) obj);
        } else if (obj instanceof Number) {
            printNumber((Number) obj);
        } else if (obj instanceof Boolean) {
            printBoolean((Boolean) obj);
        } else {
            System.out.println("Error" + obj.toString() + obj.getClass());
        }
    }

    private void printMap(Map<String, ?> map) {
        output.append("{");

        for (Map.Entry<String, ?> entry : map.entrySet()) {

            printString(entry.getKey());
            output.append(":");
            checkTypeAndCallFunc(entry.getValue());
            output.append(",");

        }

        removeCommaAfterLastElement();

        output.append("}");
    }

    private void printString(String str) {
        str = str.replace("\"", "\\\"");
        output.append("\"").append(str).append("\"");
    }

    private void printNumber(Number num) {
        output.append(num.toString());
    }

    private void printBoolean(Boolean bool) {
        output.append(bool.toString());
    }

    private void printList(List<?> list) {
        output.append("[");

        for (Object elem : list) {
            checkTypeAndCallFunc(elem);
            output.append(",");
        }

        removeCommaAfterLastElement();

        output.append("]");
    }

    private void removeCommaAfterLastElement() {
        if (output.charAt(output.length() - 1) == ',')
            output.deleteCharAt(output.length() - 1);
    }
}
