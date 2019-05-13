package com.example

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class ListClassesTask extends DefaultTask {

    String rootDir
    String packageName
    List<Integer> sizes
    ListType type

    @TaskAction
    void createBenchmark() {
        validate()
        def classes = listClasses()
        classes.each { klass ->
            def file = project.file(klass.path())
            def dir = file.parentFile
            if (!dir.exists()) dir.mkdirs()
            file.write(klass.classContents(), 'UTF-8')
            ListOperation.get().each { op ->
                def o = new SizedListOperationClass(operation: op, parentClass: klass)
                def opFile = project.file(o.path())
                println(opFile)
                opFile.write(o.classContents(), 'UTF-8')
            }
        }
    }

    void validate() {
        if (rootDir == null)
            throw new IllegalArgumentException('provide rootDir')
        if (!rootDir.matches('[a-zA-Z]\\p{Alnum}*'))
            throw new IllegalArgumentException('invalid root directory name')
        if (packageName == null || !packageName.matches('[a-zA-Z]\\p{Alnum}*[.[a-zA-Z]\\p{Alnum}]*'))
            throw new IllegalArgumentException(packageName == null? 'provide packageName' : 'invalid packageName')
        if (sizes == null) 
            throw new IllegalArgumentException('provide sizes')
        if (sizes.empty)
            throw new IllegalArgumentException('provide sizes')
        if (sizes.any { it <= 0 })
            throw new IllegalArgumentException('size should be larger than 0')
        if (type == null)
            throw new IllegalArgumentException('provide type')
    }

    List<SizedListClass> listClasses() {
        sizes.collect {
            new SizedListClass(rootDir: rootDir, listType: type, size: it, packageName: packageName)
        }
    }
}

class SizedListClass {
    String rootDir
    String packageName
    ListType listType
    int size

    String className() {
        "${listType.asName()}${size}"
    }

    String fileName() {
        "${className()}.java"
    }

    String path() {
        "${rootDir}/main/java/${packageName.replace('.','/')}/${fileName()}"
    }

    String classContents() {
        """\
       |package $packageName;
       |
       |import com.example.list.ListBenchmark;
       |
       |import java.util.${listType.asName()};
       |import java.util.List;
       |import java.util.function.Consumer;
       |
       |abstract class ${className()} extends ListBenchmark {
       |
       |  ${className()}(
       |      Consumer<? super List<String>> operation,
       |      Consumer<? super List<String>> afterHook) {
       |    super(${size}, ${listType.supplier()}, operation, afterHook);
       |  }
       |}
       |""".stripMargin('|')
    }
}

class SizedListOperationClass {
    ListOperation operation
    SizedListClass parentClass

    String className() {
        "${parentClass.listType.asName()}${parentClass.size}${operation.name}"
    }

    String fileName() {
        "${className()}.java"
    }

    String path() {
        parentClass.path().replace(parentClass.className(), className())
    }

    String classContents() {
        println("op: ${operation}, invert: ${operation.invertOperation()}")
        """\
       |package ${parentClass.packageName};
       |
       |import com.example.Main;
       |
       |public class ${className()} extends ${parentClass.className()} {
       |    
       |  public ${className()}() {
       |    super(
       |        ${operation.operation},
       |        ${operation.invertOperation()}
       |        );
       |  }
       |}
       |""".stripMargin()
    }
}
