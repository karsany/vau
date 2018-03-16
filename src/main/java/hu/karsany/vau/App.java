/******************************************************************************
 * Copyright (c) 2017, Ferenc Karsany                                         *
 * All rights reserved.                                                       *
 *                                                                            *
 * Redistribution and use in source and binary forms, with or without         *
 * modification, are permitted provided that the following conditions are met:
 *                                                                            *
 *  * Redistributions of source code must retain the above copyright notice,  *
 *    this list of conditions and the following disclaimer.                   *
 *  * Redistributions in binary form must reproduce the above copyright       *
 *    notice, this list of conditions and the following disclaimer in the     *
 *    documentation and/or other materials provided with the distribution.    *
 *  * Neither the name of  nor the names of its contributors may be used to   *
 *    endorse or promote products derived from this software without specific *
 *    prior written permission.                                               *
 *                                                                            *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE  *
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE *
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE   *
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR        *
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF       *
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS   *
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN    *
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)    *
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE *
 * POSSIBILITY OF SUCH DAMAGE.                                                *
 ******************************************************************************/

package hu.karsany.vau;

import com.beust.jcommander.JCommander;
import hu.karsany.vau.cli.Parameters;
import hu.karsany.vau.cli.task.Clean;
import hu.karsany.vau.cli.task.Compile;
import hu.karsany.vau.cli.task.Documentation;
import hu.karsany.vau.project.Project;
import org.pmw.tinylog.Configurator;

import java.io.IOException;

public class App {

    private Project projectModel;

    public static void main(String... args) throws IOException {
        new App().app(args);
    }

    private static void initializeLogger() {
        // Logger
        Configurator.defaultConfig()
                .formatPattern("[{level}] {message}")
                .activate();
    }

    private static Parameters commandLineParsing(String[] args) {
        // Command Line
        if (args.length == 0) {
            Parameters ps = new Parameters();
            JCommander.newBuilder()
                    .addObject(ps)
                    .programName("vau")
                    .build()
                    .usage();

            return null;
        }

        Parameters ps = new Parameters();
        JCommander.newBuilder()
                .addObject(ps)
                .build()
                .parse(args);
        return ps;
    }

    public Project getProjectModel() {
        return projectModel;
    }

    public void app(String... args) throws IOException {
        Parameters ps = commandLineParsing(args);
        if (ps == null)
            return;

        initializeLogger();

        // Clean
        if (ps.isClean()) {
            new Clean(ps.getProjectDirectory()).run();
        }

        if (ps.isCompile() || ps.isDocumentation()) {
            projectModel = Project.initialize(ps.getProjectDirectory());
        }

        // Compile
        if (ps.isCompile()) {
            new Compile(projectModel).run();
        }

        // Documentation
        if (ps.isDocumentation()) {
            new Documentation(projectModel).run();
        }

    }
}
