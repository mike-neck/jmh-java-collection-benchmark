package com.example

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

class SetClassesTask extends DefaultTask {

    String rootDir
    String packageName
    List<Integer> size
    SetType type

    @OutputDirectory
    File getSourceDirectory() {
        project.file(rootDir)
    }

    @TaskAction
    void createBenchmark() {
        validate()
        
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
}
