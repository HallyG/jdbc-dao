package io.github.hallyg.dao.user;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import io.github.hallyg.dao.util.QueryRunner;
import io.github.hallyg.db.Database;
import io.github.hallyg.domain.User;
import java.sql.*;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserDaoDbTest {
  @Mock private Database mockDatabase;
  @Mock private Connection mockConnection;
  @Mock private PreparedStatement mockPreparedStatement;
  @Mock private ParameterMetaData mockMetaData;
  @Mock private ResultSet mockResultSet;

  private QueryRunner runner;

  private static final Long USER_ID = 100L;
  private static final String USER_EMAIL = "test@email.com";

  @Before
  public void setUp() throws Exception {
    when(mockDatabase.getConnection()).thenReturn(mockConnection);
    when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    // when(mockConnection.prepareStatement(anyString(),
    // anyInt())).thenReturn(mockPreparedStatement);
    when(mockPreparedStatement.getParameterMetaData()).thenReturn(mockMetaData);
    when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    // when(mockPreparedStatement.getResultSet()).thenReturn(mockResultSet);

    this.runner = new QueryRunner(mockDatabase);
  }

  @Test
  public void findOneById() throws SQLException {
    when(mockMetaData.getParameterCount()).thenReturn(1);
    when(mockResultSet.next()).thenReturn(Boolean.TRUE);
    when(mockResultSet.getLong("user_id")).thenReturn(USER_ID);
    when(mockResultSet.getString("email")).thenReturn(USER_EMAIL);

    UserDaoDb instance = new UserDaoDb(runner);
    Optional<User> user = instance.findOneById(USER_ID);

    verify(mockConnection, times(1)).prepareStatement(anyString());
    verify(mockPreparedStatement, times(1)).setObject(anyInt(), anyLong());
    verify(mockPreparedStatement, times(1)).executeQuery();
    verify(mockResultSet, times(1)).next();

    assertTrue(user.isPresent());
    assertEquals(USER_ID, user.get().getId());
    assertEquals(USER_EMAIL, user.get().getEmail());
  }

  @Test
  public void findAll() throws SQLException {
    when(mockResultSet.next()).thenReturn(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
    when(mockResultSet.getLong("user_id")).thenReturn(USER_ID, USER_ID + 1);
    when(mockResultSet.getString("email")).thenReturn(USER_EMAIL, "test" + USER_EMAIL);

    UserDaoDb instance = new UserDaoDb(runner);
    List<User> user = instance.findAll();

    verify(mockConnection, times(1)).prepareStatement(anyString());
    verify(mockPreparedStatement, times(1)).executeQuery();
    verify(mockResultSet, times(3)).next();
    //// verify(mockResultSet, times(1)).getInt(Fields.GENERATED_KEYS);

    assertFalse(user.isEmpty());
    assertEquals(2, user.size());
  }
}
