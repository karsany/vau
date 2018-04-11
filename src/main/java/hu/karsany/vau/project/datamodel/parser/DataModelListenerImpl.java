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

package hu.karsany.vau.project.datamodel.parser;

import hu.karsany.vau.common.VauException;
import hu.karsany.vau.common.struct.Pair;
import hu.karsany.vau.grammar.datamodel.DataModelBaseListener;
import hu.karsany.vau.grammar.datamodel.DataModelParser;
import hu.karsany.vau.project.datamodel.model.*;
import hu.karsany.vau.project.datamodel.model.type.BusinessDataType;
import hu.karsany.vau.project.datamodel.model.type.DataType;
import hu.karsany.vau.project.datamodel.model.type.NativeDataType;
import hu.karsany.vau.project.datamodel.model.type.SimpleBusinessDataType;

import java.util.List;
import java.util.stream.Collectors;

class DataModelListenerImpl extends DataModelBaseListener {

    private final DataModel dataModel;
    private Hub currHub = null;
    private Link currLink = null;
    private Satellite currSat = null;
    private Reference currRef = null;

    public DataModelListenerImpl(DataModel dataModel) {
        this.dataModel = dataModel;
    }

    private void setCurrent(Hub h) {
        currHub = h;
        currLink = null;
        currSat = null;
        currRef = null;
    }

    private void setCurrent(Link l) {
        currHub = null;
        currLink = l;
        currSat = null;
        currRef = null;
    }

    private void setCurrent(Satellite s) {
        currSat = s;
    }

    private void setCurrent(Reference r) {
        currHub = null;
        currLink = null;
        currSat = null;
        currRef = r;
    }

    @Override
    public void enterEntity(DataModelParser.EntityContext ctx) {
        String entityName = ctx.entity_name().getText();
        setCurrent(dataModel.getHub(entityName));
    }

    @Override
    public void enterLink(DataModelParser.LinkContext ctx) {
        String linkName = ctx.link_name().getText();
        List<Pair<Hub, String>> hubsWithAliases = ctx.entity_name_with_optional_alias().stream()
                .map(entity_nameContext -> {

                    String alias;
                    if (entity_nameContext.alias() != null) {
                        alias = entity_nameContext.alias().getText();
                    } else {
                        alias = entity_nameContext.entity_name().getText();
                    }

                    Pair<Hub, String> hubWithAlias = new Pair<>(dataModel.getHub(entity_nameContext.entity_name().getText()), alias);

                    return hubWithAlias;
                })
                .collect(Collectors.toList());

        Link l = new Link(linkName, hubsWithAliases);
        setCurrent(l);
        dataModel.addTable(l);
        for (Pair<Hub, String> h : hubsWithAliases) {
            dataModel.addConnection(h.getLeft(), l);
        }
    }

    @Override
    public void enterDatagroup(DataModelParser.DatagroupContext ctx) {
        String satelliteQualifier = ctx.datagroup_name().getText();

        Satellite sat;
        if (currHub != null) {
            sat = new Satellite(currHub, satelliteQualifier);
        } else {
            sat = new Satellite(currLink, satelliteQualifier);
        }
        setCurrent(sat);
        dataModel.addTable(sat);
        dataModel.addConnection(currHub == null ? currLink : currHub, sat);
    }

    @Override
    public void enterAttribute(DataModelParser.AttributeContext ctx) {
        String attributeName = ctx.attribute_name().getText();
        String comment = ctx.comment() != null ? ctx.comment().STRINGDEF().getText() : "";

        DataType dataType = getDataType(ctx.type());

        if (currSat != null) {
            currSat.addColumn(new Column(attributeName, dataType, comment));
        } else if (currRef != null) {
            currRef.addColumn(new Column(attributeName, dataType, comment));
        } else {
            throw new VauException("Attribute call not valid here: " + ctx.attribute_name());
        }
    }

    private DataType getDataType(DataModelParser.TypeContext ctx) {
        DataType dataType;
        if (ctx.nativetype() != null) {
            dataType = new NativeDataType(ctx.nativetype().nativetypedef().getText());
        } else {
            dataType = new SimpleBusinessDataType(BusinessDataType.valueOf(ctx.getText()));
        }
        return dataType;
    }

    @Override
    public void enterRef(DataModelParser.RefContext ctx) {
        String referenceName = ctx.reference_name().getText();
        Reference reference = new Reference(referenceName);
        setCurrent(reference);
        dataModel.addTable(reference);
    }

    @Override
    public void enterKey(DataModelParser.KeyContext ctx) {
        String attributeName = ctx.attribute_name().getText();
        String comment = ctx.comment() != null ? ctx.comment().STRINGDEF().getText() : "";
        DataType dataType = getDataType(ctx.type());
        Column c = new Column(attributeName, dataType, comment);
        currRef.addColumn(c);
        currRef.addToMainUniqueKey(c);
    }

    public DataModel getDataModel() {
        return dataModel;
    }

}
