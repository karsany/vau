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

package hu.karsany.vau.project.datamodel.model;

import java.util.ArrayList;
import java.util.List;

public class Reference extends DocumentableTable {

    private final String referenceName;
    private List<Column> refKeys = new ArrayList<>();

    public Reference(String referenceName) {
        super("REF_" + referenceName);
        this.referenceName = referenceName;
        Column startDate = new Column("C$START_DATE", Column.BusinessDataType.DATE, true, "Record Validity From");
        addColumn(
                new Column("C$SOURCE_SYSTEM", Column.BusinessDataType.SMALLTEXT, true, "Source System"),
                startDate,
                new Column("C$END_DATE", Column.BusinessDataType.DATE, true, "Record Validity To"),
                new Column("C$AUDIT_ID", Column.BusinessDataType.ID, true, "Audit ID")
        );
        this.refKeys.add(startDate);

    }

    @Override
    public String getEntityName() {
        return referenceName;
    }

    @Override
    public String getTableType() {
        return "REF";
    }

    @Override
    public String getColor() {
        return "CFFFFF";
    }

    public void addToMainUniqueKey(Column c) {
        this.refKeys.add(c);
        this.getUniqueKeys().clear();
        this.addUniqueKey(refKeys.toArray(new Column[]{}));
    }
}
