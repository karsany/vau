-- sat
-- entity        LOCATION
-- datagroup     MAIN
-- source_system HR
-- load_method   cdc(ROW_EVENT, ROW_TS)
-- sql --------------------------------

Select 1 as LOCATION_BK,
        null as postal_code ,        null as city ,        null as street_address ,        null as state_province ,        null as country_code   From dual

