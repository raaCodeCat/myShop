--liquibase formatted sql
--changeset rakhmanovaa:0002_alter_table_items stripComments:false dbms:postgresql endDelimiter:;;
--comment: 0002_alter_table_items

alter table items add column image_path varchar(100);