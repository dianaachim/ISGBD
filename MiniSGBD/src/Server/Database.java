package Server;

import java.util.ArrayList;
import java.util.List;

public class Database {

        int databaseId;
        String name;
        private List<Table> tables;

        public Database(int databaseId, String name, List<Table> tables) {
            this.databaseId = databaseId;
            this.name = name;
            this.tables = tables != null ? tables : new ArrayList<>();
        }

        public Database(String name, List<Table> tables) {
            this.name = name;
            this.tables = tables != null ? tables : new ArrayList<>();
        }

        public int getDatabaseId() {
            return databaseId;
        }

        public void setDatabaseId(int databaseId) {
            this.databaseId = databaseId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Table> getTables() {
            return tables;
        }

        public void setTables(List<Table> tables) {
            this.tables = tables;
        }

        public void addTableToDb(Table tb) {
            tables.add(tb);
        }

        public void deleteTableFromDb(String tableName) {
            int index = -1;
            for (Table tb : tables) {
                if (tb.getName().equals(tableName)) {
                    index = tables.indexOf(tb);
                }
            }

            if (index > -1) {
                tables.remove(index);
            }

        }

        @Override
        public String toString() {
            return "Database{" + "databaseId=" + databaseId + ", name=" + name + ", tables=" + tables + '}';
        }

    }

