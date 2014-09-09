package cz.ladicek.annDocuGen.annotationProcessor;

import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.Trees;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

public class CompilerTreeApiBasedFieldInitializerDiscovery implements FieldInitializerDiscovery {
    private final ProcessingEnvironment processingEnv;

    public CompilerTreeApiBasedFieldInitializerDiscovery(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    @Override
    public String getFor(Element field) {
        try {
            return doGetFor(field);
        } catch (Exception e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING,
                    "Couldn't read field initializer, ignoring", field);
            return null;
        }
    }

    private String doGetFor(Element field) {
        Trees trees = Trees.instance(processingEnv);
        VariableTree fieldNode = (VariableTree) trees.getTree(field);
        ExpressionTree initializer = fieldNode.getInitializer();
        return initializer != null ? initializer.toString() : null;
    }
}
