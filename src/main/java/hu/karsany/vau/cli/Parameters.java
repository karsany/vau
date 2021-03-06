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

package hu.karsany.vau.cli;

import hu.karsany.vau.cli.task.manager.TaskManager;
import picocli.CommandLine;

import java.io.File;

@CommandLine.Command(description = "VAU Data Vault Generator",
        name = "vau", version = "vau 2.0-SNAPSHOT")
public class Parameters {
    @CommandLine.Option(names = {"-d", "--directory"}, description = "Project directory. Defaults to current directory.")
    private File projectDirectory = new File(".");
    @CommandLine.Parameters(arity = "0..*", paramLabel = "TASK", description = "Tasks to complete.")
    private TaskManager.Task[] tasks;

    public static Parameters commandLineParsing(String[] args) {

        if (args.length == 0) {
            CommandLine.usage(new Parameters(), System.out);
        }

        return CommandLine.populateCommand(new Parameters(), args);
    }

    public TaskManager.Task[] getTasks() {
        return tasks;
    }

    public File getProjectDirectory() {
        return projectDirectory;
    }

}
