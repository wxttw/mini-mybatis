# Implementation of Mini Mybatis (Version 1)

### The main functions implemented are as follows:
1. Parse the core configuration file and Mapper configuration file through Dom4J, and encapsulate the corresponding entity.
2. Expose core operations such as CRUD api, transaction api, and generated mapper proxy api through the SqlSession facade.
3. According to the principle of single responsibility, divide the functions into finer granularity and implement the executor, statement handler, parameter handler, and resultset handler. To implement SQL execution, SQL preparation, parameter binding, result set encapsulation, and other operations.
4. Implement a type processor. Bind parameters to type handlers during BoundSql generation, and automatically process parameters based on type during parameterization.
5. Implement the log module to integrate multiple logging frameworks.
6. Others, such as transaction modules, type aliases, etc

### Test & Run
1. Execute file "sql/user.sql" in mysql for testing purposes
2. In folder "src/test/resources", configure the database connection information to "mybatis-config.xml"
3. Then run the test case in "src/test/java"


### The features planned to be implemented in the next version:
1. Implement its caching mechanism, session/mapper cache
2. Implement its plugin mechanism
3. Implement its integration with Spring (through spring extension points)
4. Annotated configuration
5. Improve the version one: implement the output of statement logs (Connection proxy); The resultset is automatically mapped to parameters through TypeHandler; Supporting more types of TypeHandlers
6. Introduce Mybatis's more comprehensive reflection module


