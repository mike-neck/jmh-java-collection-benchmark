package com.example

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

class SetClassesTask extends DefaultTask {

    String rootDir
    String packageName
    List<Integer> sizes
    SetType type

    @OutputDirectory
    File getSourceDirectory() {
        project.file(rootDir)
    }

    @TaskAction
    void createBenchmark() {
        validate()
        def classes = listClasses()
        classes.each { writeClasses(it) }
    }

    void validate() {
        if (rootDir == null)
            throw new IllegalArgumentException('provide rootDir')
        if (!rootDir.matches('[a-zA-Z]\\p{Alnum}*[/[a-zA-Z]\\p{Alnum}*]*'))
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

    List<SizedSetClass> listClasses() {
        sizes.collect {
            new SizedSetClass(rootDir: rootDir, type: type, size: it, packageName: packageName)
        }
    }

    void writeClasses(SizedSetClass klass) {
        def file = project.file(klass.path())
        def dir = file.parentFile
        if (!dir.exists()) dir.mkdirs()
        file.write(klass.classBody(), 'UTF-8')
        SetOperation.get().collect {
            new SizedSetOperationClass(operation: it, parentClass: klass)
        }.each {
            def opFile = project.file(it.path())
            println(opFile)
            opFile.write(it.classBody(), 'UTF-8')
        }
    }
}

class SizedSetClass {
    String rootDir
    String packageName
    SetType type
    int size

    String className() {
        "${type.asName()}${size}"
    }

    String fileName() {
        "${className()}.java"
    }

    String path() {
        "${rootDir}/${packageName.replace('.','/')}/${fileName()}"
    }

    String classBody() {
        """\
       |package $packageName;
       |
       |import com.example.set.SetBenchmark;
       |
       |import java.util.${type.asName()};
       |import java.util.Set;
       |import java.util.function.Consumer;
       |
       |abstract class ${className()} extends SetBenchmark {
       |
       |  ${className()}(
       |      Consumer<? super Set<String>> operation,
       |      Consumer<? super Set<String>> afterHook) {
       |    super(${size}, ${type.supplier()}, operation, afterHook);
       |  }
       |}
       |""".stripMargin()
    }
}

class SizedSetOperationClass {
    SetOperation operation
    SizedSetClass parentClass

    String className() {
        "${parentClass.type.asName()}${parentClass.size}${operation.name}"
    }

    String path() {
        parentClass.path().replace(parentClass.className(), className())
    }

    String classBody() {
        """\
       |package ${parentClass.packageName};
       |
       |import com.example.Main;
       |
       |import java.util.Set;
       |
       |public class ${className()} extends ${parentClass.className()} {
       |
       |  public ${className()}() {
       |    super(
       |        ${operation.operation},
       |        ${operation.invertOperation}
       |    );
       |  }
       |}
       |""".stripMargin()
    }
}
