-- SEQUENCE: public.account_number_seq

-- DROP SEQUENCE IF EXISTS public.account_number_seq;

CREATE SEQUENCE IF NOT EXISTS public.account_number_seq
    INCREMENT 1
    START 1000000000000
    MINVALUE 1
    MAXVALUE 9999999999999
    CACHE 1;

ALTER SEQUENCE public.account_number_seq
    OWNER TO admin;