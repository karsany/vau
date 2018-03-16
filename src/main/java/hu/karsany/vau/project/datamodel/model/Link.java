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

import hu.karsany.vau.common.struct.Pair;

import java.util.List;
import java.util.stream.Collectors;

public class Link extends DocumentableTable implements Entity {
    private final String linkName;
    private final List<Pair<Hub, String>> connectedHubs;
    private final List<Column> idColumns;

    public Link(String linkName, List<Pair<Hub, String>> hubs) {
        super("LNK_" + linkName);
        this.linkName = linkName;
        this.connectedHubs = hubs;

        Column idColumn = new Column(linkName + "_ID", Column.BusinessDataType.ID, true, "SK for " + linkName);
        addColumn(idColumn);
        this.addUniqueKey(idColumn);

        idColumns = hubs.stream()
                .map(hub -> new Column(hub.getRight() + "_ID", Column.BusinessDataType.ID, true, "SK for hub " + hub.getLeft().getEntityName()))
                .collect(Collectors.toList());

        Column[] idColsArray = idColumns.toArray(new Column[]{});
        addColumn(idColsArray);
        this.addUniqueKey(idColsArray);

        addColumn(
                new Column("C$SOURCE_SYSTEM", Column.BusinessDataType.SMALLTEXT, true, "Source System"),
                new Column("C$LOAD_DATE", Column.BusinessDataType.DATE, true, "First Load Date"),
                new Column("C$AUDIT_ID", Column.BusinessDataType.ID, true, "Audit ID")
        );

    }

    @Override
    public String getEntityName() {
        return linkName;
    }

    @Override
    public String getTableType() {
        return "LNK";
    }

    @Override
    public String getColor() {
        return "CFFFCF";
    }

    public List<Column> getIdColumns() {
        return idColumns;
    }

    public List<Pair<Hub, String>> getConnectedHubs() {
        return connectedHubs;
    }
}
