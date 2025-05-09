-- Inserção das authorities
INSERT INTO authority (id, name) VALUES
    (gen_random_uuid(), 'user-profile-view'),
    (gen_random_uuid(), 'user-profile-edit'),
    (gen_random_uuid(), 'user-list'),
    (gen_random_uuid(), 'user-admin');

-- Inserção das roles
INSERT INTO role (id, name) VALUES
    (gen_random_uuid(), 'user'),
    (gen_random_uuid(), 'admin');

-- Associações da role 'user' com suas authorities
INSERT INTO role_authority (role_id, authority_id)
SELECT r.id, a.id FROM role r, authority a
WHERE r.name = 'user' AND a.name IN ('user-profile-view', 'user-profile-edit');

-- Associações da role 'admin' com todas as authorities
INSERT INTO role_authority (role_id, authority_id)
SELECT r.id, a.id FROM role r, authority a
WHERE r.name = 'admin';

-- Criação do usuário admin com senha criptografada
-- Senha: CAlvo#2025 (BCrypt)
INSERT INTO "user" (id, email, name, password, enabled, locked, created_date)
VALUES (
    gen_random_uuid(),
    'admin@codigoalvo.com.br',
    'Admin',
    '$2a$10$F38kb.x25bDIax7Bok5obORJL6VbuLRw2UkoSODcTntEXwCqU6Cn6',
    TRUE,
    FALSE,
    NOW()
);

-- Associação do usuário admin à role 'admin'
INSERT INTO user_role (user_id, role_id)
SELECT u.id, r.id FROM "user" u, role r
WHERE u.email = 'admin@codigoalvo.com.br' AND r.name = 'admin';
