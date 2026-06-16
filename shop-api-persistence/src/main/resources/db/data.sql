INSERT INTO product (name, category, price, seller_name, status) VALUES
 ('Wireless Mouse','ELECTRONICS',15000,'Acme Corp','ON_SALE'),
 ('Mechanical Keyboard','ELECTRONICS',89000,'Acme Corp','ON_SALE'),
 ('USB-C Cable','ACCESSORY',5000,'Cable Co','ON_SALE'),
 ('Old Monitor','ELECTRONICS',120000,'Acme Corp','SOLD_OUT'),
 ('Laptop Stand','ACCESSORY',32000,'Deskwork','ON_SALE'),
 ('HD Webcam','ELECTRONICS',45000,'Acme Corp','ON_SALE'),
 ('Noise-Cancelling Headset','ELECTRONICS',159000,'SoundLab','ON_SALE'),
 ('USB Hub 7-Port','ACCESSORY',28000,'Cable Co','ON_SALE'),
 ('Office Chair','HOME',210000,'Deskwork','ON_SALE'),
 ('LED Desk Lamp','HOME',38000,'Deskwork','ON_SALE'),
 ('Cotton T-Shirt','FASHION',19000,'Wearwell','ON_SALE'),
 ('Running Shoes','SPORTS',89000,'FleetFoot','ON_SALE'),
 ('Yoga Mat','SPORTS',34000,'FleetFoot','ON_SALE'),
 ('Stainless Tumbler','HOME',22000,'Wearwell','ON_SALE'),
 ('Programming Java (Book)','BOOKS',42000,'TechPress','ON_SALE'),
 ('Vintage Film Camera','ELECTRONICS',350000,'SoundLab','SOLD_OUT');

INSERT INTO product_option (product_id, option_name, extra_price, stock_qty) VALUES
 (1,'Standard',0,100),
 (2,'Brown Switch',0,50),
 (2,'Blue Switch',5000,0),
 (3,'1m',0,200),
 (3,'2m',1000,200),
 (4,'27inch',0,0),
 (5,'Aluminum',0,80),
 (6,'1080p',0,60),
 (7,'Black',0,40),
 (7,'White',0,25),
 (8,'Standard',0,150),
 (9,'Mesh / Black',0,30),
 (10,'Warm White',0,90),
 (11,'M',0,120),
 (11,'L',0,110),
 (12,'265mm',0,45),
 (13,'6mm',0,70),
 (14,'500ml',0,200),
 (15,'Paperback',0,300),
 (16,'35mm',0,0);

INSERT INTO member (login_id, name, grade, point) VALUES
 ('alice','Alice','A',10000),
 ('bob','Bob','B',500),
 ('carol','Carol','C',0);

INSERT INTO coupon (code, discount_type, discount_val, min_order, expire_date, used_yn) VALUES
 ('WELCOME10','R',10,10000,'20991231','N'),
 ('SAVE5000','F',5000,50000,'20991231','N'),
 ('EXPIRED','F',3000,0,'20200101','N');
