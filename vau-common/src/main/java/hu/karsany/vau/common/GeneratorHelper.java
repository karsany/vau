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
package hu.karsany.vau.common;

import org.apache.commons.io.FileUtils;
import org.pmw.tinylog.Logger;

import java.io.File;
import java.io.IOException;

public class GeneratorHelper {

    public static void generate(File projectPath, Generator g) throws IOException {
        generate(projectPath, g, "utf-8");
    }

    public static void generate(File projectPath, Generator g, String encoding) throws IOException {
        File fileName = new File(projectPath + "/target/" + g.getOutputType().getOutputTypeName() + "/" + g.getFileName().toLowerCase());

        Logger.info("  Generating: " + fileName.toString());

        String generatedData;
        try {
            generatedData = g.toString();
        } catch (Exception e) {
            throw new VauException("Error at generating " + g.getOutputType().getOutputTypeName().toLowerCase() + " " + g.getFileName(), e);
        }
        FileUtils.write(
                fileName,
                generatedData,
                encoding);
    }


}
