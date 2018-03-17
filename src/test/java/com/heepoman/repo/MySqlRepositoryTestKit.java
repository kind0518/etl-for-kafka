package com.heepoman.repo;

import com.heepoman.repo.driver.MySqlDriver;
import org.junit.Before;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class MySqlRepositoryTestKit {

  private MySqlDriver driver = new MySqlDriver();

  protected Long eventId = 12345678911L;
  protected String eventTimestamp = new Date().toString();
  protected String serviceCode = "SERVICE_CODE";
  protected String eventContext = "EVENT_CONTEXT";

  @Before
  public void initForTest() throws SQLException {
    Connection conn = driver.getConnection();
    Statement stmt = conn.createStatement();

    String initTableQ = "DROP TABLE IF EXISTS event CASCADE;";
    String createTableQ = "CREATE TABLE event(event_id BIGINT not null, event_timestamp character varying(255) not null, service_code character varying(255), event_context character varying(255));";
    String insertDefaultEventQ =
            String.format(
                    "INSERT INTO event(event_id, event_timestamp, service_code, event_context) VALUES(%d, '%s', '%s', '%s')",
                    eventId,
                    eventTimestamp,
                    serviceCode,
                    eventContext);

    try {
      stmt.addBatch(initTableQ);
      stmt.addBatch(createTableQ);
      stmt.addBatch(insertDefaultEventQ);
      stmt.executeBatch();
    } finally {
      if (conn != null) {
        conn.close();
      }
      if (stmt != null) {
        stmt.close();
      }
    }
  }

}
