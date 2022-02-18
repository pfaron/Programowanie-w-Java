package uj.java.annotations;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SupportedAnnotationTypes({"uj.java.annotations.MyComparable","uj.java.annotations.ComparePriority"})
public class MyProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(MyComparable.class);
        annotatedElements.forEach(this::processElement);

        return true;
    }

    private void processElement(Element e) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Processing " + e);
        var clazz = (TypeElement) e;
        var className = clazz.getQualifiedName().toString();
        try {
            JavaFileObject file = processingEnv.getFiler().createSourceFile(className + "Comparator");

            try (PrintWriter out = new PrintWriter(file.openWriter())) {
                var packageName = packageName(className);
                if (packageName != null) {
                    out.write("package " + packageName + ";\n\n");
                }

                out.write("class " + clazz.getSimpleName().toString() + "Comparator {\n");

                out.write("    public int compare(" + className + " first, " + className + " second) {\n");

                out.write("        int result;\n\n");

                for (var element : getNonPrivatePrimitiveFields(e)) {
                    if (element.asType().getKind().isPrimitive() && !(element.getModifiers().contains(Modifier.PRIVATE))) {
                        var variableElement = (VariableElement) element;
                        var typeElement = processingEnv.getTypeUtils().boxedClass(processingEnv.getTypeUtils().getPrimitiveType(variableElement.asType().getKind()));
                        var varType = typeElement.getSimpleName().toString();
                        var varName = variableElement.getSimpleName().toString();
                        out.write("        result = " + varType + ".compare(first." + varName + ", second." + varName + ");\n");
                        out.write("        if (result != 0) return result;\n\n");
                    }
                }

                out.write("        return 0;\n");

                out.write("    }\n");
                out.write("}");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private List<Element> getNonPrivatePrimitiveFields(Element e){
        List<Element> elements = new ArrayList<>();

        for (var element : e.getEnclosedElements()) {
            if (element.getKind() == ElementKind.FIELD && element.asType().getKind().isPrimitive()
                    && !element.getModifiers().contains(Modifier.PRIVATE)) {
                elements.add(element);
            }
        }
        elements.sort(new ElementComparator());
        return elements;
    }

    private String packageName(String className) {
        String packageName = null;
        int lastDot = className.lastIndexOf('.');
        if (lastDot > 0) {
            packageName = className.substring(0, lastDot);
        }
        return packageName;
    }

    public static class ElementComparator implements Comparator<Element> {
        @Override
        public int compare(Element o1, Element o2) {
            ComparePriority p1 = o1.getAnnotation(ComparePriority.class);
            ComparePriority p2 = o2.getAnnotation(ComparePriority.class);

            if (p1 == null) {
                if (p2 == null)
                    return 0;
                return 1;
            }
            if (p2 == null)
                return -1;
            return Integer.compare(p1.value(), p2.value());
        }
    }
}