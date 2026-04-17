-- DROP SCHEMA public;

CREATE SCHEMA public AUTHORIZATION pg_database_owner;

COMMENT ON SCHEMA public IS 'standard public schema';

-- DROP SEQUENCE public.audit_logs_id_seq;

CREATE SEQUENCE public.audit_logs_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE public.funds_id_seq;

CREATE SEQUENCE public.funds_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE public.portfolio_id_seq;

CREATE SEQUENCE public.portfolio_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE public.transactions_id_seq;

CREATE SEQUENCE public.transactions_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE public.users_id_seq;

CREATE SEQUENCE public.users_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;-- public.audit_logs definition

-- Drop table

-- DROP TABLE public.audit_logs;

CREATE TABLE public.audit_logs (
	id bigserial NOT NULL,
	user_id int8 NULL,
	"action" varchar(50) NULL,
	request_data text NULL,
	created_at timestamp DEFAULT CURRENT_TIMESTAMP NULL,
	CONSTRAINT audit_logs_pkey PRIMARY KEY (id)
);


-- public.funds definition

-- Drop table

-- DROP TABLE public.funds;

CREATE TABLE public.funds (
	id bigserial NOT NULL,
	fund_name varchar(150) NOT NULL,
	nav numeric(12, 4) NOT NULL,
	last_updated timestamp NOT NULL,
	CONSTRAINT funds_pkey PRIMARY KEY (id)
);


-- public.users definition

-- Drop table

-- DROP TABLE public.users;

CREATE TABLE public.users (
	id bigserial NOT NULL,
	"name" varchar(100) NULL,
	email varchar(150) NOT NULL,
	"password" varchar(255) NOT NULL,
	created_at timestamp DEFAULT CURRENT_TIMESTAMP NULL,
	CONSTRAINT users_email_key UNIQUE (email),
	CONSTRAINT users_pkey PRIMARY KEY (id)
);


-- public.portfolio definition

-- Drop table

-- DROP TABLE public.portfolio;

CREATE TABLE public.portfolio (
	id bigserial NOT NULL,
	user_id int8 NOT NULL,
	fund_id int8 NOT NULL,
	total_units numeric(12, 4) NOT NULL,
	total_investment numeric(15, 2) NOT NULL,
	last_updated timestamp DEFAULT CURRENT_TIMESTAMP NULL,
	CONSTRAINT portfolio_pkey PRIMARY KEY (id),
	CONSTRAINT portfolio_user_id_fund_id_key UNIQUE (user_id, fund_id),
	CONSTRAINT portfolio_fund_id_fkey FOREIGN KEY (fund_id) REFERENCES public.funds(id),
	CONSTRAINT portfolio_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id)
);


-- public.transactions definition

-- Drop table

-- DROP TABLE public.transactions;

CREATE TABLE public.transactions (
	id bigserial NOT NULL,
	user_id int8 NOT NULL,
	fund_id int8 NOT NULL,
	"type" varchar(10) NULL,
	units numeric(12, 4) NOT NULL,
	amount numeric(15, 2) NOT NULL,
	nav_at_transaction numeric(12, 4) NOT NULL,
	idempotency_key varchar(100) NOT NULL,
	created_at timestamp DEFAULT CURRENT_TIMESTAMP NULL,
	CONSTRAINT transactions_pkey PRIMARY KEY (id),
	CONSTRAINT transactions_type_check CHECK (((type)::text = ANY ((ARRAY['BUY'::character varying, 'SELL'::character varying])::text[]))),
	CONSTRAINT transactions_user_id_idempotency_key_key UNIQUE (user_id, idempotency_key),
	CONSTRAINT transactions_fund_id_fkey FOREIGN KEY (fund_id) REFERENCES public.funds(id),
	CONSTRAINT transactions_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id)
);