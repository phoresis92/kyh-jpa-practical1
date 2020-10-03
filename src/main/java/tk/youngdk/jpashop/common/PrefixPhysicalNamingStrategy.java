package tk.youngdk.jpashop.common;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;

public class PrefixPhysicalNamingStrategy extends SpringPhysicalNamingStrategy {

    private static final String PREFIX = "t_";

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        Identifier identifier = new Identifier(PREFIX + name.getText(), name.isQuoted());
        return super.toPhysicalTableName(identifier, context);
    }

}
