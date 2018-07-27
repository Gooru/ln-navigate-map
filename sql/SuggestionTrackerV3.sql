-- drop table suggestions_tracker

CREATE TABLE suggestions_tracker (
    id bigserial NOT NULL,
    user_id uuid NOT NULL,
    course_id uuid,
    unit_id uuid,
    lesson_id uuid,
    class_id uuid,
    collection_id uuid,
    suggested_content_id uuid NOT NULL,
    suggestion_origin text NOT NULL CHECK (suggestion_origin::text = ANY (ARRAY['system'::text, 'teacher'::text])),
    suggestion_originator_id text,
    suggestion_criteria text CHECK (suggestion_criteria::text = ANY (ARRAY['location'::text, 'performance'::text])),
    suggested_content_type text NOT NULL CHECK (suggested_content_type::text = ANY (ARRAY['course'::text, 'unit'::text, 'lesson'::text, 'collection'::text, 'assessment'::text, 'resource'::text, 'question'::text])),
    suggested_to text NOT NULL CHECK (suggested_to::text = ANY (ARRAY['student'::text, 'teacher'::text])),
    accepted boolean NOT NULL DEFAULT false,
    accepted_at timestamp without time zone,
    ctx jsonb,
    created_at timestamp without time zone DEFAULT timezone('UTC'::text, now()) NOT NULL,
    updated_at timestamp without time zone DEFAULT timezone('UTC'::text, now()) NOT NULL,
    CONSTRAINT st_pkey PRIMARY KEY (id)
);

ALTER TABLE suggestions_tracker OWNER TO nucleus;

CREATE INDEX st_uculc_idx ON suggestions_tracker using btree(user_id, course_id, unit_id, lesson_id, collection_id);

CREATE UNIQUE INDEX st_ucculcs_unq
    ON suggestions_tracker (user_id, class_id, course_id, unit_id, lesson_id, collection_id, suggested_content_id)
    where class_id is not null and collection_id is not null;

CREATE UNIQUE INDEX st_ucculs_unq
    ON suggestions_tracker (user_id, class_id, course_id, unit_id, lesson_id, suggested_content_id)
    where class_id is not null and collection_id is null;


CREATE UNIQUE INDEX st_uculcs_unq
    ON suggestions_tracker (user_id, course_id, unit_id, lesson_id, collection_id, suggested_content_id)
    WHERE class_id IS NULL and collection_id is not null;

CREATE UNIQUE INDEX st_uculs_unq
    ON suggestions_tracker (user_id, course_id, unit_id, lesson_id, suggested_content_id)
    WHERE class_id IS NULL and collection_id is null;
