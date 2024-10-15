-- SEQUENCE: public.iban_seq

-- DROP SEQUENCE IF EXISTS public.iban_seq;

CREATE SEQUENCE IF NOT EXISTS public.iban_seq
    INCREMENT 1
    START 1000000000000
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

ALTER SEQUENCE public.iban_seq
    OWNER TO admin;