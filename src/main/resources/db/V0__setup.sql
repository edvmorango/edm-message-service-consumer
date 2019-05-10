create table message (
    uuid UUID PRIMARY KEY,
    message text,
    sender_uuid UUID,
    peer_uuid UUID,
    sender_payload JSON,
    peer_payload JSON,
    send_date TIMESTAMP,
    created_at TIMESTAMP,
    update_at TIMESTAMP
)
