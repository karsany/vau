-- sat
-- entity        LOCATION
-- datagroup     MAIN
-- source_system HR
-- load_method   cdc(ROW_EVENT, ROW_TS)
-- sql --------------------------------

Select t.location_id    As location_bk,
       t.postal_code    As postal_code,
       t.city           As city,
       t.street_address As street_address,
       t.state_province As state_province,
       t.country_id     As country_code,
       row_event,
       row_ts
  From hr.locations_h t
