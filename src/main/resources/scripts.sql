CREATE TABLE public.users (
                              id uuid NOT NULL,
                              tg_id varchar(30) NOT NULL,
                              chat_id varchar(30) NOT NULL,
                              user_name varchar(100) NULL,
                              first_name varchar(100) NULL,
                              last_name varchar(100) NULL,
                              is_bot bool NOT NULL,
                              language_code varchar(10) NULL,
                              create_date timestamp NOT NULL,
                              CONSTRAINT users_pkey PRIMARY KEY (id)
);

CREATE TABLE public.analytics (
                                  id uuid NOT NULL,
                                  user_id varchar(30) NOT NULL,
                                  command varchar(30) NULL,
                                  message_id varchar(30) NULL,
                                  text varchar(3000) NULL,
                                  callback varchar(5000) NULL,
                                  create_date timestamp NOT NULL,
                                  CONSTRAINT analytics_pkey PRIMARY KEY (id)
);