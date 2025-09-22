package org.novasparkle.lunaspring.API.util.utilities.reflection;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;
import org.novasparkle.lunaspring.API.configuration.Manager;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.util.Set;

@AutoService(Processor.class)
@SupportedAnnotationTypes("org.novasparkle.lunaspring.API.configuration.Manager")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ConfigManagerProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Manager.class)) {
            if (element.getKind().isClass()) {
                generateConfigManager((TypeElement) element);
            }
        }
        return true;
    }

    private void generateConfigManager(TypeElement annotatedClass) {
        try {
            Manager configManager = annotatedClass.getAnnotation(Manager.class);

            // Создаем статический блок инициализации
            CodeBlock staticBlock = CodeBlock.builder()
                    .addStatement("configuration = new IConfig($L.getInstance().getDataFolder().getParentFile(), $S)",
                            configManager.plugin(),
                            configManager.plugin() + "/" + configManager.path()).build();

            // Создаем класс с модификаторами исходного класса
            TypeSpec.Builder classBuilder = TypeSpec.classBuilder(annotatedClass.getSimpleName().toString())
                    .addModifiers(annotatedClass.getModifiers().toArray(new Modifier[0]))
                    .addField(FieldSpec.builder(
                                    ClassName.get("", "IConfig"),
                                    "configuration",
                                    Modifier.PUBLIC, Modifier.STATIC)
                            .build())
                    .addStaticBlock(staticBlock);

            // Добавляем методы делегирования
            TypeElement configElement = processingEnv.getElementUtils().getTypeElement(processingEnv.getElementUtils().getModuleElement("org.novasparkle.lunaspring.API.configuration"), "IConfig");
            for (Element enclosed : configElement.getEnclosedElements()) {
                if (enclosed.getKind() == ElementKind.METHOD &&
                        enclosed.getModifiers().contains(Modifier.PUBLIC) &&
                        !enclosed.getModifiers().contains(Modifier.STATIC)) {

                    addDelegateMethod(classBuilder, (ExecutableElement) enclosed);
                }
            }

            // Создаем JavaFile
            JavaFile javaFile = JavaFile.builder(
                    processingEnv.getElementUtils().getPackageOf(annotatedClass).toString(),
                    classBuilder.build()
            ).build();

            // Записываем файл
            JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(
                    javaFile.packageName + "." + annotatedClass.getSimpleName());
            try (var writer = sourceFile.openWriter()) {
                javaFile.writeTo(writer);
            }

        } catch (IOException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                    "Не удалось сгенерировать ConfigManager: " + e.getMessage());
        }
    }

    private void addDelegateMethod(TypeSpec.Builder classBuilder, ExecutableElement method) {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(method.getSimpleName().toString())
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(TypeName.get(method.getReturnType()));

        // Добавляем параметры
        for (VariableElement parameter : method.getParameters()) {
            methodBuilder.addParameter(
                    ParameterSpec.builder(
                            TypeName.get(parameter.asType()),
                            parameter.getSimpleName().toString()
                    ).build());
        }

        // Добавляем аннотации
        for (AnnotationMirror annotation : method.getAnnotationMirrors()) {
            methodBuilder.addAnnotation(AnnotationSpec.get(annotation));
        }

        // Создаем вызов метода
        StringBuilder call = new StringBuilder("configuration." + method.getSimpleName() + "(");
        for (int i = 0; i < method.getParameters().size(); i++) {
            if (i > 0) call.append(", ");
            call.append(method.getParameters().get(i).getSimpleName());
        }
        call.append(")");

        if (method.getReturnType().toString().equals("void")) {
            methodBuilder.addStatement(call.toString());
        } else {
            methodBuilder.addStatement("return " + call);
        }

        classBuilder.addMethod(methodBuilder.build());
    }
}
