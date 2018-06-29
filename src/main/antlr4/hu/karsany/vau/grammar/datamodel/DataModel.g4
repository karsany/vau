grammar DataModel;

s  : entries ;

entries: (entity|link|ref)+ ;

entity : 'entity' entity_name entity_type_def? datagroup_definition ;

entity_type_def :
    'type' entity_type_name
    ;

entity_type_name:
    'TRANSACTIONAL' | 'SCD'
    ;

datagroup_definition : ('{' datagroup* '}')? ;

link : 'link' link_name 'between' entity_name_with_optional_alias (('and'|',') entity_name_with_optional_alias)* (';'|datagroup_definition) ;

entity_name_with_optional_alias :
    entity_name ('as' alias)?;

alias :
    ID;

datagroup : 'datagroup' datagroup_name '{' attributes '}';

ref : 'ref' reference_name '{' reference_attributes '}';

reference_attributes : keys attributes ;

keys : key+ ;

attributes : attribute* ;

attribute : 'attr' attribute_name 'typ' type referencing_def? comment? ';';

referencing_def: 'references' reference_name;

key : 'key' attribute_name 'typ' type comment? ';';

comment : STRINGDEF
    ;

STRINGDEF: '"' CHARSEQUENCE? '"'
        {
          String s = getText();
          s = s.substring(1, s.length() - 1); // strip the leading and trailing quotes
          s = s.replace("\"\"", "\""); // replace taskName double quotes with single quotes
          setText(s);
        }
        ;

fragment CHARSEQUENCE: CHAR+ ;

fragment CHAR: ~["]
    |   '\\\n'   // Added line
    |   '\\\r\n' // Added line
    ;


type : vautype | nativetype ;

nativetype: 'NATIVE' '(' nativetypedef ')' ;

nativetypedef: STRINGDEF ;

vautype :  'CURRENCY' |
        'DATE' |
        'LARGETEXT' |
        'MIDDLETEXT' |
        'MIDDLETEXT' |
        'MONEY' |
        'PERCENTAGE' |
        'SHORTTEXT' |
        'SMALLTEXT' |
		'FLAG' |
		'INTEGER' |
		'ID'
;

datagroup_name : ID ;

entity_name : ID ;

link_name : ID;

attribute_name : ID ;

reference_name : ID ;

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