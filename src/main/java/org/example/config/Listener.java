package org.example.config;

//public class Listener implements ServletContextListener {
//    /**
//     * При запуске сервера данный класс прослушивается и инициализируется контекст
//     * @param sce
//     */
//    @Override
//    public void contextInitialized(ServletContextEvent sce) {
//            try {
//                MyConnectionPool.initializePool();
//                runLiquibaseMigrations(MyConnectionPool.getConnection());
//            } catch (SQLException | LiquibaseException | IOException e) {
//                System.out.println("Failed migration or connection with: " + e.getMessage());
//                e.printStackTrace();
//            } catch (ClassNotFoundException e) {
//                throw new RuntimeException(e);
//            }
//    }
//
//    @Override
//    public void contextDestroyed(ServletContextEvent sce) {
//        MyConnectionPool.closePool();
//    }
//}
