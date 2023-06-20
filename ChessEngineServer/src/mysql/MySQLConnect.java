/**
 * @author Simon Greiderer
 * @author Marc Underrain
 * 
 * MySQL Klasse, wo alle Abfragen zur Datenbank geschrieben werden
 */
package mysql;

// Für Server gedacht!
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

import control.AnsiCode;

public class MySQLConnect {

	private final static String PREFIX = "MYSQL: ";

	private static Connection connection;

	private static final String DATABASE_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/chessEngine";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "";

	public Connection connect() {
		AnsiCode.sendMessage(PREFIX, "Connecting...");
		if (connection == null) {
			try {
				Class.forName(DATABASE_DRIVER);
				connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
				AnsiCode.sendSuccess(PREFIX, "Connected!");
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
		updateAllUserLoggedIn(0);
		return connection;
	}

	public void disconnect() {
		if (connection != null) {
			try {
				connection.close();
				connection = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static boolean register(final String username, final String email, final String password) {
		try {
			System.out.println(password);
			PreparedStatement stmt = connection
					.prepareStatement("insert into user (username, email, passwort) values (?, ?, md5(?))");
			stmt.setString(1, username);
			stmt.setString(2, email);
			stmt.setString(3, password);
			stmt.execute();
			stmt.close();
			AnsiCode.sendSuccess(PREFIX, "Erfolgreicher Insert!");
			return true;
		} catch (MySQLIntegrityConstraintViolationException e) {
			AnsiCode.sendError(PREFIX, "Doppelter Wert!");
		} catch (SQLException e) {
			return false;
		}
		return false;
	}

	public static boolean login(final String username, final String password) {
		try {
			PreparedStatement stmt = connection
					.prepareStatement("select * from user where username = ? and passwort = md5(?)");
			stmt.setString(1, username);
			stmt.setString(2, password);
			ResultSet result = stmt.executeQuery();
			if (result.next()) {
				if(result.getInt("isBanned") == 1) {
					AnsiCode.sendError(PREFIX, "Spieler " + username +" ist gebannt!");
					stmt.close();
					return false;
				}
				if (result.getInt("loggedIn") == 1) {
					AnsiCode.sendError(PREFIX, "Bereits angemeldet!");
					stmt.close();
					return false;
				} else {
					updateLoggedIn(username, 1);
					AnsiCode.sendSuccess(PREFIX, "Erfolgreiche Anmeldung!");
					stmt.close();
					return true;
				}
			} else {
				AnsiCode.sendError(PREFIX, "Fehlerhafte Anmeldung!");
				stmt.close();
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void updateUserPoints(final String username, final int points) {
		try {
			PreparedStatement stmt = connection
					.prepareStatement("update user set points = (points + ?), games = (games + 1) where username = ?");
			stmt.setInt(1, points);
			stmt.setString(2, username);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void updateLoggedIn(final String username, int loggedIn) {
		try {
			PreparedStatement stmt = connection.prepareStatement("update user set loggedIn = ? where username = ?");
			stmt.setInt(1, loggedIn);
			stmt.setString(2, username);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void updateBanned(final String username, int isBanned) {
		try {
			PreparedStatement stmt = connection.prepareStatement("update user set isBanned = ? where username = ?");
			stmt.setInt(1, isBanned);
			stmt.setString(2, username);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void updateAllUserLoggedIn(final int loggedIn) {
		try {
			PreparedStatement stmt = connection.prepareStatement("update user set loggedIn = ?");
			stmt.setInt(1, loggedIn);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static String selectUserPoints(final String username) {
		try {
			PreparedStatement stmt = connection.prepareStatement("select points from user where username = ?");
			stmt.setString(1, username);
			ResultSet result = stmt.executeQuery();
			if (result.next()) {
				return String.valueOf(result.getInt("points"));
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String selectUserGames(final String username) {
		try {
			PreparedStatement stmt = connection.prepareStatement("select games from user where username = ?");
			stmt.setString(1, username);
			ResultSet result = stmt.executeQuery();
			if (result.next()) {
				return String.valueOf(result.getInt("games"));
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ArrayList<String> selectAllPlayer() {
		try {
			PreparedStatement stmt = connection.prepareStatement("select username from user");
			ResultSet result = stmt.executeQuery();
			ArrayList<String> list = new ArrayList<String>(); 
			while (result.next()) {
				list.add(result.getString("username")); 
			} 
			return list;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ArrayList<String> selectAllBannedPlayer() {
		try {
			PreparedStatement stmt = connection.prepareStatement("select username from user where isBanned = 1");
			ResultSet result = stmt.executeQuery();
			ArrayList<String> list = new ArrayList<String>(); 
			while (result.next()) {
				list.add(result.getString("username")); 
			} 
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String selectUserRank(final String username) {
		try {
			PreparedStatement stmt = connection.prepareStatement("SELECT (select count(distinct(points))+1 "
					+ "as rank from user internal where internal.points > external.points order by points) as rank "
					+ "FROM user external where username = ? group by username");
			stmt.setString(1, username);
			ResultSet result = stmt.executeQuery();
			if (result.next()) {
				return String.valueOf(result.getInt("rank"));
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}