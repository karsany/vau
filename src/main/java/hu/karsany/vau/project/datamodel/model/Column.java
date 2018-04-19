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
import hu.karsany.vau.project.datamodel.model.type.DataType;
import hu.karsany.vau.project.datamodel.model.type.SimpleBusinessDataType;

import java.util.Objects;

public class Column {
    private final String columnName;
    private final DataType dataType;
    private final boolean technicalColumn;
    private final String comment;
    private String referencesTo = "";

    public Column(String columnName, DataType dataType, boolean technicalColumn, String comment) {
        this.columnName = columnName.toUpperCase();
        this.dataType = dataType;
        this.technicalColumn = technicalColumn;
        this.comment = comment;

    }

    public Column(String columnName, DataType dataType, String comment) {
        this(columnName, dataType, false, comment);
    }

    public Column(String columnName, BusinessDataType businessDataType, boolean technicalColumn, String comment) {
        this(columnName, new SimpleBusinessDataType(businessDataType), technicalColumn, comment);
    }

    public String getReferencesTo() {
        return referencesTo;
    }

    public Column(String columnName, DataType dataType, String comment, String referencesTo) {
        this(columnName, dataType, false, comment);
        this.referencesTo = referencesTo;
    }

    public String getComment() {
        return comment;
    }

    public String getDataType() {
        return dataType.getNativeDataType();
    }

    public String getColumnName() {
        return columnName;
    }

    public boolean isTechnicalColumn() {
        return technicalColumn;
    }

    public String getBusinessDataType() {
        return dataType.getBusinessDataTypeName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Column column = (Column) o;
        return Objects.equals(columnName, column.columnName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(columnName);
    }

}
