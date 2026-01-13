ALTER TABLE operation
    ADD COLUMN operation_date TIMESTAMPTZ;

ALTER TABLE auth_devices
DROP COLUMN ip,
DROP COLUMN device_id,
DROP COLUMN user_agent,
ADD COLUMN platform_name VARCHAR(100),
ADD COLUMN refresh_token TEXT;
