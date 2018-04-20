package hu.karsany.vau.project.datamodel.newmodel;

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

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class VauDataModelParser {

    private final String dataModelContent;

    public VauDataModelParser(String dataModelContent) {
        this.dataModelContent = dataModelContent;
    }

    public DataModel parse() {
        CharStream charStream = new ANTLRInputStream(dataModelContent);
        DataModelLexer lexer = new DataModelLexer(charStream);
        TokenStream tokens = new CommonTokenStream(lexer);
        DataModelParser parser = new DataModelParser(tokens);

        EntriesVisitor entriesVisitor = new EntriesVisitor();
        entriesVisitor.visit(parser.s());
        DataModel dataModel = entriesVisitor.getDataModel();

        return dataModel;
    }

    private class EntriesVisitor extends DataModelBaseVisitor<DataModel> {

        private DataModel dataModel = new DataModel();

        @Override
        public DataModel visitEntity(DataModelParser.EntityContext ctx) {
            Hub hub = new Hub(ctx.entity_name().getText());
            dataModel.addTable(hub);

            DataGroupVisitor dataGroupVisitor = new DataGroupVisitor(hub);
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

            DataGroupVisitor dataGroupVisitor = new DataGroupVisitor(link);

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

        public DataModel getDataModel() {
            return dataModel;
        }
    }

    private class DataGroupVisitor extends DataModelBaseVisitor<Satellite> {
        private final Hub hub;
        private final Link link;

        public DataGroupVisitor(Hub hub) {
            this.hub = hub;
            link = null;
        }

        public DataGroupVisitor(Link link) {
            this.link = link;
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

            AttributeVisitor attributeVisitor = new AttributeVisitor();
            List<Column> attributes = ctx.attributes().attribute()
                    .stream()
                    .map(attribute -> attribute.accept(attributeVisitor))
                    .collect(toList());

            satellite.addColumn(attributes);

            return satellite;
        }
    }


    private class AttributeVisitor extends DataModelBaseVisitor<Column> {
        @Override
        public Column visitAttribute(DataModelParser.AttributeContext ctx) {
            DataType dataType = ctx.type().accept(new DataTypeVisitor());
            String commentText = ctx.comment() != null ? ctx.comment().getText() : "";
            String references = ctx.referencing_def() != null ? ctx.referencing_def().reference_name().getText() : "";
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
}
