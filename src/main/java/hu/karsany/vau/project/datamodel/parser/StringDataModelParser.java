package hu.karsany.vau.project.datamodel.parser;

import hu.karsany.vau.common.struct.Pair;
import hu.karsany.vau.grammar.datamodel.DataModelBaseVisitor;
import hu.karsany.vau.grammar.datamodel.DataModelLexer;
import hu.karsany.vau.grammar.datamodel.DataModelParser;
import hu.karsany.vau.project.datamodel.model.*;
import hu.karsany.vau.project.datamodel.model.type.BusinessDataType;
import hu.karsany.vau.project.datamodel.model.type.DataType;
import hu.karsany.vau.project.datamodel.model.type.NativeDataType;
import hu.karsany.vau.project.datamodel.model.type.SimpleBusinessDataType;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.pmw.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class StringDataModelParser implements GenericDataModelParser {

    private final String dataModelContent;

    public StringDataModelParser(String dataModelContent) {
        this.dataModelContent = dataModelContent;
    }

    public DataModel parse() {
        Logger.info("  Data Model parsing START");
        CharStream charStream = new ANTLRInputStream(dataModelContent);
        DataModelLexer lexer = new DataModelLexer(charStream);
        TokenStream tokens = new CommonTokenStream(lexer);
        DataModelParser parser = new DataModelParser(tokens);

        EntriesVisitor entriesVisitor = new EntriesVisitor();
        entriesVisitor.visit(parser.s());
        DataModel dataModel = entriesVisitor.getDataModel();

        Logger.info("  Data Model parsing END");
        return dataModel;
    }

    private class EntriesVisitor extends DataModelBaseVisitor<DataModel> {

        private DataModel dataModel = new DataModel();

        @Override
        public DataModel visitEntity(DataModelParser.EntityContext ctx) {
            Hub hub = new Hub(ctx.entity_name().getText());
            dataModel.addTable(hub);

            DataGroupVisitor dataGroupVisitor = new DataGroupVisitor(hub, dataModel);
            List<Satellite> sats = ctx.datagroup_definition().datagroup()
                    .stream()
                    .map(datagroup -> datagroup.accept(dataGroupVisitor))
                    .collect(toList());

            dataModel.addTable(sats);
            sats.stream().forEach(satellite -> dataModel.addConnection(hub, satellite));

            return dataModel;
        }

        @Override
        public DataModel visitLink(DataModelParser.LinkContext ctx) {
            List<Pair<Hub, String>> a = new ArrayList<>();

            ctx.entity_name_with_optional_alias().forEach(en -> {
                String entityName = en.entity_name().getText();
                String optionalAlias = en.alias() != null ? en.alias().getText() : entityName;
                a.add(new Pair<>(dataModel.getHub(entityName), optionalAlias));
            });


            Link link = new Link(ctx.link_name().getText(), a);
            dataModel.addTable(link);

            for (Pair<Hub, String> hubStringPair : a) {
                dataModel.addConnection(link, hubStringPair.getLeft());
            }

            DataGroupVisitor dataGroupVisitor = new DataGroupVisitor(link, dataModel);

            if (ctx.datagroup_definition() != null) {
                List<Satellite> sats = ctx.datagroup_definition().datagroup()
                        .stream()
                        .map(datagroup -> datagroup.accept(dataGroupVisitor))
                        .collect(toList());


                dataModel.addTable(sats);
                sats.stream().forEach(satellite -> dataModel.addConnection(link, satellite));
            }

            return dataModel;
        }

        @Override
        public DataModel visitRef(DataModelParser.RefContext ctx) {
            Reference reference = new Reference(ctx.reference_name().getText());

            List<Column> keyColumns = ctx.reference_attributes().keys().key()
                    .stream()
                    .map(key -> key.accept(new KeyVisitor()))
                    .collect(toList());

            List<Column> attrColumns = ctx.reference_attributes().attributes().attribute()
                    .stream()
                    .map(attribute -> attribute.accept(new AttributeVisitor(reference, dataModel)))
                    .collect(toList());

            reference.addColumn(keyColumns);
            reference.addColumn(attrColumns);
            keyColumns.forEach(column -> reference.addToMainUniqueKey(column));

            dataModel.addTable(reference);

            return dataModel;
        }

        public DataModel getDataModel() {
            return dataModel;
        }
    }

    private class DataGroupVisitor extends DataModelBaseVisitor<Satellite> {
        private final Hub hub;
        private final Link link;
        private final DataModel dataModel;

        public DataGroupVisitor(Hub hub, DataModel dataModel) {
            this.hub = hub;
            this.dataModel = dataModel;
            link = null;
        }

        public DataGroupVisitor(Link link, DataModel dataModel) {
            this.link = link;
            this.dataModel = dataModel;
            hub = null;
        }

        @Override
        public Satellite visitDatagroup(DataModelParser.DatagroupContext ctx) {
            Satellite satellite;
            if (hub != null) {
                satellite = new Satellite(hub, ctx.datagroup_name().getText());
            } else {
                satellite = new Satellite(link, ctx.datagroup_name().getText());
            }

            AttributeVisitor attributeVisitor = new AttributeVisitor(satellite, dataModel);
            List<Column> attributes = ctx.attributes().attribute()
                    .stream()
                    .map(attribute -> attribute.accept(attributeVisitor))
                    .collect(toList());

            satellite.addColumn(attributes);

            return satellite;
        }
    }


    private class AttributeVisitor extends DataModelBaseVisitor<Column> {

        private final DocumentableTable attributeOwner;
        private final DataModel dataModel;

        public AttributeVisitor(DocumentableTable attributeOwner, DataModel dataModel) {
            this.attributeOwner = attributeOwner;
            this.dataModel = dataModel;
        }

        @Override
        public Column visitAttribute(DataModelParser.AttributeContext ctx) {
            DataType dataType = ctx.type().accept(new DataTypeVisitor());
            String commentText = ctx.comment() != null ? ctx.comment().getText() : "";
            String references = ctx.referencing_def() != null ? ctx.referencing_def().reference_name().getText() : "";

            if (!references.equals("")) {
                dataModel.addConnection(attributeOwner, dataModel.getReference(references));
            }

            return new Column(ctx.attribute_name().getText(), dataType, commentText, references);
        }
    }

    private class DataTypeVisitor extends DataModelBaseVisitor<DataType> {
        @Override
        public DataType visitVautype(DataModelParser.VautypeContext ctx) {
            return new SimpleBusinessDataType(BusinessDataType.valueOf(ctx.getText()));
        }

        @Override
        public DataType visitNativetype(DataModelParser.NativetypeContext ctx) {
            return new NativeDataType(ctx.nativetypedef().getText());
        }
    }

    private class KeyVisitor extends DataModelBaseVisitor<Column> {
        @Override
        public Column visitKey(DataModelParser.KeyContext ctx) {
            DataType dataType = ctx.type().accept(new DataTypeVisitor());
            String commentText = ctx.comment() != null ? ctx.comment().getText() : "";
            return new Column(ctx.attribute_name().getText(), dataType, commentText);
        }
    }
}
