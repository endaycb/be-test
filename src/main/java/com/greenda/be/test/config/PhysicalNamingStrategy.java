package com.greenda.be.test.config;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class PhysicalNamingStrategy extends PhysicalNamingStrategyStandardImpl {

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        return convertToUpperSnakeCase(name);
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
        return convertToUpperSnakeCase(name);
    }

    private Identifier convertToUpperSnakeCase(Identifier name) {
        if (name == null) return null;

        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";
        String newName = name.getText()
                .replaceAll(regex, replacement)
                .toUpperCase();

        return Identifier.toIdentifier(newName);
    }
}
