package uj.java.w3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class ListMerger {
    public static List<Object> mergeLists(List<?> l1, List<?> l2) {

        if (l1 != null) {

            if (l2 == null)
                return Collections.unmodifiableList(l1);

            List<Object> objectList = new ArrayList<>(l1.size() + l2.size());

            ListIterator<?> l1Iterator = l1.listIterator();
            ListIterator<?> l2Iterator = l2.listIterator();

            while (l1Iterator.hasNext() && l2Iterator.hasNext()) {
                objectList.add(l1Iterator.next());
                objectList.add(l2Iterator.next());
            }

            while (l1Iterator.hasNext())
                objectList.add(l1Iterator.next());

            while (l2Iterator.hasNext())
                objectList.add(l2Iterator.next());

            return Collections.unmodifiableList(objectList);

        } else if (l2 != null)
            return Collections.unmodifiableList(l2);
        else
            return List.of();
    }
}