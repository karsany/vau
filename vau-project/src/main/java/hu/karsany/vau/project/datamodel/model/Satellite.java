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

import hu.karsany.vau.util.struct.OneItemIterable;

public class Satellite extends DocumentableTable {

    private final Hub hub;
    private final Link link;
    private final String qualifier;

    public Satellite(Hub hub, String qualifier) {
        super("SAT_" + hub.getEntityName() + "_" + qualifier);
        this.hub = hub;
        this.link = null;
        this.qualifier = qualifier;

        Column idColumn = new Column(hub.getEntityName() + "_ID", Column.BusinessDataType.ID, true, "SK for " + hub.getEntityName());
        Column startDate = new Column("C$START_DATE", Column.BusinessDataType.DATE, true, "Record Validity From");
        addColumn(
                idColumn,
                new Column("C$SOURCE_SYSTEM", Column.BusinessDataType.SMALLTEXT, true, "Source System"),
                startDate,
                new Column("C$END_DATE", Column.BusinessDataType.DATE, true, "Record Validity To"),
                new Column("C$AUDIT_ID", Column.BusinessDataType.ID, true, "Audit ID"),
                new Column("C$REC_PRESENT", Column.BusinessDataType.ID, true, "Record Present Flag")
        );

        addUniqueKey(idColumn, startDate);

    }

    public Satellite(Link link, String qualifier) {
        super("SAT_" + link.getEntityName() + "_" + qualifier);
        this.qualifier = qualifier;
        this.hub = null;
        this.link = link;


        Column idColumn = new Column(link.getEntityName() + "_ID", Column.BusinessDataType.ID, true, "SK for " + link.getEntityName());
        Column startDate = new Column("C$START_DATE", Column.BusinessDataType.DATE, true, "Record Validity From");
        addColumn(idColumn);
        //addColumn(link.getIdColumns());
        addColumn(
                new Column("C$SOURCE_SYSTEM", Column.BusinessDataType.SMALLTEXT, true, "Source System"),
                startDate,
                new Column("C$END_DATE", Column.BusinessDataType.DATE, true, "Record Validity To"),
                new Column("C$AUDIT_ID", Column.BusinessDataType.ID, true, "Audit ID"),
                new Column("C$REC_PRESENT", Column.BusinessDataType.ID, true, "Record Present Flag")
        );

        addUniqueKey(idColumn, startDate);

    }

    public String getQualifier() {
        return qualifier;
    }

    public Hub getHub() {
        assert hub != null;
        return hub;
    }

    public Iterable<Hub> getHubs() {
        if (hub != null) {
            return new OneItemIterable<>(hub);
        } else {
            return link.getConnectedHubs();
        }
    }

    public Link getLink() {
        return link;
    }

    public Entity getEntity() {
        if (hub != null) {
            return hub;
        } else {
            return link;
        }
    }


    @Override
    public String getEntityName() {
        return hub == null ? link.getEntityName() : hub.getEntityName();
    }

    @Override
    public String getTableType() {
        return "SAT";
    }

    @Override
    public String getColor() {
        return "CFCFFF";
    }
}
