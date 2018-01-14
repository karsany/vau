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

import hu.karsany.vau.util.VauException;
import hu.karsany.vau.util.struct.Pair;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

public class DataModel {

    private Set<DocumentableTable> tables = new HashSet<>();
    private Set<Pair<DocumentableTable, DocumentableTable>> tableConnections = new HashSet<>();

    public Set<Pair<DocumentableTable, DocumentableTable>> getTableConnections() {
        return tableConnections;
    }

    public void addTable(DocumentableTable t) {
        if (t instanceof Hub) {
            throw new VauException("Cannot add HUB directly with addTable.");
        } else {
            tables.add(t);
        }
    }

    public void addConnection(DocumentableTable t1, DocumentableTable t2) {
        tableConnections.add(new Pair<>(t1, t2));
    }

    public Hub getHub(String hubEntityName) {
        Optional<DocumentableTable> first = tables.stream()
                .filter(table -> table instanceof Hub)
                .filter(table -> ((Hub) table).getEntityName().equals(hubEntityName))
                .findFirst();

        if (first.isPresent()) {
            return (Hub) first.get();
        } else {
            Hub e = new Hub(hubEntityName);
            tables.add(e);
            return e;
        }
    }

    public Set<DocumentableTable> getTables() {
        return tables;
    }

    public Link getLink(String linkName) {
        try {
            return (Link) tables.stream()
                    .filter(table -> table instanceof Link)
                    .filter(table -> ((Link) table).getEntityName().equals(linkName))
                    .findAny()
                    .get();
        } catch (NoSuchElementException e) {
            throw new VauException("Link not found: " + linkName, e);
        }
    }

    public Satellite getSatellite(String entityName, String dataGroupName) {
        return (Satellite) tables.stream()
                .filter(table -> table.getTableName().equals("SAT_" + entityName + "_" + dataGroupName))
                .findAny()
                .get();
    }

    public Reference getReference(String entityName) {
        return (Reference) tables.stream()
                .filter(table -> table instanceof Reference)
                .filter(table -> ((Reference) table).getEntityName().equals(entityName))
                .findAny()
                .get();
    }
}
