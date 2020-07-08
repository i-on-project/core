package org.ionproject.core.accessControl.pap.sql

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.ionproject.core.accessControl.TokenGenerator
import org.ionproject.core.accessControl.pap.entities.*
import org.ionproject.core.accessControl.pap.sql.AuthRepoData.API_VERSION
import org.ionproject.core.accessControl.pap.sql.AuthRepoData.GET_IMPORT_TOKENS
import org.ionproject.core.accessControl.pap.sql.AuthRepoData.GET_POLICIES_QUERY
import org.ionproject.core.accessControl.pap.sql.AuthRepoData.GET_TOKEN_QUERY
import org.ionproject.core.accessControl.pap.sql.AuthRepoData.INSERT_TOKEN_QUERY
import org.ionproject.core.accessControl.pap.sql.AuthRepoData.REVOKE_TOKEN_QUERY
import org.ionproject.core.accessControl.pap.sql.AuthRepoData.SCOPE_URI
import org.ionproject.core.accessControl.pap.sql.AuthRepoData.TOKEN
import org.ionproject.core.common.transaction.TransactionManager
import org.jdbi.v3.core.mapper.RowMapper
import org.springframework.stereotype.Repository
import java.lang.Exception

@Repository
class AuthRepoImpl(private val tm: TransactionManager) : AuthRepo {
    private val tokenMapper = TokenMapper()
    private val derivedTokenMapper = DerivedTokenMapper()
    private val policyMapper = PolicyMapper()

    private val tokenGenerator = TokenGenerator()

    override fun getTableToken(tokenHash: String): TokenEntity? = tm.run { handle ->
        {
            handle.createQuery(GET_TOKEN_QUERY)
                .bind(TOKEN, tokenHash)
                .map(tokenMapper)
                .firstOrNull()
        }()
    }

    /**
     * Avoids having to add if's checking if its meant to build a derived token or not
     */
    override fun getDerivedTableToken(tokenHash: String): TokenEntity? = tm.run { handle ->
        {
            handle.createQuery(GET_TOKEN_QUERY)
                .bind(TOKEN, tokenHash)
                .map(derivedTokenMapper)
                .firstOrNull()
        }()
    }

    override fun getPolicies(scope: String, apiVersion: String): List<PolicyEntity> = tm.run { handle ->
        handle.createQuery(GET_POLICIES_QUERY)
            .bind(SCOPE_URI, scope)
            .bind(API_VERSION, apiVersion)
            .map(policyMapper)
            .list()
    } as List<PolicyEntity>

    override fun storeToken(token: TokenEntity): Boolean = tm.run { handle ->
        {
            val mapper = jacksonObjectMapper()
            val claimsData = mapper.writeValueAsString(token.claims)

            val result = handle.execute(
                INSERT_TOKEN_QUERY,
                token.hash,
                token.isValid,
                token.issuedAt,
                token.expiresAt,
                token.derivedToken,
                claimsData
            )
            result > 0
        }()
    }

    override fun revokeToken(hash: String): Boolean = tm.run { handle ->
        {
            val result = handle.execute(REVOKE_TOKEN_QUERY, hash)
            result > 0
        }()
    }

    /**
     * First checks to see if there already exists an import url token to the requested resource,
     * if it doesn't, creates one.
     */
    override fun generateImportToken(url: String, fatherTokenHash: String): String = tm.run { handle ->
        {
            val importTokens = handle.createQuery(GET_IMPORT_TOKENS)
                .map(derivedTokenMapper)
                .list()

            //Check if there already exists a token for the current URL
            var tokenReference = ""
            for(tokenDb : TokenEntity in importTokens) {
                val claims = tokenDb.claims as DerivedTokenClaims

                if(claims.uri == url) {
                    tokenReference = claims.derivedTokenReference
                    break
                }
            }

            if(tokenReference == "") {
                //No token found for the requesting resource, generate new token
                val token = generateToken(url, fatherTokenHash)

                val mapper = jacksonObjectMapper()
                val claimsData = mapper.writeValueAsString(token.claims)

                val result = handle.execute(
                    INSERT_TOKEN_QUERY,
                    token.hash,
                    token.isValid,
                    token.issuedAt,
                    token.expiresAt,
                    token.derivedToken,
                    claimsData
                )

                if(result == 0)
                    throw Exception("Something Unexpected happened during the import url token generation.")

                tokenReference = (token.claims as DerivedTokenClaims).derivedTokenReference
            }

            tokenReference
        }()
    }

    /**
     * It's more optimized to build the hash, token... after its confirmed there is no token for the resource
     */
    private fun generateToken(url: String, fatherTokenHash: String) : TokenEntity {
        val tokenReferenceBytes = tokenGenerator.generateRandomString()
        val tokenReferenceString = tokenGenerator.encodeBase64url(tokenReferenceBytes)
        val derivedTokenHash = tokenGenerator.getHash(tokenReferenceBytes)

        return tokenGenerator.buildDerivedToken(fatherTokenHash, derivedTokenHash, url, tokenReferenceString)
    }
}