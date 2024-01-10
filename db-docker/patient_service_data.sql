USE patient_service;
CREATE TABLE IF NOT EXISTS patients
(
    id int auto_increment primary key,
    first_name   varchar(25)    not null,
    last_name    varchar(25)    not null,
    birth_date   varchar(10)    not null,
    gender       enum ('Male','Female') not null,
    address      varchar(100)    null,
    phone        varchar(20)     null
) ENGINE = innoDB;
commit;

INSERT INTO patients(first_name, last_name, birth_date,gender,address,phone)
VALUES  ('Lucas','Ferguson','1968-06-22','Male','2 Warren Street','387-866-1399'),
        ('Pippa','Rees','1952-09-27','Female','745 West Valley Farms Drive','628-423-0993'),
        ('Edward','Arnold','1952-11-11','Male','599 East Garden Ave','123-727-2779'),
        ('Anthony','Sharp','1946-11-26','Male','894 Hall Street','451-761-8383'),
        ('Wendy','Ince','1958-06-29','Female','4 Southampton Road','802-911-9975'),
        ('Tracey','Ross','1949-12-07','Female','40 Sulphur Springs Dr','131-396-5049'),
        ('Claire','Wilson','1966-12-31','Female','12 Cobblestone St','300-452-1091'),
        ('Max','Buckland','1945-06-24','Male', '193 Vale St','833-534-0864'),
        ('Natalie ','Clark','1964-06-18','Female','12 Beechwood Road','241-467-9197'),
        ('Piers','Bailey','1959-06-28','Male','1202 Bumble Dr','747-815-0557'),
        ('Mary','Hope','2023-04-01','Female','534 Rose Dr','441-874-6510'),
        ('Ryder','Russell','2023-05-28','Male','534 King Dr','441-874-6510'),
        ('John','Walker','2023-07-15','Male','27 Queen Ave','441-874-6990'),
        ('Stephania','Ritz','2000-01-19','Female','57A Town-drive Road','241-874-4465'),
        ('Gordon','Ramsay','1966-08-10','Male','178 London Drive','556-546-1897'),
        ('Alex','Torv','1964-07-22','Male','1133 King street','555-333-2222');
commit;