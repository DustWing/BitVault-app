package com.bitvault.database.utils;

public class DbScripts {

    public final static String t_users = """
            CREATE TABLE IF NOT EXISTS t_users
                    (
                        id              TEXT NOT NULL PRIMARY KEY,
                        name            TEXT NOT NULL,
                        credential      TEXT NOT NULL
                       
                    );
            """;


    public final static String t_passwords = """
            CREATE TABLE IF NOT EXISTS t_passwords
                    (
                        id              TEXT NOT NULL PRIMARY KEY,
                        username        TEXT not null,
                        password        TEXT not null,
                        secure_details  TEXT not null,
                        FOREIGN KEY(secure_details) REFERENCES t_secure_details(id)
                    );
            """;

    public final static String t_passwords_audit = """
            CREATE TABLE IF NOT EXISTS t_passwords
                    (
                        id              TEXT NOT NULL PRIMARY KEY,
                        password_id     TEXT NOT NULL,
                        username        TEXT not null,
                        password        TEXT not null,
                        secure_details  TEXT not null,
                        FOREIGN KEY(password_id) REFERENCES t_passwords(id)
                    );
            """;

    public final static String t_secure_details = """
            CREATE TABLE IF NOT EXISTS t_secure_details
                    (
                        id              TEXT NOT NULL PRIMARY KEY,
                        category_id     TEXT not null,
                        profile_id      TEXT not null,
                        domain          TEXT,
                        title           TEXT,
                        description     TEXT,
                        favourite       INTEGER not null,
                        created_on      TEXT not null,
                        modified_on     TEXT,
                        expires_on      TEXT,
                        imported_on     TEXT,
                        requires_mp     INTEGER,
                        shared          INTEGER
                    );
            """;


    public final static String t_secure_details_audit = """
            CREATE TABLE IF NOT EXISTS t_secure_details
                    (
                        id                  TEXT NOT NULL PRIMARY KEY,
                        secure_details_id   TEXT not null,
                        category_id         TEXT not null,
                        profile_id          TEXT not null,
                        domain              TEXT,
                        title               TEXT,
                        description         TEXT,
                        favourite           INTEGER not null,
                        created_on          TEXT not null,
                        modified_on         TEXT,
                        expires_on          TEXT,
                        imported_on         TEXT,
                        requires_mp         INTEGER,
                        shared              INTEGER
                    );
            """;


    public final static String t_categories = """
            CREATE TABLE IF NOT EXISTS t_categories
                    (
                        id              TEXT NOT NULL PRIMARY KEY,
                        name            TEXT not null,
                        color           TEXT,
                        created_on      TEXT not null,
                        modified_on     TEXT,
                        type            TEXT not null,
                        deleted         INTEGER not null DEFAULT 0
                    );
            """;

    public final static String t_profiles = """
                  CREATE TABLE IF NOT EXISTS t_profiles
                        (
                            id              TEXT NOT NULL PRIMARY KEY,
                            name            TEXT not null,
                            created_on      TEXT not null,
                            modified_on     TEXT,
                            deleted         INTEGER not null DEFAULT 0
                        );
            """;

    public final static String t_domain_details = """
                  CREATE TABLE IF NOT EXISTS t_domain_details
                        (
                            id          TEXT NOT NULL PRIMARY KEY,
                            name        TEXT not null,
                            domain      TEXT not null,
                            icon        TEXT not null
                        );
            """;


    public final static String t_settings = """
                  CREATE TABLE IF NOT EXISTS t_settings
                        (
                            name            TEXT NOT NULL PRIMARY KEY,
                            value           TEXT NOT NULL,
                            created_on      TEXT not null,
                            modified_on     TEXT
                        );
            """;


}
