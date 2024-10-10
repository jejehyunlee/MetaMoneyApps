SHOW DATABASES ;
CREATE DATABASE meta_moneydb;
USE meta_moneydb;

SHOW TABLES ;
DROP DATABASE meta_moneydb;

SELECT * FROM m_customer;
SELECT * FROM m_product;
SELECT * FROM t_peminjaman;
update t_peminjaman
set status="DITERIMA";
update t_detail_peminjaman
set sisa_tenor=1;

drop table t_detail_peminjaman;
drop table t_pembayaran;
DESC t_detail_peminjaman;
SELECT * FROM t_detail_peminjaman;
update m_customer
set is_active_peminjaman=false;

update t_detail_peminjaman
set sisa_pinjaman = 1375000;
delete from t_detail_peminjaman;
delete from t_peminjaman;

select * from t_detail_peminjaman;
delete from t_pembayaran;



DESC t_pembayaran;
SELECT * FROM t_pembayaran;
DELETE FROM t_pembayaran;
update m_customer set is_active_peminjaman=false;
update t_peminjaman set status="DITERIMA";