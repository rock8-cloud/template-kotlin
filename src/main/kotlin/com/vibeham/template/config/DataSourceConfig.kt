package com.vibeham.template.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.net.URI
import javax.sql.DataSource

@Configuration
class DataSourceConfig {

    /**
     * Accepts DATABASE_URL in postgres:// format (as provided by managed Kubernetes services):
     *   postgres://user:pass@host:port/dbname?sslmode=require
     *
     * Falls back to individual env vars when DATABASE_URL is not set:
     *   DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD, DB_SSL_MODE
     */
    @Bean
    @Primary
    fun dataSource(): DataSource {
        val config = HikariConfig()
        val databaseUrl = System.getenv("DATABASE_URL")

        if (!databaseUrl.isNullOrBlank()) {
            val uri = URI.create(databaseUrl.replaceFirst(Regex("^postgres(ql)?://"), "http://"))
            var jdbcUrl = "jdbc:postgresql://${uri.host}:${uri.port}${uri.path}"
            if (uri.query != null) {
                val query = if (uri.query.contains("=")) uri.query else "${uri.query}=require"
                jdbcUrl += "?$query"
            }
            val parts = uri.userInfo.split(":", limit = 2)
            config.jdbcUrl = jdbcUrl
            config.username = parts[0]
            config.password = if (parts.size > 1) parts[1] else ""
        } else {
            val host = env("DB_HOST", "localhost")
            val port = env("DB_PORT", "5432")
            val name = env("DB_NAME", "spacedb")
            val sslMode = System.getenv("DB_SSL_MODE")
            var jdbcUrl = "jdbc:postgresql://$host:$port/$name"
            if (!sslMode.isNullOrBlank()) jdbcUrl += "?sslmode=$sslMode"
            config.jdbcUrl = jdbcUrl
            config.username = env("DB_USER", "postgres")
            config.password = env("DB_PASSWORD", "postgres")
        }

        return HikariDataSource(config)
    }

    private fun env(key: String, default: String) =
        System.getenv(key)?.takeIf { it.isNotBlank() } ?: default
}
