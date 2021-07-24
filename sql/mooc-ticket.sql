select * from t_ticket;

truncate t_ticket;

update t_ticket set lock_user = null where id = 1;