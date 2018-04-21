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

package hu.karsany.vau.project.datamodel.model;

import hu.karsany.vau.common.Generator;
import hu.karsany.vau.common.VauException;
import hu.karsany.vau.common.templating.TemplateEvaluation;
import org.pmw.tinylog.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Table implements Generator {

    private final String owner;
    private final String tableName;
    private final List<Column> columns = new ArrayList<>();
    private final List<List<Column>> uniqueKeys = new ArrayList<>();

    public Table(String owner, String tableName) {
        this.owner = owner == null ? null : owner.toUpperCase();
        this.tableName = tableName.toUpperCase();
        if (this.tableName.length() > 30) {
            Logger.warn("Tablename " + tableName + " is longer than 30 characters...");
        }
    }

    public Table(String tableName) {
        this(null, tableName);
    }

    public void addColumn(Column... columnList) {
        this.addColumn(Arrays.asList(columnList));
    }

    public List<List<Column>> getUniqueKeys() {
        return uniqueKeys;
    }

    protected void addUniqueKey(Column... uniqueColumnCombination) {
        uniqueKeys.add(Arrays.asList(uniqueColumnCombination));
    }

    public List<Column> getColumns() {
        return columns;
    }

    public List<Column> getNonTechnicalColumns() {
        return columns.stream()
                .filter(column -> !column.isTechnicalColumn())
                .collect(Collectors.toList());
    }

    public String getFullyQualifiedName() {
        if (owner == null || owner.isEmpty()) {
            return tableName;
        } else {
            return owner + "." + tableName;
        }
    }

    public String getOwner() {
        return owner;
    }

    public String getTableName() {
        return tableName;
    }

    @Override
    public String toString() {
        return new TemplateEvaluation("table_create_script.sql", this).toString();
    }

    @Override
    public String getFileName() {
        return getTableName() + ".sql";
    }

    @Override
    public OutputType getOutputType() {
        return OutputType.TABLE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Table table = (Table) o;

        if (owner != null ? !owner.equals(table.owner) : table.owner != null) {
            return false;
        }
        return tableName.equals(table.tableName);
    }

    @Override
    public int hashCode() {
        int result = owner != null ? owner.hashCode() : 0;
        result = 31 * result + tableName.hashCode();
        return result;
    }

    public void addColumn(List<Column> columnList) {
        for (Column column : columnList) {
            if (this.columns.contains(column)) {
                throw new VauException("Table " + tableName + " contains the " + column.getColumnName() + ". Specify an alias.");
            }
            columns.add(column);
        }

    }
}
