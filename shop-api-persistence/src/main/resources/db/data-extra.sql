-- extra members (auto ids continue at 4..) — never touches members 1-3
INSERT INTO member (login_id, name, grade, point) VALUES
 ('dave','Dave','A',8000), ('erin','Erin','B',1200), ('frank','Frank','C',0),
 ('grace','Grace','A',15000), ('heidi','Heidi','B',300);

-- extra coupons (auto ids 4..) — never touches coupons 1 & 3
INSERT INTO coupon (code, discount_type, discount_val, min_order, expire_date, used_yn) VALUES
 ('VIP20','R',20,100000,'20991231','N'), ('FLAT2000','F',2000,20000,'20991231','N');

-- extra products: status='DISCONTINUED' ONLY (invisible to findAllOnSale + search) — keeps products=14 & search 1/3/1
INSERT INTO product (name, category, price, seller_name, status) VALUES
 ('Legacy Tablet','ELECTRONICS',299000,'Acme Corp','DISCONTINUED'),
 ('Retro Speaker','ELECTRONICS',79000,'SoundLab','DISCONTINUED'),
 ('Canvas Backpack','FASHION',54000,'Wearwell','DISCONTINUED'),
 ('Trail Jacket','SPORTS',128000,'FleetFoot','DISCONTINUED'),
 ('Ceramic Mug Set','HOME',26000,'Deskwork','DISCONTINUED'),
 ('Data Structures (Book)','BOOKS',38000,'TechPress','DISCONTINUED');
-- options for the discontinued products (auto option ids continue at 21..)
INSERT INTO product_option (product_id, option_name, extra_price, stock_qty) VALUES
 (17,'64GB',0,0), (18,'Black',0,0), (19,'One Size',0,0),
 (20,'L',0,0), (21,'Set of 4',0,0), (22,'Paperback',0,0);

-- historical orders: explicit order_id 101..115 (synthetic, above runtime range); status=2 (paid)
INSERT INTO orders (order_id, member_id, order_date, status, total_price, pay_method, pg_tid, approval_no, zipcode, address) VALUES
 (101,1,'20260501',2, 30000,'CARD','TID-30000','AP-101','06236','Seoul'),
 (102,2,'20260501',2, 89000,'CARD','TID-89000','AP-102','06236','Seoul'),
 (103,4,'20260502',2,159000,'CARD','TID-159000','AP-103','13494','Seongnam'),
 (104,5,'20260502',2, 28000,'BANK','TID-28000','AP-104','13494','Seongnam'),
 (105,1,'20260503',2, 45000,'CARD','TID-45000','AP-105','06236','Seoul'),
 (106,6,'20260503',2, 19000,'CARD','TID-19000','AP-106','48058','Busan'),
 (107,7,'20260504',2,210000,'CARD','TID-210000','AP-107','48058','Busan'),
 (108,2,'20260504',2, 34000,'BANK','TID-34000','AP-108','06236','Seoul'),
 (109,4,'20260505',2, 89000,'CARD','TID-89000','AP-109','13494','Seongnam'),
 (110,8,'20260505',2, 42000,'CARD','TID-42000','AP-110','48058','Busan'),
 (111,1,'20260506',2, 38000,'CARD','TID-38000','AP-111','06236','Seoul'),
 (112,5,'20260506',2, 22000,'BANK','TID-22000','AP-112','13494','Seongnam'),
 (113,6,'20260507',2,120000,'CARD','TID-120000','AP-113','48058','Busan'),
 (114,7,'20260507',2, 15000,'CARD','TID-15000','AP-114','48058','Busan'),
 (115,2,'20260508',2, 89000,'CARD','TID-89000','AP-115','06236','Seoul');

-- historical order_items (auto order_item_id; reference seed order_id 101..115 + existing options 1..20)
INSERT INTO order_item (order_id, option_id, qty, unit_price) VALUES
 (101,1,2,15000), (102,2,1,89000), (103,9,1,159000), (104,11,1,28000),
 (105,8,1,45000), (106,14,1,19000), (107,12,1,210000), (108,17,1,34000),
 (109,2,1,89000), (110,19,1,42000), (111,13,1,38000), (112,18,1,22000),
 (113,1,8,15000), (114,4,3,5000), (115,16,1,89000),
 (101,4,1,5000), (105,5,1,6000), (113,11,2,28000);

-- historical inventory movements
INSERT INTO inventory_log (option_id, change_qty, reason, reg_date) VALUES
 (1,-2,'ORDER','20260501'), (2,-1,'ORDER','20260501'), (1,-8,'ORDER','20260507'),
 (1,100,'RESTOCK','20260430'), (8,-1,'ORDER','20260503'), (13,-1,'ORDER','20260506'),
 (12,-1,'ORDER','20260504'), (4,50,'RESTOCK','20260430');
