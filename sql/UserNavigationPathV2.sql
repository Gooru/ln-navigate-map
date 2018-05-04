-- This is self referencing. We need validate if we should also store the hierarchy information
-- of parent trees here
CREATE TABLE user_navigation_paths (
    id bigserial NOT NULL,
    ctx_user_id uuid NOT NULL,
    ctx_course_id uuid NOT NULL,
    ctx_unit_id uuid,
    ctx_lesson_id uuid,
    ctx_class_id uuid,
    ctx_collection_id uuid,
    suggested_content_id uuid NOT NULL,
    suggestion_type text NOT NULL CHECK (suggestion_type::text = ANY (ARRAY['system'::text, 'teacher'::text, 'route0'::text])),
    suggested_content_type text NOT NULL CHECK (suggested_content_type::text = ANY (ARRAY['course'::text, 'unit'::text, 'lesson'::text, 'collection'::text, 'assessment'::text, 'resource'::text])),
    suggested_content_subtype varchar(128)  CHECK (suggested_content_subtype::text = ANY (ARRAY['signature-collection'::text, 'signature-assessment'::text, 'signature-resource'::text])),
    serve_count bigint NOT NULL DEFAULT 0,
    created_at timestamp without time zone DEFAULT timezone('UTC'::text, now()) NOT NULL,
    updated_at timestamp without time zone DEFAULT timezone('UTC'::text, now()) NOT NULL,
    CONSTRAINT unp_pkey PRIMARY KEY (id)
);

ALTER TABLE user_navigation_paths OWNER TO nucleus;

ALTER TABLE user_navigation_paths ADD CONSTRAINT ulc_st_null CHECK ((ctx_unit_id is null AND ctx_lesson_id is null AND ctx_collection_id is null AND suggestion_type = 'route0') OR (ctx_unit_id is not null AND ctx_lesson_id is not null AND ctx_collection_id is not null and suggestion_type <> 'route0'));

CREATE INDEX unp_uculc_idx ON user_navigation_paths using btree(ctx_user_id, ctx_course_id, ctx_unit_id, ctx_lesson_id, ctx_collection_id);

CREATE UNIQUE INDEX unp_uculccst_idx
    ON user_navigation_paths (ctx_user_id, ctx_course_id, ctx_unit_id, ctx_lesson_id, ctx_collection_id, ctx_class_id, suggested_content_id, suggestion_type)
    where ctx_class_id is not null and ctx_collection_id is not null;

CREATE UNIQUE INDEX unp_uculccst_null_ctx_class_id_idx
    ON user_navigation_paths (ctx_user_id, ctx_course_id, ctx_unit_id, ctx_lesson_id, ctx_collection_id, suggested_content_id, suggestion_type)
    WHERE ctx_class_id IS NULL and ctx_collection_id is not null;

CREATE UNIQUE INDEX unp_uculccst_null_coll_ctx_class_id_idx
    ON user_navigation_paths (ctx_user_id, ctx_course_id, ctx_unit_id, ctx_lesson_id, suggested_content_id, suggestion_type)
    WHERE ctx_class_id IS NULL and ctx_collection_id is null;

CREATE UNIQUE INDEX unp_uculccst_null_coll_idx
    ON user_navigation_paths (ctx_user_id, ctx_course_id, ctx_unit_id, ctx_lesson_id, ctx_class_id, suggested_content_id, suggestion_type)
    WHERE ctx_class_id IS not NULL and ctx_collection_id is null;

UPDATE collection set subformat = 'signature-assessment' where subformat = 'pre-test' OR subformat = 'post-test' OR subformat = 'benchmark';

-- Though there may not be any data here
UPDATE collection set subformat = 'signature-collection' where subformat = 'backfill';

-- drop table signature_items;

CREATE TABLE signature_items (
    id bigserial,
    competency_internal_code character varying(512) NOT NULL,
    competency_display_code character varying(512),
    competency_gut_code character varying(512),
    micro_competency_display_code character varying(512),
    micro_competency_internal_code character varying(521),
    micro_competency_gut_code character varying(512),
    performance_range character varying(32) NOT NULL,
    item_id character varying(36) NOT NULL,
    item_format varchar(128) NOT NULL CHECK (item_format::text = ANY (ARRAY['collection'::text, 'assessment'::text])),
    language character varying(256),
    engagement real DEFAULT 0,
    efficacy real DEFAULT 0,
    weight real DEFAULT 0,
    suggested_count bigint NOT NULL DEFAULT 0,
    is_curated boolean NOT NULL DEFAULT false,
    CONSTRAINT si_pkey PRIMARY KEY (id)
);

ALTER TABLE signature_items OWNER TO nucleus;
CREATE INDEX si_cic_pr ON signature_items using btree(competency_internal_code, performance_range);
CREATE INDEX si_cgc_pr ON signature_items using btree(competency_gut_code, performance_range);

