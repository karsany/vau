grammar Simplemap;

s : entries ;

entries : entry* ;

entry :
    'map' 'for' 'entity' entity_name '{'
     source
     business_key_def
     attributes
 '}'
;

entity_name : ID;

source : 'source' '{' 'system' system_name 'owner' owner_name 'table' table_name 'contains' containing_type  '}';

system_name : ID;
owner_name : ID;
table_name : ID;
containing_type : ('FULL'|'DELTA'|'INSERT'|cdc);

cdc : 'CDC' '(' cdc_column ')';

cdc_column: ID;

business_key_def: 'business' 'key' business_key_column ';';

business_key_column: ID;

attributes : 'attributes' '{' datagroup+ '}';

datagroup : 'datagroup' datagroup_name '{' mapping_specs '}';

datagroup_name : ID;

mapping_specs : mapping_spec+ ;

mapping_spec : target_column_name ':=' source_expression ';' ;

target_column_name : ID;

source_expression: string_constant | source_column_name ;

source_column_name: ID;

string_constant : STRINGDEF ;

ID : CHARS (CHARS|SYMBOLS|NUMBERS)*;

SYMBOLS : ('_'|'$');

CHARS : [A-Z] ;

NUMBERS : [0-9] ;

WS : [ \t\r\n]+ -> skip ;

COMMENT
    : '/*' .*? '*/' -> skip
;

LINE_COMMENT
    : '//' ~[\r\n]* -> skip
;

STRINGDEF: '"' CHARSEQUENCE? '"'
        {
          String s = getText();
          s = s.substring(1, s.length() - 1); // strip the leading and trailing quotes
          s = s.replace("\"\"", "\""); // replace all double quotes with single quotes
          setText(s);
        }
        ;

fragment CHARSEQUENCE: CHAR+ ;

fragment CHAR: ~["]
    |   '\\\n'   // Added line
    |   '\\\r\n' // Added line
    ;
