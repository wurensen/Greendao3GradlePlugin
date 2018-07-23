package com.lancewu.plugin

import kotlin.TuplesKt
import kotlin.TypeCastException
import kotlin.collections.CollectionsKt
import kotlin.collections.MapsKt
import kotlin.io.FilesKt
import kotlin.jvm.internal.Intrinsics
import kotlin.sequences.SequencesKt
import kotlin.text.Charsets
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.ConfigurableFileTree
import org.gradle.api.file.FileCollection
import org.gradle.api.logging.LogLevel
import org.greenrobot.greendao.codemodifier.Greendao3Generator
import org.greenrobot.greendao.codemodifier.SchemaOptions
import org.greenrobot.greendao.gradle.*

/**
 * 由于macOS文件系统获取目录下的文件列表时排序不是按字母排序，导致全量生成dao代码时顺序有问题；
 * 该插件基于greendao插件开发，把获取到文件列表后，重新排序后再传递给下个task；
 * 并且取消增量编译特性，新增表也全量生成，以便保持团队内的人员编译后生成的代码也是一致的
 */
class GreendaoGradlePlugin implements Plugin<Project> {

    private final String mName = "greendao"
    private final String mPackageName = "org/greenrobot/greendao"
    private final String mVersion = "3.2.2"
    private Project mProject

    @Override
    void apply(Project project) {
        mProject = project
        project.getLogger().debug(this.class.getSimpleName() + " plugin starting...")
        project.getExtensions().create(this.mName, GreendaoOptions.class, project)
        project.afterEvaluate {
            addTask()
        }
    }

    private void addTask() {
        String version = mVersion
        mProject.getLogger().debug(getName() + " plugin " + version + " preparing tasks...")
        File candidatesFile = mProject.file("build/cache/" + getName() + "-candidates.list")
        // 删出candidates文件，让每次执行task都全量生成dao
        if (candidatesFile.exists()) {
            candidatesFile.delete()
        }
        SourceProvider sourceProvider = SourceProviderKt.getSourceProvider(mProject)
        String encoding = sourceProvider.getEncoding()
        if (encoding == null) {
            encoding = "UTF-8"
        }
        Map taskArgs = new HashMap<>()
        taskArgs.put("type", DetectEntityCandidatesTask.class)
        Task task = mProject.task(taskArgs, getName() + "Prepare")
        if (task == null) {
            throw new TypeCastException("null cannot be cast to non-null type org.greenrobot.greendao.gradle.DetectEntityCandidatesTask")
        }
        DetectEntityCandidatesTask prepareTask = (DetectEntityCandidatesTask) task
        FileCollection collection = sourceProvider.sourceTree().matching {
            it.include("**/*.java")
        }

        // 文件列表重新排序
        List<File> sortedFiles = collection.sort(true, new Comparator<File>() {
            @Override
            int compare(File f1, File f2) {
                return f1.getName().compareToIgnoreCase(f2.getName())
            }
        })
        collection = mProject.files(sortedFiles.toArray())

        prepareTask.setSourceFiles(collection)
        prepareTask.setCandidatesListFile(candidatesFile)
        prepareTask.setVersion(version)
        prepareTask.setCharset(encoding)
        prepareTask.setGroup(this.getName())
        prepareTask.setDescription("Finds entity source files for " + this.getName())
        GreendaoOptions options = (GreendaoOptions) mProject.getExtensions().getByType(GreendaoOptions.class)
        boolean writeToBuildFolder = options.getTargetGenDir() == null
        File file
        if (writeToBuildFolder) {
            file = new File(mProject.getBuildDir(), "generated/source/" + this.getName())
        } else {
            file = options.getTargetGenDir()
            if (file == null) {
                Intrinsics.throwNpe()
            }
        }
        File targetGenDir = file
        GreendaoOptions greendaoOptions = options
        Task greendaoTask = createGreendaoTask(mProject, candidatesFile, greendaoOptions, targetGenDir, encoding, version)
        greendaoTask.dependsOn(prepareTask)
        sourceProvider.addGeneratorTask(greendaoTask, targetGenDir, writeToBuildFolder)
    }

