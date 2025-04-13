-- Create the TODO_ITEM table.
CREATE TABLE IF NOT EXISTS TODO_ITEM (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    due_date DATE,
    status VARCHAR(50),
    img_url TEXT,
    attached_doc_url TEXT
);