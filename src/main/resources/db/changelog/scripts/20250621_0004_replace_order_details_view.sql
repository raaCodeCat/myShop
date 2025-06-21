--liquibase formatted sql
--changeset rakhmanovaa:0004_replace_order_details_view stripComments:false dbms:postgresql endDelimiter:;;
--comment: 0004_replace_order_details_view

create or replace view order_details as
select
    o.id as order_id,
    i.id as item_id,
    i."name" as title,
    i.description,
    i.price,
    oi.count,
    i.price * oi.count::numeric as total,
    o.is_paid,
    o.user_id,
    i.image_path
from orderitems oi
         inner join orders o on o.id = oi.order_id
         left join items i on i.id = oi.item_id;