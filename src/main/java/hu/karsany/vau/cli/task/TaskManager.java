/*
 * Copyright (c) 2017, Ferenc Karsany
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *  * Neither the name of  nor the names of its contributors may be used to
 *    endorse or promote products derived from this software without specific
 *    prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package hu.karsany.vau.cli.task;

import org.pmw.tinylog.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TaskManager {

    private final Set<Task> selectedTasks = new HashSet<>();

    public TaskManager(List<Task> selectedTasks) {
        for (Task st : selectedTasks) {
            addTaskWithDependencies(st);
        }
    }

    private void addTaskWithDependencies(Task t) {
        selectedTasks.add(t);
        for (Task d : t.dependencies) {
            addTaskWithDependencies(d);
        }
    }

    public void run() throws IllegalAccessException, InstantiationException, IOException {
        for (Task task : Task.values()) {
            if (selectedTasks.contains(task)) {
                Logger.info("Start task " + task.name() + "...");
                final AbstractTask realTask = task.taskSpec.newInstance();
                realTask.run();
            }
        }
        Logger.info("VAU finished.");
    }

    public enum Task {
        clean(Clean.class),
        init_config(InitConfig.class),
        init_model(InitModel.class, init_config),
        init_mapping(InitMapping.class, init_config),
        compile_tables(CompileTables.class, init_model),
        compile_sequences(CompileSequences.class, init_model),
        example_mapping(ExampleMapping.class, init_model),
        compile_loaders(CompileLoaders.class, init_model, init_mapping),
        compile_grants(CompileGrants.class, init_mapping),
        install_script(InstallScript.class, init_config),
        compile(NopeTask.class, compile_tables, compile_sequences, example_mapping, compile_loaders, compile_grants, install_script),
        doc_csv(DocCsv.class, init_model),
        doc_tgf(DocTgf.class, init_model),
        doc_html(DocHtml.class, init_model),
        table_lineage(TableLineage.class, init_model, init_mapping),
        column_lineage(ColumnLineage.class, init_model, init_mapping),
        lineage(NopeTask.class, table_lineage, column_lineage),
        doc(NopeTask.class, doc_csv, doc_tgf, doc_html, lineage),
        all(NopeTask.class, clean, compile, doc);

        private final Set<Task> dependencies = new HashSet<>();
        private final Class<? extends AbstractTask> taskSpec;

        Task(Class<? extends AbstractTask> taskSpec, Task... dependencies) {
            this.taskSpec = taskSpec;
            this.dependencies.addAll(Arrays.asList(dependencies));
        }
    }

}
