grammar Loader;

s
	: loader;

loader
	: (hub|sat|link|ref) ;

hub
	: '--' 'hub'
	  '--'* 'entity' entity_name
	  '--'* 'source_system' source_system_name ;

ref
    : '--' 'ref'
      '--'* 'ref' reference_name
      '--'* 'source_system' source_system_name ;


sat
	: '--' 'sat'
	  '--'* 'entity' entity_name
	  '--'* 'datagroup' datagroup_name
	  '--'* 'source_system' source_system_name
	  '--'* 'load_method' load_method_name ;

link
	: '--' 'link'
	  '--'* 'name' link_name
	  '--'* 'source_system' source_system_name	  ;

	  
load_method_name
	: ('full'|'delta'|'insert'|cdc) ;

cdc : 'cdc' '(' cdc_column ',' timestamp_column ')';

cdc_column: ID;

timestamp_column: ID;
		
datagroup_name : ID ;

entity_name : ID ;

reference_name : ID ;

link_name : ID;

source_system_name : ID ;

ID : CHARS (CHARS|SYMBOLS)*;

EXTID : CHARS (CHARS|SYMBOLSN)*;

SYMBOLS : ('_'|'$') ;
SYMBOLSN : ('_'|'$'|'0'|'1'|'2'|'3'|'4'|'5'|'6'|'7'|'8'|'9') ;

CHARS : [A-Z] ;

WS : [ \t\r\n]+ -> skip ;

COMMENT
    : '/*' .*? '*/' -> skip
;
