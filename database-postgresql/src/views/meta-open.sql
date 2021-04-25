CREATE VIEW meta_open AS
    SELECT
        m.id
      , m.metakey
      , m.opens
      , m.edits
      , m.is_deleted
      , m.created_at
      , m.edited_at
      , m.deleted_at
      , created_by.username AS created_by
      , edited_by.username AS edited_by
      , deleted_by.username AS deleted_by
      , m.additional_notes
    FROM meta m
    LEFT OUTER JOIN users created_by ON m.created_by = created_by.id
    LEFT OUTER JOIN users edited_by ON m.edited_by = edited_by.id
    LEFT OUTER JOIN users deleted_by ON m.deleted_by = deleted_by.id;
