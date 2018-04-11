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

import hu.karsany.vau.project.datamodel.model.type.BusinessDataType;

public class Hub extends DocumentableTable implements Entity {
    private final String hubName;

    public Hub(String hubName) {
        super("HUB_" + hubName);
        this.hubName = hubName;


        Column idColumn = new Column(hubName + "_ID", BusinessDataType.ID, true, "SK Column for " + hubName);
        Column businessKeyColumn = new Column(hubName + "_BK", BusinessDataType.MIDDLETEXT, false, "BK Column for " + hubName);

        addColumn(
                idColumn,
                businessKeyColumn,
                new Column("C$SOURCE_SYSTEM", BusinessDataType.SMALLTEXT, true, "Source System"),
                new Column("C$LOAD_DATE", BusinessDataType.DATE, true, "First Load Date"),
                new Column("C$AUDIT_ID", BusinessDataType.ID, true, "Load Audit ID")
        );

        this.addUniqueKey(idColumn);
        this.addUniqueKey(businessKeyColumn);


    }

    @Override
    public String getEntityName() {
        return hubName;
    }

    @Override
    public String getTableType() {
        return "HUB";
    }

    @Override
    public String getColor() {
        return "FFCFCF";
    }

}
