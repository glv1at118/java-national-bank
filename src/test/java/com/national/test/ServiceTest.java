package com.national.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.national.dao.JavaBankDaoImpl;
import com.national.service.Service;

public class ServiceTest {
	// 2 questions:
	// - should all the unit test methods have a void return type?
	// - should all the unit test methods be non-static?

	@Mock
	JavaBankDaoImpl innerDao;
	Service serve;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		this.serve = new Service();
		this.serve.setDaoField(this.innerDao);

		// data is for: acquireUserType_Test
		when(this.innerDao.findUserType("helloworld", "hello123")).thenReturn("customer");
		when(this.innerDao.findUserType("hellojava", "java123")).thenReturn("employee");
		when(this.innerDao.findUserType("hellosql", "sql123")).thenReturn("admin");

		// data is for: createUser_Test
		doNothing().when(this.innerDao).insertUser("javamockito", "abcde12345", "customer");

		// data is for: loginUser_Test
		when(this.innerDao.findUser("jakeoooio", "sadjg23124")).thenReturn(true);
		when(this.innerDao.findUser("botimobve", "1847443cdd")).thenReturn(false);

		// data is for: retrieveMoney_Test
		when(this.innerDao.findAccountId(1658195462876L)).thenReturn(603);
		when(this.innerDao.findUserId("", "")).thenReturn(468);
		when(this.innerDao.checkRecordInJunction(468, 603)).thenReturn(false);
		when(this.innerDao.getAccountState(1658195462876L)).thenReturn("active");
		when(this.innerDao.getAccountBalance(1658195462876L)).thenReturn(15483.16);
		doNothing().when(this.innerDao).decreaseAccountBalance(1658195462876L, 516.98);

		// data is for: retrieveMoneyAdmin_Test
		when(this.innerDao.findAccountId(4365927658430L)).thenReturn(435);
		when(this.innerDao.findUserId("", "")).thenReturn(76);
		when(this.innerDao.checkRecordInJunction(76, 435)).thenReturn(true);
		when(this.innerDao.getAccountState(4365927658430L)).thenReturn("frozen");
		when(this.innerDao.getAccountBalance(4365927658430L)).thenReturn(216485.69);
		doNothing().when(this.innerDao).decreaseAccountBalance(4365927658430L, 35674.51);
	}

	@Test
	public void acquireUserType_Test() {
		assertEquals("customer", serve.acquireUserType("helloworld", "hello123"));
		assertEquals("employee", serve.acquireUserType("hellojava", "java123"));
		assertEquals("admin", serve.acquireUserType("hellosql", "sql123"));
	}

	@Test
	public void createUser_Test() {
		assertTrue(serve.createUser("javamockito", "abcde12345", "customer"));
		assertFalse(serve.createUser("gj1234@#$", "abcde12345", "customer"));
		assertFalse(serve.createUser("rehasfg", "15.", "employee"));
		assertFalse(serve.createUser("voraene", "jf23jf3", "unknown"));
	}

	@Test
	public void loginUser_Test() {
		assertTrue(serve.loginUser("jakeoooio", "sadjg23124"));
		assertFalse(serve.loginUser("botimobve", "1847443cdd"));
	}

	@Test
	public void retrieveMoney_Test() {
		assertFalse(serve.retrieveMoney(1658195462876L, "516.98"));
	}

	@Test
	public void retrieveMoneyAdmin_Test() {
		assertFalse(serve.retrieveMoney(4365927658430L, "35674.51"));
	}

}
