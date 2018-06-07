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

package hu.karsany.vau.project.mapping.generator.script;

import hu.karsany.vau.cli.task.manager.AbstractTask;
import hu.karsany.vau.common.Generator;
import hu.karsany.vau.common.GeneratorHelper;
import hu.karsany.vau.project.mapping.generator.loader.LinkLoader;
import hu.karsany.vau.project.mapping.generator.loader.LoaderParameter;
import hu.karsany.vau.project.mapping.generator.loader.LoaderProcedure;
import hu.karsany.vau.project.mapping.generator.loader.SatLoader;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class GenerateProcessDependencies extends AbstractTask {


    @Override
    public void run() throws IOException {


        /***
         * add_task(p_group_name => '$GROUP_NAME$', p_task_name  => '$SCHEMA$.$LOADER_NAME$;', p_parent_task_name => Null, p_task_desc => null);
         *
         */

        GeneratorHelper.generate(project.getProjectPath(), new ProcessDependencyGenerator(project.getLoaderProcedures()));


    }

    private class ProcessDependencyGenerator implements Generator {

        private final List<LoaderProcedure> loaderProcedures;

        public ProcessDependencyGenerator(List<LoaderProcedure> loaderProcedures) {

            this.loaderProcedures = loaderProcedures;
        }

        @Override
        public String getFileName() {
            return "dependencies.sql";
        }

        @Override
        public OutputType getOutputType() {
            return OutputType.INSTALL_SCRIPT;
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();

            for (LoaderProcedure loaderProcedure : loaderProcedures) {
                stringBuilder.append(
                        String.format(
                                "add_task(p_group_name => '$GROUP_NAME$', p_task_name  => '%s.%s;', p_parent_task_name => Null, p_task_desc => '%s');\n",
                                project.getConfiguration().getTargetSchema(),
                                loaderProcedure.getLoaderName().toUpperCase(),
                                loaderProcedure.getLoaderName().toUpperCase()
                        )
                );
            }

            for (LoaderProcedure loaderProcedure : loaderProcedures) {

                final Generator loaderGen = loaderProcedure.getLoader().getGenerator();

                if (loaderGen instanceof SatLoader) {

                    final SatLoader sl = (SatLoader) loaderGen;

                    List<String> parentProcs = loaderProcedures.stream()
                            .filter(ll ->
                                    (ll.getLoader().getLoaderParameter().getLoaderType() == LoaderParameter.LoaderType.HUB ||
                                            ll.getLoader().getLoaderParameter().getLoaderType() == LoaderParameter.LoaderType.LINK
                                    ) &&
                                            ll.getLoader().getLoaderParameter().getEntityName() != null &&
                                            ll.getLoader().getLoaderParameter().getEntityName().equals(sl.getModel().getSat().getEntityName())
                            )
                            .map(ll -> ll.getLoaderName().toUpperCase())
                            .collect(Collectors.toList());


                    for (String parentProc : parentProcs) {
                        stringBuilder.append(
                                String.format(
                                        "add_task_dependency(p_task_name => '%s.%s;', p_parent_task_name => '%s.%s;');\n",
                                        project.getConfiguration().getTargetSchema(),
                                        loaderProcedure.getLoaderName().toUpperCase(),
                                        project.getConfiguration().getTargetSchema(),
                                        parentProc
                                )
                        );
                    }


                } else if (loaderGen instanceof LinkLoader) {
                    final LinkLoader lilo = (LinkLoader) loaderGen;

                    List<String> parentProcs = loaderProcedures.stream()
                            .filter(ll ->
                                    (ll.getLoader().getLoaderParameter().getLoaderType() == LoaderParameter.LoaderType.HUB) &&
                                            ll.getLoader().getLoaderParameter().getEntityName() != null &&
                                            lilo.getLink().getDistinctConnectedHubs().contains(ll.getLoader().getLoaderParameter().getEntityName())
                            )
                            .map(ll -> ll.getLoaderName().toUpperCase())
                            .collect(Collectors.toList());

                    for (String parentProc : parentProcs) {
                        stringBuilder.append(
                                String.format(
                                        "add_task_dependency(p_task_name => '%s.%s;', p_parent_task_name => '%s.%s;');\n",
                                        project.getConfiguration().getTargetSchema(),
                                        loaderProcedure.getLoaderName().toUpperCase(),
                                        project.getConfiguration().getTargetSchema(),
                                        parentProc
                                )
                        );
                    }


                }

                stringBuilder.append("\n");

            }


            return stringBuilder.toString();
        }
    }

}
