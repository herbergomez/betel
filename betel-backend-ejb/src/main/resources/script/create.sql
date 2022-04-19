CREATE TABLE oy7x7yw0emxzzinx.Products (
	pr_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	pr_name varchar(100) NOT NULL,
	pr_short_description TEXT NULL,
	pr_large_description TEXT NULL,
	pr_price DECIMAL NULL,
	pr_unit_of_measure varchar(100) NULL,
	pr_user_creation varchar(100) NULL,
	pr_createion_date DATETIME NULL,
	pr_modify_date DATETIME NULL,
	pr_image BLOB NULL,
        pr_enabled BOOL DEFAULT false NULL,
	pr_version INT NULL
)
ENGINE=InnoDB
DEFAULT CHARSET=latin1
COLLATE=latin1_swedish_ci;


CREATE TABLE oy7x7yw0emxzzinx.Settings (
	set_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	set_name varchar(100) NOT NULL,
	set_value varchar(100) NULL,
	set_user_creation varchar(100) NULL,
	set_creation_date DATETIME NULL,
	set_modify_date DATETIME NULL,
	set_version INT NULL
)
ENGINE=InnoDB
DEFAULT CHARSET=latin1
COLLATE=latin1_swedish_ci;

CREATE TABLE oy7x7yw0emxzzinx.tools (
	tol_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	tol_name varchar(200) NOT NULL,
        tol_image BLOB NULL,
        tol_enabled BOOL DEFAULT false NULL,
	tol_user_creation varchar(100) NULL,
	tol_creation_date DATETIME NULL,
	tol_modify_date DATETIME NULL,
	tol_version INT NULL
)
ENGINE=InnoDB
DEFAULT CHARSET=latin1
COLLATE=latin1_swedish_ci;

ALTER TABLE oy7x7yw0emxzzinx.Products ADD CONSTRAINT Products_UN UNIQUE KEY (pr_name);
ALTER TABLE oy7x7yw0emxzzinx.Settings ADD CONSTRAINT Settings_UN UNIQUE KEY (set_name);


INSERT INTO oy7x7yw0emxzzinx.Settings(set_name, set_value, set_user_creation, set_creation_date, set_modify_date, set_version)
VALUES('OWNER_ACCOUNT', NULL, NULL, '2021-12-10 19:43:25', '2021-12-10 19:43:25', 1);

INSERT INTO oy7x7yw0emxzzinx.Settings(set_name, set_value, set_user_creation, set_creation_date, set_modify_date, set_version)
VALUES('OWNER_PERCENT', NULL, NULL, '2021-12-10 19:43:25', '2021-12-10 19:43:25', 1);

CREATE TABLE oy7x7yw0emxzzinx.Persons (
	per_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	per_names varchar(50) NOT NULL,
        per_last_names varchar(50) NOT NULL,
        per_full_surname varchar(150) NULL,
        per_civil_state varchar(40) NULL,
        per_gender varchar(40) NULL,
        per_direction varchar(250) NULL,
        per_phone_number varchar(40) NULL,
        per_person_type varchar(40) not NULL,
        per_image BLOB NULL,
        per_birthdate DATE NULL,
	per_createion_date DATETIME NULL,
	per_modify_date DATETIME NULL,
	per_version INT NULL
)
ENGINE=InnoDB
DEFAULT CHARSET=latin1
COLLATE=latin1_swedish_ci;

CREATE TABLE oy7x7yw0emxzzinx.Users (
	usr_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	usr_email varchar(100) NOT NULL,
	usr_username varchar(50) NULL,
        usr_password varchar(50) NOT NULL,
        usr_tools varchar(50) NULL,
        usr_blocked BOOL DEFAULT false NULL,
        usr_accept_police BOOL default false null,
        usr_status varchar(50) NOT NULL DEFAULT 'CREATED',
        usr_worker_status varchar(50) NULL ,
        usr_basic_information_completed BOOL DEFAULT false NULL,
        usr_reset_password varchar(150) NULL,
        usr_login_fail_count varchar(40) NULL,
        usr_blocked_date DATETIME NULL,
        usr_reset_password_date DATETIME NULL,
	usr_creation_date DATETIME NULL,
	usr_modify_date DATETIME NULL,
        usr_per_id BIGINT not null,
	usr_version INT NULL,
    CONSTRAINT users_fk FOREIGN KEY (usr_per_id) REFERENCES oy7x7yw0emxzzinx.Persons (per_id) ON DELETE CASCADE ON UPDATE CASCADE
)
ENGINE=InnoDB
DEFAULT CHARSET=latin1
COLLATE=latin1_swedish_ci;

