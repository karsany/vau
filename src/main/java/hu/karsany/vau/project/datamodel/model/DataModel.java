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

import hu.karsany.vau.common.VauException;
import hu.karsany.vau.common.struct.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class DataModel {

    private Set<DocumentableTable> tables = new HashSet<>();
    private Set<Pair<DocumentableTable, DocumentableTable>> tableConnections = new HashSet<>();

    public Set<Pair<DocumentableTable, DocumentableTable>> getTableConnections() {
        return tableConnections;
    }

    public void addTable(DocumentableTable t) {
        tables.add(t);
    }

    public void addConnection(DocumentableTable t1, DocumentableTable t2) {
        tableConnections.add(new Pair<>(t1, t2));
    }

    public Set<Entity> getEntityTables() {
        return tables
                .stream()
                .filter(t -> t instanceof Entity)
                .map(t -> (Entity) t)
                .collect(Collectors.toSet());
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

        final Optional<DocumentableTable> optionalResult = tables.stream()
                .filter(table -> table.getTableName().equals("S_" + entityName + "_" + dataGroupName))
                .findAny();

        if (!optionalResult.isPresent()) {
            throw new VauException("Satellite not found: " + entityName + "/" + dataGroupName);
        }

        final Satellite satellite = (Satellite) optionalResult
                .get();

        return satellite;
    }

    public Reference getReference(String entityName) {
        return (Reference) tables.stream()
                .filter(table -> table instanceof Reference)
                .filter(table -> ((Reference) table).getEntityName().equals(entityName))
                .findAny()
                .get();
    }

    public Hub getHub(String entityName) {
        Optional<DocumentableTable> first = findHub(entityName);

        if (first.isPresent()) {
            return (Hub) first.get();
        } else {
            throw new VauException("Hub " + entityName + " not found");
        }
    }

    private Optional<DocumentableTable> findHub(String entityName) {
        return tables.stream()
                .filter(table -> table instanceof Hub)
                .filter(table -> ((Hub) table).getEntityName().equals(entityName))
                .findFirst();
    }

    public void addTable(List<? extends DocumentableTable> hubSats) {
        this.tables.addAll(hubSats);
    }

    public Hub createHubIfNotExists(String hubEntityName) {
        Optional<DocumentableTable> first = findHub(hubEntityName);

        if (first.isPresent()) {
            return (Hub) first.get();
        } else {
            Hub e = new Hub(hubEntityName);
            tables.add(e);
            return e;
        }
    }
}
