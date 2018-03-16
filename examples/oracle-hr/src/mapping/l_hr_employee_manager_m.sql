-- link
-- name          EMPLOYEE_MANAGER
-- source_system HR
-- sql --------------------------------

Select employee_id employee_bk,
       manager_id  manager_bk
  From hr.employees
 Where manager_id Is Not Null