CREATE TABLE oy7x7yw0emxzzinx.Credit_Cards (
	cc_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	cc_card_number varchar(20) NOT NULL,
	cc_expiration_date varchar(10) NULL,
	cc_card_cvv varchar(10) NULL,
	cc_postal_code varchar(10) NULL,
	cc_phone_number varchar(10) NULL,
	cc_user_id bigint(20) NOT NULL,
	cc_user_creation varchar(100) NULL,
	cc_creation_date DATETIME NULL,
	cc_modify_date DATETIME NULL,
	cc_version INT NULL,
	CONSTRAINT user_fk FOREIGN KEY (cc_user_id) REFERENCES oy7x7yw0emxzzinx.Users (usr_id) ON DELETE CASCADE ON UPDATE CASCADE
)
ENGINE=InnoDB
DEFAULT CHARSET=latin1
COLLATE=latin1_swedish_ci;

CREATE TABLE oy7x7yw0emxzzinx.knowledges (
    kn_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    kn_description varchar(30) NOT NULL,
    kn_year_experience varchar(30)  null,
    kn_year_experience_min numeric(10)  null,
    kn_year_experience_max numeric(10)  null,
    kn_reference_names varchar(50)  NULL,
    kn_reference_phone varchar(30)  NULL,
    kn_creation_date DATETIME  NULL,
    kn_modify_date DATETIME NULL,
    kn_usr_id BIGINT not null,
    kn_version INT NULL,
    CONSTRAINT kn_user_fk FOREIGN KEY (kn_usr_id) REFERENCES oy7x7yw0emxzzinx.Users (usr_id) ON DELETE CASCADE ON UPDATE CASCADE
)
ENGINE=InnoDB
DEFAULT CHARSET=latin1
COLLATE=latin1_swedish_ci;



CREATE TABLE oy7x7yw0emxzzinx.purchase_orders (
    po_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    po_status varchar(30) NOT NULL,
    po_workflow varchar(30) NULL,
    po_latitude numeric(10,8)  NULL,
    po_longitude numeric(10,8)  NULL,
    po_address varchar(100) NULL,
    po_total_cost numeric(10,2)  null,
    po_total_tax numeric(10,2)  null,
    po_total_worker numeric(10,2)  null,
    po_total_cost_plus_tax numeric(10,2)  null,
    po_creation_date DATETIME  NULL,
    po_modify_date DATETIME NULL,
    po_usr_id BIGINT not null,
    po_version INT NULL,
    CONSTRAINT po_user_fk FOREIGN KEY (po_usr_id) REFERENCES oy7x7yw0emxzzinx.Users (usr_id) ON DELETE CASCADE ON UPDATE CASCADE
)
ENGINE=InnoDB
DEFAULT CHARSET=latin1
COLLATE=latin1_swedish_ci;


CREATE TABLE oy7x7yw0emxzzinx.purchase_orders_items (
    poi_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    poi_product_id BIGINT not null,
    poi_po_id BIGINT not null,
    poi_unit_of_measure varchar(30) NOT NULL,
    poi_unit_cost numeric(10,2) not null,
    poi_count_uom numeric(10,2) not null,
    poi_total_cost numeric(10,2) not null,
    poi_creation_date DATETIME not NULL,
    poi_modify_date DATETIME NULL,
    poi_version INT NULL,
    CONSTRAINT poi_po_fk FOREIGN KEY (poi_po_id) REFERENCES oy7x7yw0emxzzinx.purchase_orders (po_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT poi_product_fk FOREIGN KEY (poi_product_id) REFERENCES oy7x7yw0emxzzinx.Products (pr_id) ON DELETE CASCADE ON UPDATE CASCADE
)
ENGINE=InnoDB
DEFAULT CHARSET=latin1
COLLATE=latin1_swedish_ci;

CREATE TABLE oy7x7yw0emxzzinx.purchase_orders_workers (
    pow_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    pow_po_id BIGINT not null,
    pow_worker_id BIGINT not null,
    pow_payment numeric(10,2) not null,
    pow_rating numeric(10,2) null,
    pow_rated bool null,
    pow_creation_date DATETIME not NULL,
    pow_modify_date DATETIME NULL,
    pow_version INT NULL,
    CONSTRAINT pow_po_fk FOREIGN KEY (pow_po_id) REFERENCES oy7x7yw0emxzzinx.purchase_orders (po_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT pow_worker_fk FOREIGN KEY (pow_worker_id) REFERENCES oy7x7yw0emxzzinx.Users (usr_id) ON DELETE CASCADE ON UPDATE CASCADE
)
ENGINE=InnoDB
DEFAULT CHARSET=latin1
COLLATE=latin1_swedish_ci;