    private Task createGreendaoTask(Project project, File candidatesFile, GreendaoOptions options,
                                    File targetGenDir, String encoding, String version) {
        Task task = project.task(this.mName + "Generate")
        task.getLogging().captureStandardOutput(LogLevel.INFO)
        task.getInputs().file(candidatesFile)
        task.getInputs().property("plugin-version", version)
        task.getInputs().property("source-encoding", encoding)
        Map schemaOptions = collectSchemaOptions(options.getDaoPackage(), targetGenDir, options)
        schemaOptions.each {
            task.getInputs().property("schema-" + it.getKey(), it.getValue().toString())
        }
        ConfigurableFileTree outputFileTree2 = project.fileTree(targetGenDir, {
            it.include("**/*Dao.java", "**/DaoSession.java", "**/DaoMaster.java")
        })
        task.getOutputs().files(outputFileTree2)
        if (options.getGenerateTests()) {
            task.getOutputs().dir(options.getTargetGenDirTests())
        }
        task.doLast {
            if (!candidatesFile.exists()) {
                throw new IllegalArgumentException("Candidates file does not exist. Can't continue")
            }
            def sequence = CollectionsKt.asSequence(FilesKt.readLines(candidatesFile, Charsets.UTF_8))
            def nameSequence = SequencesKt.drop(sequence, 1)
            def fileSequence = SequencesKt.map(nameSequence, { String name ->
                return new File(name)
            })
            Iterable candidatesIterable = SequencesKt.asIterable(fileSequence)
            new Greendao3Generator(options.getFormatting$greendao_gradle_plugin_main().data,
                    options.skipTestGeneration, encoding).run(candidatesIterable, schemaOptions)
        }
        task.setGroup(this.mName)
        task.setDescription("Generates source files for " + this.mName)
        return task
    }

    static Map<String, SchemaOptions> collectSchemaOptions(String daoPackage, File genSrcDir, GreendaoOptions options) {
        SchemaOptions defaultOptions = new SchemaOptions("default", options.getSchemaVersion(),
                daoPackage, genSrcDir, options.getGenerateTests() ? options.getTargetGenDirTests() : null)
        Map schemaOptions = MapsKt.mutableMapOf(TuplesKt.to("default", defaultOptions))
        Map schemasMap = options.getSchemas$greendao_gradle_plugin_main().getSchemasMap()
        Collection schemasCollection = new ArrayList(schemasMap.size())
        schemasMap.each { item ->
            String name = item.getKey()
            SchemaOptionsExtension schemaExt = item.getValue()
            // package
            String packageStr = schemaExt.getDaoPackage()
            if (packageStr == null) {
                packageStr = defaultOptions.getDaoPackage()
                if (packageStr != null) {
                    packageStr = packageStr + "." + name
                }
            }
            // version
            Integer version = schemaExt.getVersion()
            int versionValue = version != null ? version.intValue() : defaultOptions.getVersion()
            // testsOutputDir
            File testsOutputDir = null
            if (options.getGenerateTests()) {
                testsOutputDir = schemaExt.getTargetGenDirTests();
                if (testsOutputDir == null) {
                    testsOutputDir = defaultOptions.getTestsOutputDir();
                }
            }
            SchemaOptions newOptions = new SchemaOptions(name, versionValue, packageStr,
                    defaultOptions.getOutputDir(), testsOutputDir)
            schemasCollection.add(newOptions)
        }
        schemasCollection.each { item ->
            item = TuplesKt.to(item.getName(), item)
            schemaOptions.put(item.getFirst(), item.getSecond())
        }
        return schemaOptions
    }

    String getName() {
        return this.mName
    }

    String getPackageName() {
        return this.mPackageName
    }

}