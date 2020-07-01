-- This script is meant for development purposes only
-- It will be executed in the local DB container

-- Read token
-- Token: l7kowOOkliu21oXxNpuCyM47u2omkysxb8lv3qEhm5U
INSERT INTO dbo.Token(hash,isValid,issuedAt,expiresAt,claims) VALUES ('5f5efa16d66d290bb31667bfffd6d9e37776a862ef116cbaa415f650a7283c0e','t',1593338117608,9999999999999,'{"scope":"urn:org:ionproject:scopes:api:read", "client_id": 500}');

-- Write token
-- Token: hfk0DXJ9LIPuhvrjDEmhYRv5Z0YRhOl1DMEEPIp42ok
INSERT INTO dbo.Token(hash,isValid,issuedAt,expiresAt,claims) VALUES ('58e46fa9f1441d4fe9798ce1015eeab9231aa66cdee7638177c33a1b64ab534c','t',1593338126153,9999999999999,'{"scope":"urn:org:ionproject:scopes:api:write", "client_id": 500}');

-- Issue token
-- Token: vUG-N_m_xVohFrnXcu2Jmt_KAeKfxQXV2LkLjJF4144
INSERT INTO dbo.Token(hash,isValid,issuedAt,expiresAt,claims) VALUES ('1784968c5536e2ed449507982b9cc281017a0a74c41d96f02dab31bbb7c6138f','t',1593338132242,9999999999999,'{"scope":"urn:org:ionproject:scopes:token:issue", "client_id": 500}');

