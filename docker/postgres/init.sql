create table pay_fee
(
    id     varchar(255) not null,
    status varchar(255) not null,
    primary key (id)
);

create table cooperation_agreement
(
    id         varchar(255) not null,
    pay_fee_id varchar(255) not null,
    primary key (id)
);

alter table if exists cooperation_agreement
    add constraint fk_cooperation_agreement_pay_fee_id foreign key (pay_fee_id) references pay_fee;


insert into pay_fee (id, status)
values ('O0001', 'PENDING');

insert into cooperation_agreement (id, pay_fee_id)
values ('SA001', 'O0001');