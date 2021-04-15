package com.national.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.national.model.Account;
import com.national.model.InfoList;
import com.national.model.User;

public class JavaBankDaoImpl implements JavaBankDao {

	public static Connection createConnection() throws SQLException {
		String dbUrl = "deleted due to protect personal info";
		String dbUserName = "deleted due to protect personal info";
		String dbPassWord = "deleted due to protect personal info";
		return DriverManager.getConnection(dbUrl, dbUserName, dbPassWord);
	}

	public boolean checkUserExists(String name) {
		try (Connection c = createConnection()) {
			String sql = "select name from users where name = ?";
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean findUser(String name, String password) {
		try (Connection c = createConnection()) {
			String sql = "select name, password, type from users where name = ? and password = ?";
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, name);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return true;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public String findUserType(String name, String password) {
		try (Connection c = createConnection()) {
			String sql = "select type from users where name = ? and password = ?";
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, name);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				return rs.getString(1);
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int findUserId(String name, String password) {
		try (Connection c = createConnection()) {
			String sql = "select user_id from users where name = ? and password = ?";
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setString(1, name);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	};

// 常规方式：
//	public void insertUser(String name, String password, String type) {
//		try (Connection c = createConnection()) {
//			String sql = "insert into users(name, password, type) values(?, ?, ?)";
//			PreparedStatement ps = c.prepareStatement(sql);
//			ps.setString(1, name);
//			ps.setString(2, password);
//			ps.setString(3, type);
//			ps.executeUpdate();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}

	// SQL函数方式：
	public void insertUser(String name, String password, String type) {
		try (Connection c = createConnection()) {
			String sql = "{? = call insert_user(?, ?, ?)}";
			CallableStatement cs = c.prepareCall(sql);
			cs.registerOutParameter(1, Types.VARCHAR);
			cs.setString(2, name);
			cs.setString(3, password);
			cs.setString(4, type);
			cs.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertAccount(long number, double balance, String state) {
		try (Connection c = createConnection()) {
			String sql = "insert into accounts(number, balance, state) values(?, ?, ?)";
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setLong(1, number);
			ps.setDouble(2, balance);
			ps.setString(3, state);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteAccount(long number) {
		try (Connection c = createConnection()) {
			String sql = "delete from accounts where number = ?";
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setLong(1, number);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int findAccountId(long number) {
		try (Connection c = createConnection()) {
			String sql = "select account_id from accounts where number = ?";
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setLong(1, number);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	};

	public void insertJunction(int user_id, int account_id) {
		try (Connection c = createConnection()) {
			String sql = "insert into users_accounts_junction(user_id, account_id) values(?, ?)";
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, user_id);
			ps.setInt(2, account_id);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void increaseAccountBalance(long number, double increment) {
		try (Connection c = createConnection()) {
			String sql = "update accounts set balance = balance + ? WHERE number = ?";
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setDouble(1, increment);
			ps.setLong(2, number);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void decreaseAccountBalance(long number, double decrement) {
		try (Connection c = createConnection()) {
			String sql = "update accounts set balance = balance - ? WHERE number = ?";
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setDouble(1, decrement);
			ps.setLong(2, number);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void approveAccount(long number) {
		try (Connection c = createConnection()) {
			String sql = "update accounts set state = 'active' WHERE number = ?";
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setLong(1, number);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void denyAccount(long number) {
		try (Connection c = createConnection()) {
			String sql = "update accounts set state = 'frozen' WHERE number = ?";
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setLong(1, number);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<User> getAllCustomers() {
		List<User> userList = new ArrayList<>();
		try (Connection c = createConnection()) {
			String sql = "select name, type from users where type = 'customer'";
			PreparedStatement ps = c.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String name = rs.getString(1);
				String type = rs.getString(2);
				User u = new User(name, type);
				userList.add(u);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userList;
	}

	public List<User> getAllEmployeeAndAdmin() {
		List<User> userList = new ArrayList<>();
		try (Connection c = createConnection()) {
			String sql = "select name, password, type from users where type != 'customer'";
			PreparedStatement ps = c.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String name = rs.getString(1);
				String password = rs.getString(2);
				String type = rs.getString(3);
				User u = new User(name, password, type);
				userList.add(u);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userList;
	}

	public List<Account> getAllAccounts() {
		List<Account> accountList = new ArrayList<>();
		try (Connection c = createConnection()) {
			String sql = "select number, balance, state from accounts";
			PreparedStatement ps = c.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				long number = rs.getLong(1);
				double balance = rs.getDouble(2);
				String state = rs.getString(3);
				Account a = new Account(number, balance, state);
				accountList.add(a);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return accountList;
	}

	public List<InfoList> getComprehensiveList() {
		List<InfoList> infoList = new ArrayList<>();
		try (Connection c = createConnection()) {
			String sql = "select users.name, users.password, users.type, users_accounts_junction.user_id, \r\n"
					+ "users_accounts_junction.account_id, accounts.number, accounts.balance, accounts.state from \r\n"
					+ "users inner join users_accounts_junction\r\n"
					+ "on users.user_id = users_accounts_junction.user_id\r\n" + "inner join accounts\r\n"
					+ "on users_accounts_junction.account_id = accounts.account_id";

			PreparedStatement ps = c.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String name = rs.getString(1);
				String password = rs.getString(2);
				String type = rs.getString(3);
				int user_id = rs.getInt(4);
				int account_id = rs.getInt(5);
				long number = rs.getLong(6);
				double balance = rs.getDouble(7);
				String state = rs.getString(8);
				InfoList info = new InfoList(name, password, type, user_id, account_id, number, balance, state);
				infoList.add(info);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return infoList;
	}

	public List<InfoList> getComprehensiveListWithoutPw() {
		List<InfoList> infoList = new ArrayList<>();
		try (Connection c = createConnection()) {
			String sql = "select users.name, users.type, accounts.number, accounts.balance, accounts.state \r\n"
					+ "from users inner join users_accounts_junction\r\n"
					+ "on users.user_id = users_accounts_junction.user_id\r\n" + "inner join accounts\r\n"
					+ "on users_accounts_junction.account_id = accounts.account_id";
			PreparedStatement ps = c.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String name = rs.getString(1);
				String type = rs.getString(2);
				long number = rs.getLong(3);
				double balance = rs.getDouble(4);
				String state = rs.getString(5);
				InfoList info = new InfoList(name, "", type, -1, -1, number, balance, state);
				infoList.add(info);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return infoList;
	}

	public List<Account> getAllAccountsWithPending() {
		List<Account> accountList = new ArrayList<>();
		try (Connection c = createConnection()) {
			String sql = "select number, balance, state from accounts where state = 'pending'";
			PreparedStatement ps = c.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				long number = rs.getLong(1);
				double balance = rs.getDouble(2);
				String state = rs.getString(3);
				Account a = new Account(number, balance, state);
				accountList.add(a);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return accountList;
	}

	public List<Account> getAllAccountsWithFrozen() {
		List<Account> accountList = new ArrayList<>();
		try (Connection c = createConnection()) {
			String sql = "select number, balance, state from accounts where state = 'frozen'";
			PreparedStatement ps = c.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				long number = rs.getLong(1);
				double balance = rs.getDouble(2);
				String state = rs.getString(3);
				Account a = new Account(number, balance, state);
				accountList.add(a);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return accountList;
	}

	public List<Account> getAllAccountsWithUserId(int user_id) {
		List<Account> accountList = new ArrayList<>();
		try (Connection c = createConnection()) {
			String sql = "select number, balance, state \r\n" + "from users_accounts_junction inner join accounts\r\n"
					+ "on users_accounts_junction.account_id = accounts.account_id\r\n" + "where user_id = ?";
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, user_id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				long number = rs.getLong(1);
				double balance = rs.getDouble(2);
				String state = rs.getString(3);
				Account a = new Account(number, balance, state);
				accountList.add(a);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return accountList;
	}

	public double getAccountBalance(long number) {
		double balance = 0;
		try (Connection c = createConnection()) {
			String sql = "select balance from accounts where number = ?";
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setLong(1, number);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				balance = rs.getDouble(1);
				return balance;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return balance;
	}

	public boolean checkRecordInJunction(int user_id, int account_id) {
		try (Connection c = createConnection()) {
			String sql = "select junc_id from users_accounts_junction uaj where uaj.user_id = ? and uaj.account_id = ?";
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setInt(1, user_id);
			ps.setInt(2, account_id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	};

	public String getAccountState(long number) {
		try (Connection c = createConnection()) {
			String sql = "select state from accounts where number = ?";
			PreparedStatement ps = c.prepareStatement(sql);
			ps.setLong(1, number);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}
}
